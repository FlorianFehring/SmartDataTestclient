package de.fhbielefeld.smartdata.testcllient.rest;

import de.fhbielefeld.scl.rest.util.WebTargetCreator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

/**
 * Test methods for the performance of RecordsRessource
 *
 * @author Florian Fehring
 */
public class RecordsPerformanceTest {

    private static LocalDateTime startDateTime;
    // Testing SmartData on Server
//    private static final String SERVER = "http://epigraf01.ad.fh-bielefeld.de:8080/SmartDataEnviron/smartdata";
//    private static final String RESOURCE = "records";
//    private static final String STORAGE = "smartmonitoring";
//    private static final String COLLECTION = "data_10";

    // Testing SmartMonitoring on Server
//    private static final String SERVER = "http://epigraf1.ad.fh-bielefeld.de:8080/SmartMonitoringBackendEnviron"; // "http://localhost:8080/SmartData/smartdata/";
//    private static final String RESOURCE = "data";
//    private static final String STORAGE = "smartmonitoring";
//    private static final String COLLECTION = "getSets";
    // Testing SmartData local
    private static final String SERVER = "http://localhost:8080/SmartData/smartdata/";
    private static final String RESOURCE = "records";
    private static final String STORAGE = "test";
    private static final String COLLECTION = "testcol";

    private static WebTarget webTarget;
    private static final boolean PRINT_DEBUG_MESSAGES = true;
    private static final int createRuns = 100;
    private static final int getRuns = 100;
    private static final int getSets = 100; // Number of datasets requested with one request
    private static final List<RecordsCreateThreadData>createSetsMultithread = new ArrayList<>();
    
    public RecordsPerformanceTest() {
        createSetsMultithread.add(new RecordsCreateThreadData("testcol", STORAGE, 100));
        createSetsMultithread.add(new RecordsCreateThreadData("testcol", STORAGE, 100));
        
        startDateTime = LocalDateTime.now();
        webTarget = WebTargetCreator.createWebTarget(SERVER, RESOURCE);
    }

    /**
     * Test createing a simple dataset
     *
     * @return true if dataset was created
     */
    public boolean performanceCreateSetSimple() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        System.out.println("----performanceCreateSetSimple()----");
        System.out.println("Starting performance test for creating of " + createRuns + " datasets.");
        
        // Create target
        WebTarget target = webTarget
                .path(COLLECTION)
                .queryParam("storage", STORAGE);
        // Create dataset
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", "testwert");
        builder.add("float_value", 12.2323);
        builder.add("int_value", 12);
        builder.add("ts_value", "2011-12-30T10:15:30");
        JsonObject dataObject = builder.build();
        Entity<String> collectiondef = Entity.json(dataObject.toString());

        // Measurements storage
        long[] neededTimes = new long[createRuns];
        long neededTimeSum = 0;
        long quickestTime = 300000000;
        long slowestTime = 0;
        int[] neededSize = new int[createRuns];
        long neededSizeSum = 0;
        String exampleResponse = null;

        double donePercentage;
        for (int i = 0; i < createRuns; i++) {
            double curRun = i;
            double allRun = createRuns;
            donePercentage = curRun / allRun * 100;
            if(donePercentage % 1 == 0) {
                System.out.println(donePercentage + "% done (" + i + " datasets)");
            }
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
                return false;
            }
            MultivaluedMap<String, Object> headers = response.getHeaders();
            Integer size = Integer.parseInt((String) headers.getFirst("Content-Length"));
            neededSize[i] = size;
            neededSizeSum += size;
            // Get example response
            if (i == createRuns - 1) {
                exampleResponse = response.readEntity(String.class);
            }
        }

        // Calculate statistics
        System.out.println("======== CREATE performance ==========");
        double neededTimeSec = neededTimeSum / 1000 / 1000 / 1000;
        System.out.println("Total used time: " + neededTimeSec + " sec");
        System.out.println("Average time:    " + neededTimeSec / createRuns + " sec");
        System.out.println("Quickest request:" + quickestTime / 1000 / 1000 + " ms");
        System.out.println("Slowest request: " + slowestTime / 1000 / 1000 + " ms");
        System.out.println("Total bytes:     " + neededSizeSum + " byte");
        System.out.println("Average size:    " + neededSizeSum / createRuns + " byte");
        System.out.println("---- Example response ----");
        System.out.println(exampleResponse);
        return true;
    }

    public boolean performanceCreateSetMultiThread() {
        
        // Create and run threads for simulating simultanous requests
        for(RecordsCreateThreadData curRequest : createSetsMultithread) {
            RecordsCreatePerformanceThread curThread = new RecordsCreatePerformanceThread(webTarget,curRequest);
            curThread.start();
        }
        return true;
    }
    
    /**
     * Tests to get a simple dataset
     *
     * @return
     */
    public boolean performanceGetSetSimple() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path(COLLECTION)
                .path("1")
                .queryParam("storage", STORAGE);
