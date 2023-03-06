package de.fhbielefeld.smartdata.testcllient.rest;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

/**
 * A thread for creating datasets with simulated multiple clients
 *
 * @author Florian Fehring
 */
public class RecordsCreatePerformanceThread extends Thread {

    private WebTarget webTarget;
    private RecordsCreateThreadData threadData;
    private boolean result;

    public RecordsCreatePerformanceThread(WebTarget webTarget, RecordsCreateThreadData threadData) {
        this.webTarget = webTarget;
        this.threadData = threadData;
    }

    @Override
    public void run() {
        System.out.println("=======================");
        System.out.println("== STARTING THREAD ====");
        System.out.println("=======================");
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        // Create target
        WebTarget target = webTarget
                .path(this.threadData.getCollection())
                .queryParam("storage", this.threadData.getStorage());
        // Create dataset
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", "testwert");
        builder.add("float_value", 12.2323);
        builder.add("int_value", 12);
        builder.add("ts_value", "2011-12-30T10:15:30");
        JsonObject dataObject = builder.build();
        Entity<String> collectiondef = Entity.json(dataObject.toString());

        // Measurements storage
        long[] neededTimes = new long[this.threadData.getCreateRuns()];
        long neededTimeSum = 0;
        long quickestTime = 300000000;
        long slowestTime = 0;
        int[] neededSize = new int[this.threadData.getCreateRuns()];
        long neededSizeSum = 0;
        String exampleResponse = null;

        for (int i = 0; i < this.threadData.getCreateRuns(); i++) {
            // Measure needed time
            long start = System.nanoTime();
            Response response = target.request(MediaType.APPLICATION_JSON).post(collectiondef);
            long finish = System.nanoTime();
            neededTimes[i] = finish - start;
            neededTimeSum += neededTimes[i];
            if (neededTimes[i] < quickestTime) {
                quickestTime = neededTimes[i];
            }
            if (neededTimes[i] > slowestTime) {
                slowestTime = neededTimes[i];
            }
            // Check status
            if (Response.Status.CREATED.getStatusCode() != response.getStatus()) {
                String responseText = response.readEntity(String.class);
                System.err.println("ERROR executing performanceCreateSetSimple:");
                System.err.println(response.getStatusInfo());
                System.err.println(responseText);
                this.result = false;
            }
            MultivaluedMap<String, Object> headers = response.getHeaders();
            Integer size = Integer.parseInt((String) headers.getFirst("Content-Length"));
            neededSize[i] = size;
            neededSizeSum += size;
            // Get example response
            if (i == this.threadData.getCreateRuns() - 1) {
                exampleResponse = response.readEntity(String.class);
            }
        }

        // Calculate statistics
        System.out.println("======== CREATE performance ==========");
        double neededTimeSec = neededTimeSum / 1000 / 1000 / 1000;
        System.out.println("Total used time: " + neededTimeSec + " sec");
        System.out.println("Average time:    " + neededTimeSec / this.threadData.getCreateRuns() + " sec");
        System.out.println("Quickest request:" + quickestTime / 1000 / 1000 + " ms");
        System.out.println("Slowest request: " + slowestTime / 1000 / 1000 + " ms");
        System.out.println("Total bytes:     " + neededSizeSum + " byte");
        System.out.println("Average size:    " + neededSizeSum / this.threadData.getCreateRuns() + " byte");
        System.out.println("---- Example response ----");
        System.out.println(exampleResponse);
        this.result = true;
    }

    public boolean isResult() {
        return result;
    }
}
