package de.fhbielefeld.smartdata.testcllient.rest;

import de.fhbielefeld.scl.rest.util.WebTargetCreator;
import java.io.StringReader;
import java.time.LocalDateTime;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.stream.JsonParser;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Test methods for the RecordsRessource
 *
 * @author Florian Fehring
 */
public class RecordsRessourceTest {

    private static LocalDateTime startDateTime;
    private static final String SERVER = "http://localhost:8080/SmartData/smartdata/";
    private static final String RESOURCE = "records";
    private static final String STORAGE = "test";
    private static WebTarget webTarget;
    private static final boolean PRINT_DEBUG_MESSAGES = true;

    public RecordsRessourceTest() {
        startDateTime = LocalDateTime.now();
        System.out.println("TEST beforeAll");
        webTarget = WebTargetCreator.createWebTarget(SERVER, RESOURCE);
    }

    /**
     * Test createing a simple dataset
     *
     * @return true if dataset was created
     */
    public boolean testCreateSetSimple() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("testtable")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", "testwert");
        builder.add("float_value", 12.2323);
        builder.add("int_value", 12);
        builder.add("ts_value","2011-12-30T10:15:30");
        JsonObject dataObject = builder.build();
        Entity<String> tabledef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(tabledef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateSetSimple---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a simple dataset
     *
     * @return
     */
    public boolean testGetSetSimple() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testtable")
                .path("1")
                .queryParam("storage", STORAGE);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetSimple---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset with only included columns
     *
     * @return
     */
    public boolean testGetSetIncludes() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testtable")
                .path("1")
                .queryParam("storage", STORAGE)
                .queryParam("includes", "");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetIncludes---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Test createing multiple sets at once
     *
     * @return
     */
    public boolean testCreateSetsSimple() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("testtable")
                .queryParam("storage", STORAGE);

        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job1 = Json.createObjectBuilder();
        job1.add("name", "testwert1");
        job1.add("float_value", 12.2323);
        job1.add("int_value", 12);
        job1.add("ts_value","31.12.2019 12:14");
        jab.add(job1);
        JsonObjectBuilder job2 = Json.createObjectBuilder();
        job2.add("name", "testwert2");
        job2.add("float_value", -11.1111);
        job2.add("int_value", -11);
        job2.add("ts_value", "2011-12-30T10:15:30");
        jab.add(job2);
        JsonObjectBuilder job3 = Json.createObjectBuilder();
        job3.add("name", "testwert3");
        job3.add("float_value", 42.0);
        job3.add("int_value", 42);
        job3.add("ts_value","2011-12-30 10:15:30.123");
        jab.add(job3);
        JsonArray dataObject = jab.build();
        Entity<String> tabledef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(tabledef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateSetsSimple---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a simple dataset
     *
     * @return
     */
    public boolean testEqualsFilter() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testtable")
                .queryParam("storage", STORAGE);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testEqualsFilter---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Tests if values with unicode chars can be inserted. Note: If this does
     * not succseed the problem can be, that the underliyng database is not UTF8
     * encoded.
     *
     * @return
     */
    public boolean testCreateSetUnicode() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("testtable")
                .queryParam("storage", STORAGE);

        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job1 = Json.createObjectBuilder();
        job1.add("name", "Oława - Żołnierzy");
        job1.add("float_value", 99.999);
        job1.add("int_value", 99);
        jab.add(job1);
        JsonArray dataObject = jab.build();
        Entity<String> tabledef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(tabledef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateSetUnicode---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Test get multiple sets
     *
     * @return
     */
    public boolean testGetSetsSimple() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testtable")
                .queryParam("storage", STORAGE);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetsSimple---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 4) {
                System.out.println("Expected that there are 4 datasets, but there where " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Test updateing a simple dataset
     *
     * @return true if dataset was updated
     */
    public boolean testUpdateSetSimple() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("testtable")
                .path("1")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", "neuer testwert");
        builder.add("float_value", 0.3333);
        builder.add("int_value", 0);
        JsonObject dataObject = builder.build();
        Entity<String> tabledef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).put(tabledef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testUpdateSetSimple---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Test updateing three datasets at once
     *
     * @return true if dataset was updated
     */
    public boolean testUpdateSetsSimple() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("testtable")
                .queryParam("storage", STORAGE);
        JsonArrayBuilder sets = Json.createArrayBuilder();
        JsonObjectBuilder set1 = Json.createObjectBuilder();
        set1.add("id",1);
        set1.add("name", "neuer testwert id 1");
        set1.add("float_value", 0.3333);
        set1.add("int_value", 0);
        sets.add(set1);
        JsonObjectBuilder set2 = Json.createObjectBuilder();
        set2.add("id",2);
        set2.add("name", "neuer testwert id 2");
        set2.add("float_value", 0.3333);
        set2.add("int_value", 0);
        sets.add(set2);
        JsonObjectBuilder set3 = Json.createObjectBuilder();
        set3.add("id",3);
        set3.add("name", "neuer testwert id 3");
        set3.add("float_value", 0.3333);
        set3.add("int_value", 0);
        sets.add(set3);
        JsonObjectBuilder env = Json.createObjectBuilder();
        env.add("records", sets);
        
        Entity<String> tabledef = Entity.json(env.build().toString());

        Response response = target.request(MediaType.APPLICATION_JSON).put(tabledef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testUpdateSetsSimple---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Test the deletion of an dataset
     * 
     * @return true if the dataset was deleted
     */
    public boolean testDeleteSet() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }
        
        // Delete dataset one
        WebTarget target = webTarget
                .path("testtable")
                .path("1")
                .queryParam("storage", STORAGE);

        Response response = target.request(MediaType.APPLICATION_JSON).delete();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testDeleteSet---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Test the deletion of multiple datasets
     * 
     * @return true if the dataset was deleted
     */
    public boolean testDeleteSets() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }
        
        // Delete dataset one
        WebTarget target = webTarget
                .path("testtable")
                .path("2,3")
                .queryParam("storage", STORAGE);

        Response response = target.request(MediaType.APPLICATION_JSON).delete();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testDeleteSets---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }
}