//        .queryParam("ooid", 10)
//        .queryParam("limit", 1);

        // Measurements storage
        long[] neededTimes = new long[getRuns];
        long neededTimeSum = 0;
        long quickestTime = 300000000;
        long slowestTime = 0;
        int[] neededSize = new int[getRuns];
        long neededSizeSum = 0;
        String exampleResponse = null;

        for (int i = 0; i < getRuns; i++) {
            // Measure needed time
            long start = System.nanoTime();
            Response response = target.request(MediaType.APPLICATION_JSON).get();
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
            if (Response.Status.OK.getStatusCode() != response.getStatus()) {
                String responseText = response.readEntity(String.class);
                System.err.println("ERROR executing performanceGetSetSimple:");
                System.err.println(response.getStatusInfo());
                System.err.println(responseText);
                return false;
            }
            MultivaluedMap<String, Object> headers = response.getHeaders();
            String contLength = (String) headers.getFirst("Content-Length");
            if (contLength == null) {

            } else {
                Integer size = Integer.parseInt(contLength);
                neededSize[i] = size;
                neededSizeSum += size;
            }
            // Get example response
            if (i == createRuns - 1) {
                exampleResponse = response.readEntity(String.class);
            }
        }
        // Calculate statistics
        System.out.println("======== GET performance ==========");
        double neededTimeMikro = neededTimeSum / 1000;
        double neededTimeMilli = neededTimeMikro / 1000;
        double neededTimeSec = neededTimeMilli / 1000;
        System.out.println("Total used time: " + neededTimeSec + " sec");
        System.out.println("Average time:    " + neededTimeSec / createRuns + " sec");
        double quickestMikro = quickestTime / 1000;
        System.out.println("Quickest request:" + quickestMikro / 1000 + " ms");
        double slowestMikro = slowestTime / 1000;
        System.out.println("Slowest request: " + slowestMikro / 1000 + " ms");
        System.out.println("Total bytes:     " + neededSizeSum + " byte");
        System.out.println("Average size:    " + neededSizeSum / createRuns + " byte");
        System.out.println("---- Example response ----");
        System.out.println(exampleResponse);

        return true;
    }

    /**
     * Tests to get a simple datasets
     *
     * @return
     */
    public boolean performanceGetSetsSimple() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path(COLLECTION)
                .queryParam("storage", STORAGE)
                .queryParam("size", getSets);
        //.queryParam("ooid", 10)
        //.queryParam("limit", getSets);

        // Measurements storage
        long[] neededTimes = new long[getRuns];
        long neededTimeSum = 0;
        long quickestTime = 300000000;
        long slowestTime = 0;
        long[] neededStream = new long[getRuns];
        long neededStreamSum = 0;
        long quickestStream = 300000000;
        long slowestStream = 0;
        int[] neededSize = new int[getRuns];
        long neededSizeSum = 0;
        String exampleResponse = null;

        for (int i = 0; i < getRuns; i++) {
            // Measure needed time
            long start = System.nanoTime();
            Response response = target.request(MediaType.APPLICATION_JSON).get();
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
            if (Response.Status.OK.getStatusCode() != response.getStatus()) {
                String responseText = response.readEntity(String.class);
                System.err.println("ERROR executing performanceGetSetsSimple:");
                System.err.println(response.getStatusInfo());
                System.err.println(responseText);
                return false;
            }
            MultivaluedMap<String, Object> headers = response.getHeaders();
            String contlength = (String) headers.getFirst("Content-Length");
            if (contlength != null) {
                Integer size = Integer.parseInt(contlength);
                neededSize[i] = size;
                neededSizeSum += size;
                // Get example response
                if (i == createRuns - 1) {
                    exampleResponse = response.readEntity(String.class);
                }
            } else {
                // Measure stream read time
                long startStream = System.nanoTime();
                exampleResponse = response.readEntity(String.class);
                long finishStream = System.nanoTime();
                System.out.println(exampleResponse);
                neededStream[i] = finishStream - startStream;
                neededStreamSum += neededStream[i];
                if (neededStream[i] < quickestStream) {
                    quickestStream = neededStream[i];
                }
                if (neededStream[i] > slowestStream) {
                    slowestStream = neededStream[i];
                }
            }
        }
        // Calculate statistics
        System.out.println("======== GET SETS performance ==========");
        double neededTimeMikro = neededTimeSum / 1000;
        double neededTimeMilli = neededTimeMikro / 1000;
        double neededTimeSec = neededTimeMilli / 1000;
        System.out.println("Total used time: " + neededTimeSec + " sec (" + neededTimeSum + " ns)");
        System.out.println("Average time:    " + neededTimeSec / createRuns + " sec (" + neededTimeSum / createRuns + " ns)");
        double quickestMikro = quickestTime / 1000;
        System.out.println("Quickest request:" + quickestMikro / 1000 + " ms");
        double slowestMikro = slowestTime / 1000;
        System.out.println("Slowest request: " + slowestMikro / 1000 + " ms");
        if (neededSizeSum > 0) {
            // Data transfered as one
            System.out.println("Total bytes:     " + neededSizeSum + " byte");
            System.out.println("Average size:    " + neededSizeSum / createRuns + " byte");
        } else {
            // Data transfered as stream
            double neededStreamMikro = neededStreamSum / 1000;
            double neededStreamMilli = neededStreamMikro / 1000;
            double neededStreamSec = neededStreamMilli / 1000;
            System.out.println("Total used time for stream: " + neededStreamSec + " sec");
            System.out.println("Average time for stream:    " + neededStreamSec / createRuns + " sec");
            double quickestStreamMikro = quickestStream / 1000;
            System.out.println("Quickest stream:" + quickestStreamMikro / 1000 + " ms");
            double slowestStreamMikro = slowestStream / 1000;
            System.out.println("Slowest stream: " + slowestStreamMikro / 1000 + " ms");
        }
        System.out.println("---- Example response ----");
        System.out.println(exampleResponse);

        return true;
    }
}
