package de.fhbielefeld.smartdata.testcllient.rest;

import de.fhbielefeld.scl.rest.util.WebTargetCreator;
import java.io.StringReader;
import java.util.Random;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
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

    private static final String SERVER = "http://localhost:8080/SmartData/smartdata/";
    private static final String RESOURCE = "records";
    private static final String STORAGE = "test";
    private static WebTarget webTarget;
    private static final boolean PRINT_DEBUG_MESSAGES = true;

    private int createdSets = 0;
    private int createdGeoSets = 0;

    public RecordsRessourceTest() {
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
                .path("testcol")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", "testwert");
        builder.add("float_value", 12.23);
        builder.add("int_value", 12);
        builder.add("bool_value", true);
        builder.add("ts_value", "2011-12-30T10:15:30");
        builder.add("json_value", "{ \"customer\": \"John Doe\", \"items\": {\"product\": \"Beer\",\"qty\": 6}}");
        JsonObject dataObject = builder.build();
        Entity<String> dataset = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(dataset);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateSetSimple---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            createdSets++;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Test createing a dataset with string id
     *
     * @return true if dataset was created
     */
    public boolean testCreateSetStrid() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("colwithstrid")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", "mytestid8cas");
        builder.add("name", "testwert");

        JsonObject dataObject = builder.build();
        Entity<String> dataset = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(dataset);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateSetStrid---");
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
     * Create a flattend dataset
     * 
     * @return true if the collection was created
     */
    public boolean testCreateSetFlattend() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("colflattend")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder dataarr = Json.createArrayBuilder();
        JsonObjectBuilder dataset = Json.createObjectBuilder();
        // Name column
        dataset.add("name", "some example value");
        // Create flattend data
        Random rand = new Random();
        for (int i = 0; i < 255; i++) {
            dataset.add("U" + i, rand.nextDouble());
            dataset.add("I" + i, rand.nextDouble());
        }
        dataarr.add(dataset);
        builder.add("records", dataarr);
        JsonObject dataObject = builder.build();
        Entity<String> datadef = Entity.json(dataObject.toString());
        Response response = target.request(MediaType.APPLICATION_JSON).post(datadef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateSetFlattend---");
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
     * Test createing a dataset with binary data
     *
     * @return true if dataset was created
     */
    public boolean testCreateSetBinary() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("testcolbinary")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("bytea_value", "data:image/jpeg;base64,/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAAAjAAD/4QMdaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjMtYzAxMSA2Ni4xNDU2NjEsIDIwMTIvMDIvMDYtMTQ6NTY6MjcgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkY4MkE2N0I4RjgyNTExRTM4QUI3ODY1NDVDMUVGNkE4IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkY4MkE2N0I3RjgyNTExRTM4QUI3ODY1NDVDMUVGNkE4IiB4bXA6Q3JlYXRvclRvb2w9IkNPT0xQSVggUzkxMDBWMS4wICAgICAgICAgICAgICAiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0iOUY4MjFFMkEwOEZDQUZFQzU5RERGQjVBMDZGOUM0NjEiIHN0UmVmOmRvY3VtZW50SUQ9IjlGODIxRTJBMDhGQ0FGRUM1OURERkI1QTA2RjlDNDYxIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+/+0ASFBob3Rvc2hvcCAzLjAAOEJJTQQEAAAAAAAPHAFaAAMbJUccAgAAAgACADhCSU0EJQAA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");
        JsonObject dataObject = builder.build();
        Entity<String> dataset = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(dataset);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateSetBinary---");
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
     * Test recreateing a simple dataset
     *
     * @return true if dataset was rejected
     */
    public boolean testReCreateSetSimple() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("testcol")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", "recreateTestset");
        builder.add("float_value", 77.77);
        builder.add("int_value", 77);
        builder.add("bool_value", true);
        builder.add("ts_value", "2015-04-20T10:15:30");
        JsonObject dataObject = builder.build();
        Entity<String> dataset = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(dataset);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testReCreateSetSimple---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            createdSets++;
            // Add dataset id to dataset
            builder.add("id", Integer.parseInt(responseText));
            // Resent dataset
            JsonObject dataObject2 = builder.build();
            Entity<String> dataset2 = Entity.json(dataObject2.toString());

            Response response2 = target.request(MediaType.APPLICATION_JSON).post(dataset2);
            String responseText2 = response2.readEntity(String.class);
            if (PRINT_DEBUG_MESSAGES) {
                System.out.println("Response from resent:");
                System.out.println(response2.getStatusInfo());
                System.out.println(responseText2);
            }
            if (Response.Status.CONFLICT.getStatusCode() == response2.getStatus()) {
                return true;
            }
        }
        return false;
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

        WebTarget target = webTarget.path("testcol")
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
     * Tests to get a simple dataset
     *
     * @return
     */
    public boolean testGetSetBinaryData() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcolbinary")
                .path("1")
                .queryParam("includes","bytea_value")
                .queryParam("storage", STORAGE);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetBinaryData---");
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
            JsonObject recordobj = recordsArr.get(0).asJsonObject();
            JsonString jstr = recordobj.getJsonString("bytea_value");
            if (jstr == null) {
                System.out.println(">bytea_value< attribute is missing.");
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Tests to get a dataset with json column
     *
     * @return
     */
    public boolean testGetSetWithJson() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .path("1")
                .queryParam("storage", STORAGE);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetWithJson---");
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
            JsonObject recordobj = recordsArr.get(0).asJsonObject();
            if (recordobj.getJsonObject("json_value") == null) {
                System.out.println(">json_value< attribute is missing.");
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset with unicode data
     *
     * @return
     */
    public boolean testGetSetUnicode() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetUnicode---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            if (!responseText.contains("?")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests to get a dataset deflatted
     *
     * @return
     */
    public boolean testGetSetDeflatted() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("colflattend")
                .path("1")
                .queryParam("storage", STORAGE)
                .queryParam("deflatt", true);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetDeflatted---");
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
     * Tests to get a dataset with only included attributes
     *
     * @return
     */
    public boolean testGetSetIncludes() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
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
                .path("testcol")
                .queryParam("storage", STORAGE);

        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder set1 = Json.createObjectBuilder();
        set1.add("name", "testwert1");
        set1.add("float_value", 12.2323);
        set1.add("int_value", 12);
        set1.add("bool_value", true);
        set1.add("ts_value", "31.12.2018 12:14");
        set1.add("txt_value", "Datensatz für cs-filter in-filter1");
        JsonObjectBuilder subjson = Json.createObjectBuilder();
        subjson.add("sub_name", "subjson");
        subjson.add("sub_boolean", false);
        subjson.add("sub_number", 10);
        set1.add("json_value", subjson);
        jab.add(set1);
        JsonObjectBuilder set2 = Json.createObjectBuilder();
        set2.add("name", "testwert2");
        set2.add("float_value", -11.1111);
        set2.add("int_value", -11);
        set2.add("bool_value", false);
        set2.add("ts_value", "2013-12-30T10:15:30");
        set2.add("txt_value", "sw-filter Datensatz in-filter2");
        jab.add(set2);
        JsonObjectBuilder set3 = Json.createObjectBuilder();
        set3.add("name", "testwert3");
        set3.add("float_value", 42.0);
        set3.add("int_value", 42);
        set3.add("bool_value", true);
        set3.add("ts_value", "2011-12-31 10:15:30.123");
        set3.add("txt_value", "Test für ew-filter");
        jab.add(set3);
        JsonObjectBuilder set4 = Json.createObjectBuilder();
        set4.add("name", "testwert_nullset");
        set4.add("int_value", 0);
        jab.add(set4);
        JsonArray dataObject = jab.build();
        Entity<String> datasets = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(datasets);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateSetsSimple---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            createdSets += 4;
            if (responseText.contains(",")) {
                return true;
            }
        }
        return false;
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

        WebTarget target = webTarget.path("testcol")
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
                .path("testcol")
                .queryParam("storage", STORAGE);

        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job1 = Json.createObjectBuilder();
        job1.add("name", "testwert Oława - Żołnierzy");
        job1.add("float_value", 88.888);
        job1.add("int_value", 88);
        jab.add(job1);
        JsonArray dataObject = jab.build();
        Entity<String> coldef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(coldef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateSetUnicode---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            createdSets++;
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
            System.out.println("WebTarget is null!");
        }

        WebTarget target = webTarget.path("testcol")
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
            if (recordsArr.size() != 5) {
                System.out.println("Expected that there are 5 datasets, but there where " + recordsArr.size());
                return false;
            }
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
    public boolean testGetCountSets() {
        if (webTarget == null) {
            System.out.println("WebTarget is null!");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("countonly", true);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetCountSets---");
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
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there is one dataset, but there where " + recordsArr.size());
                return false;
            }
            JsonNumber countVal = recordsArr.get(0).asJsonObject().getJsonNumber("count");
            if(countVal == null) {
                System.out.println("Expected that there is one attribute called >count< but is not.");
                return false;
            }
            if(countVal.intValue() != createdSets) {
                System.out.println("Expected count to deliver >" + createdSets + "< but there where >" + countVal.intValue() + " sets.");
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Test get multiple sets from not existing collection
     *
     * @return
     */
    public boolean testGetSetsNotExists() {
        if (webTarget == null) {
            System.out.println("WebTarget is null!");
        }

        WebTarget target = webTarget.path("notexistingtable")
                .queryParam("storage", STORAGE);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetsNotExists---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.INTERNAL_SERVER_ERROR.getStatusCode() == response.getStatus()) {
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray errorsArr = responseObj.getJsonArray("errors");
            if (errorsArr == null) {
                System.out.println(">errors< attribute is missing.");
                return false;
            }
            String errStr = errorsArr.getString(0);
            if (!errorsArr.getString(0).contains("does not exists.")) {
                System.out.println("Expected >does not exists.< but is: " + errStr);
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Test get multiple sets with limit
     *
     * @return
     */
    public boolean testGetSetsLimit() {
        if (webTarget == null) {
            System.out.println("WebTarget is null!");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("size", 2);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetsLimit---");
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
            if (recordsArr.size() != 2) {
                System.out.println("Expected that there are 2 datasets, but there where " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Test get multiple sets with negative limit
     *
     * @return
     */
    public boolean testGetSetsNegativeLimit() {
        if (webTarget == null) {
            System.out.println("WebTarget is null!");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("size", -10);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetsNegativeLimit---");
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
            if (recordsArr.size() != createdSets) {
                System.out.println("Expected that there are " + createdSets + " datasets, but there where " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Test get multiple sets with includes one given attribute
     *
     * @return
     */
    public boolean testGetSetsWithExistingInclude() {
        if (webTarget == null) {
            System.out.println("WebTarget is null!");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("includes", "name");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetsWithExistingInclude---");
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
            for (int i = 0; i < recordsArr.size(); i++) {
                JsonObject curObj = recordsArr.getJsonObject(i);
                if (!curObj.containsKey("name")) {
                    System.out.println(">name< attribute not found in result.");
                    return false;
                }
                if (curObj.containsKey("int_value")) {
                    System.out.println(">int_value< attribute was found in result but should not be there.");
                    return false;
                }
                // Test for attr that should not be existend
                if (curObj.containsKey("float_value")) {
                    System.out.println(">float_value< attribute was found in result but should not be there.");
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Test get multiple sets with includes given attributes
     *
     * @return
     */
    public boolean testGetSetsWithExistingIncludes() {
        if (webTarget == null) {
            System.out.println("WebTarget is null!");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("includes", "name,int_value");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetsWithExistingIncludes---");
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
            for (int i = 0; i < recordsArr.size(); i++) {
                JsonObject curObj = recordsArr.getJsonObject(i);
                if (!curObj.containsKey("name")) {
                    System.out.println(">name< attribute not found in result.");
                    return false;
                }
                if (!curObj.containsKey("int_value")) {
                    System.out.println(">int_value< attribute not found in result.");
                    return false;
                }
                // Test for attr that should not be existend
                if (curObj.containsKey("float_value")) {
                    System.out.println(">float_value< attribute was found in result but should not be there.");
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Test get multiple sets with includes where the given attribute does not
     * exists
     *
     * @return
     */
    public boolean testGetSetsWithNotExistingInclude() {
        if (webTarget == null) {
            System.out.println("WebTarget is null!");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("includes", "notexattribute");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetsWithNotExistingInclude---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            if (!responseObj.containsKey("warnings")) {
                System.out.println(">warning< for not existing attribute is missing.");
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Test get multiple sets with default (descending) order
     *
     * @return
     */
    public boolean testGetSetsWithOrderDESC() {
        if (webTarget == null) {
            System.out.println("WebTarget is null!");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("order", "int_value");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetsWithOrderDESC---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            int maxval = 99999;
            for (int i = 0; i < recordsArr.size(); i++) {
                JsonObject curObj = recordsArr.getJsonObject(i);
                int intval = curObj.getInt("int_value");
                if (intval > maxval) {
                    System.out.println(">" + intval + "< is less than >" + maxval + "< expected to be bigger.");
                    return false;
                }
                maxval = intval;
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Test get multiple sets with ascending order
     *
     * @return
     */
    public boolean testGetSetsWithOrderASC() {
        if (webTarget == null) {
            System.out.println("WebTarget is null!");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("order", "int_value,asc");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetsWithOrderASC---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            int minval = -99999;
            for (int i = 0; i < recordsArr.size(); i++) {
                JsonObject curObj = recordsArr.getJsonObject(i);
                int intval = curObj.getInt("int_value");
                if (intval < minval) {
                    System.out.println(">" + intval + "< is less than >" + minval + "< expected to be bigger.");
                    return false;
                }
                minval = intval;
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Test get multiple sets with order where the given attribute does not
     * exists
     *
     * @return
     */
    public boolean testGetSetsWithNotExistingOrder() {
        if (webTarget == null) {
            System.out.println("WebTarget is null!");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("order", "notexattribute");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetsWithNotExistingOrder---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            if (!responseObj.containsKey("warnings")) {
                System.out.println(">warning< for not existing attribute is missing.");
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
                .path("testcol")
                .path("1")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", "neuer testwert");
        builder.add("float_value", 0.3333);
        builder.add("boolean_value", false);
        builder.add("int_value", 0);
        JsonObject dataObject = builder.build();
        Entity<String> coldef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).put(coldef);
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
                .path("testcol")
                .queryParam("storage", STORAGE);
        JsonArrayBuilder sets = Json.createArrayBuilder();
        JsonObjectBuilder set1 = Json.createObjectBuilder();
        set1.add("id", 1);
        set1.add("name", "neuer testwert id 1");
        set1.add("float_value", 0.3333);
        set1.add("boolean_value", false);
        set1.add("int_value", 0);
        sets.add(set1);
        JsonObjectBuilder set2 = Json.createObjectBuilder();
        set2.add("id", 2);
        set2.add("name", "neuer testwert id 2");
        set2.add("float_value", 0.3333);
        set2.add("boolean_value", false);
        set2.add("int_value", 0);
        sets.add(set2);
        JsonObjectBuilder set3 = Json.createObjectBuilder();
        set3.add("id", 3);
        set3.add("name", "neuer testwert id 3");
        set3.add("float_value", 0.3333);
        set3.add("int_value", 0);
        set3.add("boolean_value", false);
        sets.add(set3);
        JsonObjectBuilder env = Json.createObjectBuilder();
        env.add("records", sets);

        Entity<String> coldef = Entity.json(env.build().toString());

        Response response = target.request(MediaType.APPLICATION_JSON).put(coldef);
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
                .path("testcol")
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
                .path("testcol")
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

    /**
     * Tests to get a dataset useing an equals filter
     *
     * @return
     */
    public boolean testEQFilterString() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "name,eq,testwert2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testEQFilter---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there where " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset useing an equals filter with double value
     *
     * @return
     */
    public boolean testEQFilterFloat() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "float_value,eq,-11.1111");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testEQFilterFloat---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there where " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset useing an equals filter
     *
     * @return
     */
    public boolean testEQFilterBoolean() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "bool_value,eq,false");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testEQFilterBoolean---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there where " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset useing an equals filter
     *
     * @return
     */
    public boolean testEQFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "name,eq,notexistingvalue");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testEQFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (!recordsArr.isEmpty()) {
                System.out.println("Expected that there are 0 datasets, but there where " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset useing equals filter on non existend column
     *
     * @return
     */
    public boolean testEQFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,eq,testwert2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testEQFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not equals filter
     *
     * @return
     */
    public boolean testNEQFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "name,neq,testwert");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNEQFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != createdSets - 1) {
                System.out.println("Expected that there are " + (createdSets - 1)
                        + " dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not equals filter on non existend column
     *
     * @return
     */
    public boolean testNEQFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,neq,testwert2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNEQFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a contain filter
     *
     * @return
     */
    public boolean testCSFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "txt_value,cs,cs-filter");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCSFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a contain filter
     *
     * @return
     */
    public boolean testCSFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "name,cs,notexistingvalue");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCSFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (!recordsArr.isEmpty()) {
                System.out.println("Expected that there are 0 datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a contain filter on non existend column
     *
     * @return
     */
    public boolean testCSFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,cs,testwert2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSWFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not contain filter
     *
     * @return
     */
    public boolean testNCSFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "name,ncs,notexist");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNCSFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != createdSets) {
                System.out.println("Expected that there are " + createdSets + " dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not contain filter
     *
     * @return
     */
    public boolean testNCSFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "name,ncs,notexists");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNCSFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != createdSets) {
                System.out.println("Expected that there are " + createdSets + " datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not contain filter on non existend column
     *
     * @return
     */
    public boolean testNCSFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,ncs,testwert2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSWFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a starts with filter
     *
     * @return
     */
    public boolean testSWFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "txt_value,sw,sw-filter");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSWFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a starts with filter
     *
     * @return
     */
    public boolean testSWFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "name,sw,notexistingvalue");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSWFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (!recordsArr.isEmpty()) {
                System.out.println("Expected that there are 0 datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a starts with filter on non existend column
     *
     * @return
     */
    public boolean testSWFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,sw,testwert2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSWFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not starts with filter
     *
     * @return
     */
    public boolean testNSWFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "txt_value,nsw,sw-filter");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNSWFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != createdSets - 1) {
                System.out.println("Expected that there are " + (createdSets - 1)
                        + " dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not starts with filter
     *
     * @return
     */
    public boolean testNSWFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "txt_value,nsw,sw-filter");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNSWFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != createdSets - 1) {
                System.out.println("Expected that there are " + (createdSets - 1) + " datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not starts with filter on non existend
     * column
     *
     * @return
     */
    public boolean testNSWFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,nsw,testwert2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNSWFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a ends with filter
     *
     * @return
     */
    public boolean testEWFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "txt_value,ew,ew-filter");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testEWFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a ends with filter
     *
     * @return
     */
    public boolean testEWFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "name,ew,notexistingvalue");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testEWFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (!recordsArr.isEmpty()) {
                System.out.println("Expected that there are 0 datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a ends with filter on non existend column
     *
     * @return
     */
    public boolean testEWFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,ew,testwert2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testEWFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not ends with filter
     *
     * @return
     */
    public boolean testNEWFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "name,new,wert");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNEWFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != createdSets - 1) {
                System.out.println("Expected that there are " + (createdSets - 1)
                        + " dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not ends with filter on non existend
     * column
     *
     * @return
     */
    public boolean testNEWFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,new,testwert2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNEWFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a lower than filter
     *
     * @return
     */
    public boolean testLTFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "float_value,lt,13");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testLTFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 3) {
                System.out.println("Expected that there are 3 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a lower than filter with timestamp value
     *
     * @return
     */
    public boolean testLTFilterTimestamp() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "ts_value,lt,2012-12-30T10:15:30");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testLTFilterTimestamp---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 2) {
                System.out.println("Expected that there are 2 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using two filters with timestamp value. This is
     * the same functionallity as the between filter
     *
     * @return
     */
    public boolean testTwoFilterTimestamp() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "ts_value,le,2011-12-30T00:00:00")
                .queryParam("filter", "ts_value,gt,2011-12-30T23:59:59");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testTwoFilterTimestamp---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a lower than filter
     *
     * @return
     */
    public boolean testLTFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,lt,-20");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testLTFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (!recordsArr.isEmpty()) {
                System.out.println("Expected that there are 0 datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a lower than filter on non existend column
     *
     * @return
     */
    public boolean testLTFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,lt,2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testLTFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not lower than filter
     *
     * @return
     */
    public boolean testNLTFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "float_value,nlt,13");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNLTFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 3) {
                System.out.println("Expected that there are 3 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not lower than filter
     *
     * @return
     */
    public boolean testNLTFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,nlt,89");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNLTFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (!recordsArr.isEmpty()) {
                System.out.println("Expected that there are 0 datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not lower than filter on non existend
     * column
     *
     * @return
     */
    public boolean testNLTFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,nlt,2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNLTFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a lower or equal filter
     *
     * @return
     */
    public boolean testLEFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "float_value,le,12.2323");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testLEFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 3) {
                System.out.println("Expected that there are 3 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a lower or equal filter with timestamp value
     *
     * @return
     */
    public boolean testLEFilterTimestamp() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "ts_value,le,2011-12-30T10:15:30");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testLEFilterTimestamp---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a lower or equal filter
     *
     * @return
     */
    public boolean testLEFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,le,-20");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testLEFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (!recordsArr.isEmpty()) {
                System.out.println("Expected that there are 0 datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a lower or equal filter on non existend
     * column
     *
     * @return
     */
    public boolean testLEFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,le,2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testLEFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not lower or equal filter
     *
     * @return
     */
    public boolean testNLEFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "float_value,nle,13");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNLEFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 3) {
                System.out.println("Expected that there are 3 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not lower or equal filter
     *
     * @return
     */
    public boolean testNLEFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,nle,89");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNLEFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (!recordsArr.isEmpty()) {
                System.out.println("Expected that there are 0 datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not lower or equal filter on non existend
     * column
     *
     * @return
     */
    public boolean testNLEFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,nle,2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNLEFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a greater or equal filter
     *
     * @return
     */
    public boolean testGEFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "float_value,ge,42");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGEFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 3) {
                System.out.println("Expected that there are 3 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a greater or equal filter with timestamp
     * value
     *
     * @return
     */
    public boolean testGEFilterTimestamp() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "ts_value,ge,2013-12-30T10:15:30");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGEFilterTimestamp---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 3) {
                System.out.println("Expected that there are 3 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a greater or equal filter
     *
     * @return
     */
    public boolean testGEFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,ge,89");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGEFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (!recordsArr.isEmpty()) {
                System.out.println("Expected that there are 0 datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a greater or equal filter on non existend
     * column
     *
     * @return
     */
    public boolean testGEFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,ge,2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGEFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not greater or equal filter
     *
     * @return
     */
    public boolean testNGEFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "float_value,nge,12");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNGEFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not greater or equal filter
     *
     * @return
     */
    public boolean testNGEFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,nge,-42");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNGEFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (!recordsArr.isEmpty()) {
                System.out.println("Expected that there are 0 datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not greater or equal filter on non
     * existend column
     *
     * @return
     */
    public boolean testNGEFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,nge,2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNGEFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a greater than filter
     *
     * @return
     */
    public boolean testGTFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,gt,41");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGTFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 3) {
                System.out.println("Expected that there are 3 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a greater than filter with timestamp value
     *
     * @return
     */
    public boolean testGTFilterTimestamp() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "ts_value,gt,2013-12-30T10:15:30");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGTFilterTimestamp---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 2) {
                System.out.println("Expected that there are 2 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a greater than filter
     *
     * @return
     */
    public boolean testGTFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,gt,88");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGTFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (!recordsArr.isEmpty()) {
                System.out.println("Expected that there are 0 datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a greater than filter on non existend column
     *
     * @return
     */
    public boolean testGTFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,gt,2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGTFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not greater than filter
     *
     * @return
     */
    public boolean testNGTFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "float_value,ngt,12");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNGTFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not greater than filter
     *
     * @return
     */
    public boolean testNGTFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,ngt,-41");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNGTFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (!recordsArr.isEmpty()) {
                System.out.println("Expected that there are 0 datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not greater than filter on non existend
     * column
     *
     * @return
     */
    public boolean testNGTFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,ngt,2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNGTFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a between filter
     *
     * @return
     */
    public boolean testBTFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "float_value,bt,12.0,13.0");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testBTFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 2) {
                System.out.println("Expected that there are 2 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a between filter
     *
     * @return
     */
    public boolean testBTFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,bt,50,52");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testBTFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (!recordsArr.isEmpty()) {
                System.out.println("Expected that there are 0 datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a between filter on non existend column
     *
     * @return
     */
    public boolean testBTFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,bt,2,3");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testBTFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not between filter
     *
     * @return
     */
    public boolean testNBTFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "float_value,nbt,12.0,50.0");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNBTFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 3) {
                System.out.println("Expected that there are 3 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not between filter
     *
     * @return
     */
    public boolean testNBTFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,nbt,-11,88");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNBTFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (!recordsArr.isEmpty()) {
                System.out.println("Expected that there are 0 datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not between filter on non existend column
     *
     * @return
     */
    public boolean testNBTFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,nbt,2,2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNBTFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a between filter with timestamp values
     *
     * @return
     */
    public boolean testBTFilterTimestamp() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "ts_value,bt,2011-12-29T10:15:30,2013-12-30T10:15:30");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testBTFilterTimestamp---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 3) {
                System.out.println("Expected that there are 3 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a in filter
     *
     * @return
     */
    public boolean testINFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,in,125,13,42,-11");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testINFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 2) {
                System.out.println("Expected that there are 2 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a in filter
     *
     * @return
     */
    public boolean testINFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,in,50,52,-35");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testINFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (!recordsArr.isEmpty()) {
                System.out.println("Expected that there are 0 datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a in filter on non existend column
     *
     * @return
     */
    public boolean testINFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,in,2,3");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testINFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not in filter
     *
     * @return
     */
    public boolean testNINFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,nin,12,42,123,0");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNINFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != createdSets - 4) {
                System.out.println("Expected that there are " + (createdSets - 4)
                        + " dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not in filter
     *
     * @return
     */
    public boolean testNINFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,nin,42,12,-11,0,88");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNINFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != createdSets - 6) {
                System.out.println("Expected that there are " + (createdSets - 6) + " datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a not in filter on non existend column
     *
     * @return
     */
    public boolean testNINFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,nin,2,2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNINFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a in filter with timestamp values
     *
     * @return
     */
    public boolean testINFilterTimestamp() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "ts_value,in,2011-12-29T10:15:30,2013-12-30T10:15:30,2018-12-29T10:15:30");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testINFilterTimestamp---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a in filter with string values
     *
     * @return
     */
    public boolean testINFilterString() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "txt_value,in,Datensatz für cs-filter in-filter1,sw-filter Datensatz in-filter2");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testINFilterString---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 2) {
                System.out.println("Expected that there are 2 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a in filter with double values
     *
     * @return
     */
    public boolean testINFilterDouble() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "float_value,in,-11.1111,42.0,15.35,-68.15");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testINFilterString---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 2) {
                System.out.println("Expected that there are 2 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a is null filter
     *
     * @return
     */
    public boolean testISFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "float_value,is");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testISFilterString---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a is null filter
     *
     * @return
     */
    public boolean testISFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,is");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testISFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (!recordsArr.isEmpty()) {
                System.out.println("Expected that there are 0 datasets, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a is null filter on non existend column
     *
     * @return
     */
    public boolean testISFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,is");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testISFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a is not null filter
     *
     * @return
     */
    public boolean testNISFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "float_value,nis");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNISFilterString---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != createdSets - 1) {
                System.out.println("Expected that there are " + (createdSets - 1) + " dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a is not null filter on non existend column
     *
     * @return
     */
    public boolean testNISFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,nis");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNISFilterMissingColumn---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests get one set and post it to second storage
     *
     * @return
     */
    public boolean testGetSetAndCreateOnSecond() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .path("1")
                .queryParam("storage", STORAGE);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetAndCreateOnSecond---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            WebTarget target2 = webTarget
                    .path("testcol")
                    .queryParam("storage", STORAGE + "2");

            Entity<String> dataset = Entity.json(responseText);

            Response response2 = target2.request(MediaType.APPLICATION_JSON).post(dataset);
            String responseText2 = response2.readEntity(String.class);
            if (PRINT_DEBUG_MESSAGES) {
                System.out.println(response2.getStatusInfo());
                System.out.println(responseText2);
            }
            if (Response.Status.OK.getStatusCode() == response2.getStatus()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Tests get one set and post it to second storage
     *
     * @return
     */
    public boolean testGetSetsAndCreateOnSecond() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetSetsAndCreateOnSecond---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            WebTarget target2 = webTarget
                    .path("testcol")
                    .queryParam("storage", STORAGE + "2");

            Entity<String> dataset = Entity.json(responseText);

            Response response2 = target2.request(MediaType.APPLICATION_JSON).post(dataset);
            String responseText2 = response2.readEntity(String.class);
            if (PRINT_DEBUG_MESSAGES) {
                System.out.println(response2.getStatusInfo());
                System.out.println(responseText2);
            }
            if (Response.Status.OK.getStatusCode() == response2.getStatus()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Test createing multiple geometry sets at once
     *
     * @return
     */
    public boolean testCreateGeoSetsSimple() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("testgeocol")
                .queryParam("storage", STORAGE);

        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder set1 = Json.createObjectBuilder();
        set1.add("point2d", "SRID=25832;POINT(471703.330843 5755213.129580)");
        set1.add("point3d", "SRID=5555;POINT(471374.757001877 5755041.13599968 0.0)");
        set1.add("multipolygon2d", "SRID=25832;MULTIPOLYGON(((471707.870000839 5755194.99500084,471706.445001602 5755198.41200066,471704.905000687 5755202.104002,471703.673002243 5755201.59700203,471700.82500267 5755208.36800194,471699.766000748 5755207.9430027,471696.754999161 5755215.1930027,471695.489999771 5755214.63200188,471693.898000717 5755218.05800056,471703.342000961 5755224.17000008,471710.625001907 5755212.91600227,471715.520002365 5755216.09300041,471717.177000046 5755213.54000282,471712.267999649 5755210.35199928,471715.406000137 5755205.5170002,471716.899999619 5755203.21700096,471720.069002151 5755205.27499962,471724.329999924 5755198.71199989,471721.304002762 5755195.48800087,471719.160001755 5755197.31300163,471710.877000809 5755191.97599983,471709.135000229 5755195.51400185,471707.870000839 5755194.99500084)))");
        set1.add("multipolygon3d", "SRID=5555;MULTIPOLYGON(((471361.680002213 5755037.32800102 0.0, 471368.246999741 5755041.79300117 0.0, 471374.757001877 5755041.13599968 0.0, 471361.680002213 5755037.32800102 0.0)))");
        set1.add("non_geo", "Elbeallee 76");
        jab.add(set1);
        JsonObjectBuilder set2 = Json.createObjectBuilder();
        set2.add("point2d", "SRID=25832;POINT(472023.584332 5755448.168211)");
        set2.add("point3d", "SRID=5555;POINT(472017.971 5755465.619 169.17)");
        set2.add("multipolygon2d", "SRID=25832;MULTIPOLYGON(((472024.026 5755455.491,472022.681 5755454.573,472027.693 5755447.905,472028.884 5755447.01,472029.878 5755448.176,472035.294 5755444.102,472042.444 5755441.325,472039.037 5755430.499,472038.647 5755430.62,472030.801 5755433.056,472031.472 5755434.537,472023.959 5755437.924,472024.74 5755439.125,472018.297 5755443.299,472019.341 5755444.388,472013.515 5755449.943,472014.716 5755450.943,472009.385 5755457.331,472011.948 5755458.633,472011.558 5755459.397,472009.756 5755462.933,472017.585 5755466.474,472017.971 5755465.619,472020.318 5755460.42,472024.026 5755455.491)))");
        set2.add("multipolygon3d", "SRID=5555;MULTIPOLYGON(((472020.318 5755460.42 169.17,472017.971 5755465.619 169.17,472017.585 5755466.474 169.17,472009.756 5755462.933 169.17,472011.558 5755459.397 169.17,472011.948 5755458.633 169.17,472009.385 5755457.331 169.17,472014.716 5755450.943 169.17,472013.515 5755449.943 169.17,472019.341 5755444.388 169.17,472018.297 5755443.299 169.17,472024.74 5755439.125 169.17,472023.959 5755437.924 169.17,472031.472 5755434.537 169.17,472030.801 5755433.056 169.17,472038.647 5755430.62 169.17,472039.037 5755430.499 169.17,472042.444 5755441.325 169.17,472035.294 5755444.102 169.17,472029.878 5755448.176 169.17,472028.884 5755447.01 169.17,472027.693 5755447.905 169.17,472022.681 5755454.573 169.17,472024.026 5755455.491 169.17,472020.318 5755460.42 169.17)))");
        set2.add("non_geo", "Elbeallee 136-148");
        jab.add(set2);
        JsonObjectBuilder set3 = Json.createObjectBuilder();
        set3.add("point2d", "SRID=25832;POINT(471805.591493 5756379.729509)");
        set3.add("point3d", "SRID=5555;POINT(471802.553 5756386.124 173.5)");
        set3.add("multipolygon2d", "SRID=25832;MULTIPOLYGON(((471804.796 5756387.331,471810.576 5756384.561,471809.684 5756382.703,471810.975 5756382.084,471816.292 5756380.342,471821.961 5756379.797,471821.596 5756368.72,471818.571 5756370.037,471814.517 5756369.315,471811.926 5756370.78,471807.187 5756371.203,471805.32 5756373.097,471801.578 5756374.056,471799.658 5756376.306,471795.534 5756377.527,471794.316 5756379.705,471790.496 5756381.757,471789.345 5756384.437,471785.919 5756387.095,471794.769 5756394.049,471798.595 5756389.188,471802.553 5756386.124,471803.903 5756385.474,471804.796 5756387.331)))");
        set3.add("multipolygon3d", "SRID=5555;MULTIPOLYGON(((471803.903 5756385.474 173.5,471802.553 5756386.124 173.5,471798.595 5756389.188 173.5,471794.769 5756394.049 173.5,471785.919 5756387.095 173.5,471789.345 5756384.437 173.5,471790.496 5756381.757 173.5,471794.316 5756379.705 173.5,471795.534 5756377.527 173.5,471799.658 5756376.306 173.5,471801.578 5756374.056 173.5,471805.32 5756373.097 173.5,471807.187 5756371.203 173.5,471811.926 5756370.78 173.5,471814.517 5756369.315 173.5,471818.571 5756370.037 173.5,471821.596 5756368.72 173.5,471821.961 5756379.797 173.5,471816.292 5756380.342 173.5,471810.975 5756382.084 173.5,471809.684 5756382.703 173.5,471810.576 5756384.561 173.5,471804.796 5756387.331 173.5,471803.903 5756385.474 173.5)))");
        set3.add("non_geo", "Rheinallee 117");
        jab.add(set3);
        JsonObjectBuilder set4 = Json.createObjectBuilder();
        set4.add("point2d", "SRID=25832;POINT(955521.916581662 6790232.01843556)");
        set4.add("point3d", "SRID=5555;POINT(471369.493384076 5755012.46542657 118.0)");
        set4.add("multipolygon2d", "SRID=25832;MULTIPOLYGON(((471361.680002213 5755037.32800102,471368.246999741 5755041.79300117,471370.585000992 5755038.36200142,471374.757001877 5755041.13599968,471379.572999954 5755034.32299995,471384.780000687 5755028.94799995,471390.295999527 5755023.88800239,471387.524999619 5755019.89999962,471390.476999283 5755017.84800148,471385.87100029 5755011.22100258,471379.110002518 5755016.37400246,471379.288000107 5755016.57500267,471376.516000748 5755019.04200172,471370.196001053 5755011.93900108,471366.599000931 5755014.63400078,471366.341001511 5755014.91399956,471364.382001877 5755017.04200172,471363.513002396 5755017.98800087,471361.12899971 5755021.38200188,471368.951002121 5755027.72300148,471366.540002823 5755030.17200279,471361.680002213 5755037.32800102)))");
        set4.add("multipolygon3d", "SRID=5555;MULTIPOLYGON(((471369.5 5755012.49 118.0,471369.493384076 5755012.46542657 118.0,471369.471282536 5755012.48198582 118.0,471369.431870814 5755012.51151448 118.0,471369.5 5755012.49 118.0)),((471368.89 5755013.02 118.0,471368.872915032 5755012.93030392 118.0,471368.791626889 5755012.99120787 118.0,471368.739005883 5755013.03063339 118.0,471368.575903193 5755013.15283569 118.0,471368.59 5755013.19 118.0,471368.89 5755013.02 118.0)),((471371.46 5755013.95 118.0,471371.72 5755013.68 118.0,471371.17 5755013.48 118.0,471371.46 5755013.95 118.0)),((471367.65 5755013.93 118.0,471367.631884924 5755013.86012756 118.0,471367.572919541 5755013.90430651 118.0,471367.540454223 5755013.92863068 118.0,471367.411064023 5755014.02557439 118.0,471367.65 5755013.93 118.0)),((471372.422602695 5755014.4414621 118.0,471372.02 5755014.31 118.0,471372.27 5755014.74 118.0,471372.479012621 5755014.5048608 118.0,471372.422602695 5755014.4414621 118.0)),((471382.78 5755015 118.0,471383.11 5755014.84 118.0,471382.9 5755014.77 118.0,471382.78 5755015 118.0)),((471383.27 5755014.26 118.0,471383.06 5755014.2 118.0,471383.005 5755014.35 118.0,471382.95 5755014.5 118.0,471383.27 5755014.26 118.0)),((471368.34 5755013.49 118.0,471368.32 5755013.38 118.0,471368.218542388 5755013.42058305 118.0,471368.060950445 5755013.5386565 118.0,471368.165 5755013.56 118.0,471368.36 5755013.6 118.0,471368.34 5755013.49 118.0)),((471366.16 5755015.22 118.0,471366.128485283 5755015.14484952 118.0,471366.080273154 5755015.1972209 118.0,471366.010839092 5755015.27264503 118.0,471366.16 5755015.22 118.0)),((471366.19 5755015.24 118.0,471365.959898881 5755015.32797984 118.0,471365.906385257 5755015.38611007 118.0,471365.871301087 5755015.42422095 118.0,471365.905 5755015.51 118.0,471365.96 5755015.65 118.0,471366.27 5755015.49 118.0,471366.19 5755015.24 118.0)),((471365.572145649 5755015.74918447 118.0,471365.61 5755015.76 118.0,471365.595699474 5755015.72359866 118.0,471365.572145649 5755015.74918447 118.0)),((471374.01 5755016.62 118.0,471374.184985179 5755016.42219067 118.0,471374.073280474 5755016.29664658 118.0,471373.74 5755016.17 118.0,471374.01 5755016.62 118.0)),((471365.65 5755015.8 118.0,471365.482544595 5755015.84651539 118.0,471365.402106646 5755015.93389271 118.0,471365.317059359 5755016.02627701 118.0,471365.32 5755016.04 118.0,471365.335 5755016.11 118.0,471365.35 5755016.18 118.0,471365.72 5755016.03 118.0,471365.65 5755015.8 118.0)),((471373.22 5755015.84 118.0,471373.429607293 5755015.57322708 118.0,471373.407692688 5755015.54859742 118.0,471372.93 5755015.38 118.0,471373.22 5755015.84 118.0)),((471385.14 5755017.005 118.0,471384.97 5755016.86 118.0,471385.12 5755017.17 118.0,471385.14 5755017.005 118.0)),((471381.94 5755017.24 118.0,471381.45 5755017.09 118.0,471381.465 5755017.295 118.0,471381.48 5755017.5 118.0,471381.94 5755017.24 118.0)),((471364.14 5755017.36 118.0,471364.210983409 5755017.22817367 118.0,471364.161544827 5755017.28199287 118.0,471364.122766465 5755017.32420727 118.0,471364.100305304 5755017.34865866 118.0,471364.064751081 5755017.38736324 118.0,471364.14 5755017.36 118.0)),((471378.78 5755017.345 118.0,471378.79 5755017.29 118.0,471378.6 5755017.36 118.0,471378.77 5755017.4 118.0,471378.78 5755017.345 118.0)),((471364.18 5755017.42 118.0,471363.983090112 5755017.47625997 118.0,471363.922243212 5755017.54249834 118.0,471363.867908016 5755017.60164803 118.0,471363.895 5755017.66 118.0,471363.96 5755017.8 118.0,471364.24 5755017.62 118.0,471364.18 5755017.42 118.0)),((471374.95 5755017.73 118.0,471375.148214197 5755017.5047566 118.0,471375.103108286 5755017.4540624 118.0,471374.68 5755017.29 118.0,471374.95 5755017.73 118.0)),((471363.2 5755019.02 118.0,471362.84963467 5755018.93240867 118.0,471362.817120257 5755018.97869802 118.0,471362.835 5755019.04 118.0,471362.87 5755019.16 118.0,471363.2 5755019.02 118.0)),((471378.4 5755019.75 118.0,471377.99 5755019.66 118.0,471378.25 5755020.09 118.0,471378.69 5755020.21 118.0,471378.89 5755019.92 118.0,471378.4 5755019.75 118.0)),((471385.52 5755020.87 118.0,471385.41 5755021.31 118.0,471385.89 5755021.45 118.0,471385.52 5755020.87 118.0)),((471387.39 5755020.41 118.0,471387.23 5755020.78 118.0,471387.69 5755020.89 118.0,471387.88 5755020.53 118.0,471387.39 5755020.41 118.0)),((471389.74 5755023.37 118.0,471389.972310351 5755023.4221513 118.0,471389.815918831 5755023.19707377 118.0,471389.74 5755023.37 118.0)),((471379.88 5755023.95 118.0,471379.74 5755024.28 118.0,471379.86 5755024.28 118.0,471379.88 5755023.95 118.0)),((471388.91316412 5755021.8978358 118.0,471388.47 5755021.77 118.0,471388.28 5755022.14 118.0,471388.77 5755022.27 118.0,471388.960820307 5755021.96642224 118.0,471388.91316412 5755021.8978358 118.0)),((471384.115 5755020.185 118.0,471384.11 5755019.96 118.0,471383.89 5755020.01 118.0,471384.12 5755020.41 118.0,471384.115 5755020.185 118.0)),((471386.58 5755020.345 118.0,471386.39 5755020.05 118.0,471386.26 5755020.48 118.0,471386.77 5755020.64 118.0,471386.58 5755020.345 118.0)),((471384.77 5755020.85 118.0,471384.71 5755020.08 118.0,471384.485 5755020.395 118.0,471384.26 5755020.71 118.0,471384.77 5755020.85 118.0)),((471382.08 5755018.565 118.0,471381.92 5755018.43 118.0,471382.08 5755018.76 118.0,471382.08 5755018.565 118.0)),((471377.02 5755018.685 118.0,471377.033344404 5755018.58158087 118.0,471377.004439529 5755018.60730537 118.0,471376.911972376 5755018.68959846 118.0,471376.864646449 5755018.73171716 118.0,471376.925 5755018.78 118.0,471377 5755018.84 118.0,471377.02 5755018.685 118.0)),((471382.5 5755018.575 118.0,471382.41 5755018.6 118.0,471382.56 5755018.92 118.0,471382.5 5755018.575 118.0)),((471382.23 5755018.88 118.0,471382.08 5755018.76 118.0,471382.22 5755019.06 118.0,471382.23 5755018.88 118.0)),((471363.578286456 5755017.91693217 118.0,471363.59 5755017.92 118.0,471363.58575318 5755017.90880384 118.0,471363.578286456 5755017.91693217 118.0)),((471385.41 5755017.41 118.0,471385.06 5755017.54 118.0,471385.245 5755017.675 118.0,471385.43 5755017.81 118.0,471385.62 5755018.05 118.0,471385.715 5755018.17 118.0,471385.56 5755017.71 118.0,471385.41 5755017.41 118.0)),((471382.04 5755017.89 118.0,471382.08 5755017.53 118.0,471381.855 5755017.665 118.0,471381.63 5755017.8 118.0,471382.04 5755017.89 118.0)),((471364.32 5755017.73 118.0,471363.98 5755017.82 118.0,471363.65 5755017.97 118.0,471363.501589782 5755018.00424851 118.0,471363.47047813 5755018.04854082 118.0,471363.427638605 5755018.10952959 118.0,471363.48 5755018.125 118.0,471363.7 5755018.19 118.0,471364.1 5755018.12 118.0,471364.46 5755018.03 118.0,471364.32 5755017.73 118.0)),((471382.235 5755018.05 118.0,471382.22 5755017.83 118.0,471382.13 5755017.86 118.0,471382.04 5755017.89 118.0,471382.25 5755018.27 118.0,471382.44 5755018.23 118.0,471382.235 5755018.05 118.0)),((471375.74 5755018.57 118.0,471375.895460935 5755018.34458164 118.0,471375.784269888 5755018.21961486 118.0,471375.45 5755018.09 118.0,471375.74 5755018.57 118.0)),((471365.93 5755024.89 118.0,471365.597114255 5755025.00413225 118.0,471365.750899765 5755025.12880031 118.0,471365.802900983 5755025.17095572 118.0,471365.93 5755024.89 118.0)),((471368.22 5755026.69 118.0,471368.13 5755026.76 118.0,471368.59 5755026.92 118.0,471368.22 5755026.69 118.0)),((471381.72 5755026.245 118.0,471381.5 5755026.15 118.0,471381.51625 5755026.22375 118.0,471381.524375 5755026.260625 118.0,471381.5325 5755026.2975 118.0,471381.54875 5755026.37125 118.0,471381.565 5755026.445 118.0,471381.58125 5755026.51875 118.0,471381.5975 5755026.5925 118.0,471381.63 5755026.74 118.0,471381.7075 5755026.64 118.0,471381.785 5755026.54 118.0,471381.82375 5755026.49 118.0,471381.8625 5755026.44 118.0,471381.94 5755026.34 118.0,471381.72 5755026.245 118.0)),((471382.19 5755026.69 118.0,471382.04 5755026.96 118.0,471382.11 5755027 118.0,471382.19 5755026.69 118.0)),((471382.19 5755026.69 118.0,471382.205 5755026.915 118.0,471382.2125 5755027.0275 118.0,471382.22 5755027.14 118.0,471382.43 5755027.1 118.0,471382.64 5755027.06 118.0,471382.19 5755026.69 118.0)),((471375.07 5755026.96 118.0,471375.59 5755027.18 118.0,471375.4 5755026.89 118.0,471375.07 5755026.96 118.0)),((471382.16 5755027.14 118.0,471382.07 5755027.06 118.0,471382.065 5755027.2 118.0,471382.0625 5755027.27 118.0,471382.06 5755027.34 118.0,471382.16 5755027.14 118.0)),((471368.81 5755027.95 118.0,471368.780011547 5755027.8966872 118.0,471368.737095746 5755027.94027943 118.0,471368.606338298 5755028.07309787 118.0,471368.6 5755028.09 118.0,471368.81 5755027.95 118.0)),((471368.485 5755028.735 118.0,471368.46 5755028.97 118.0,471368.65 5755028.51 118.0,471368.485 5755028.735 118.0)),((471384.122251197 5755029.62697115 118.0,471384.1 5755029.6 118.0,471384.114930877 5755029.63452765 118.0,471384.122251197 5755029.62697115 118.0)),((471376.56 5755028.8 118.0,471376.39 5755028.67 118.0,471376.55 5755029 118.0,471376.56 5755028.8 118.0)),((471383.1625 5755028.135 118.0,471383.09 5755028.01 118.0,471383.04 5755028.1425 118.0,471383.015 5755028.20875 118.0,471382.99 5755028.275 118.0,471382.965 5755028.34125 118.0,471382.94 5755028.4075 118.0,471382.915 5755028.47375 118.0,471382.9025 5755028.506875 118.0,471382.89 5755028.54 118.0,471383.38 5755028.51 118.0,471383.361875 5755028.47875 118.0,471383.34375 5755028.4475 118.0,471383.3075 5755028.385 118.0,471383.27125 5755028.3225 118.0,471383.235 5755028.26 118.0,471383.19875 5755028.1975 118.0,471383.1625 5755028.135 118.0)),((471373.95 5755028.17 118.0,471374.48 5755028.36 118.0,471374.285 5755028.06 118.0,471373.95 5755028.17 118.0)),((471376.98 5755028.305 118.0,471376.82 5755028.19 118.0,471376.72 5755028.49 118.0,471377.14 5755028.42 118.0,471376.98 5755028.305 118.0)),((471376.4 5755028.47 118.0,471376.41 5755028.27 118.0,471376.15 5755028.445 118.0,471375.89 5755028.62 118.0,471376.39 5755028.67 118.0,471376.4 5755028.47 118.0)),((471374.68 5755027.19 118.0,471374.54 5755027.56 118.0,471374.77 5755027.15 118.0,471374.68 5755027.19 118.0)),((471382.73 5755027.495 118.0,471382.79 5755027.37 118.0,471382.69 5755027.455 118.0,471382.59 5755027.54 118.0,471382.67 5755027.62 118.0,471382.73 5755027.495 118.0)),((471380.83625 5755027.50875 118.0,471380.8175 5755027.4225 118.0,471380.50625 5755027.45625 118.0,471380.39 5755027.41 118.0,471380.66 5755027.675 118.0,471380.93 5755027.94 118.0,471380.920625 5755027.896875 118.0,471380.91125 5755027.85375 118.0,471380.901875 5755027.810625 118.0,471380.8925 5755027.7675 118.0,471380.883125 5755027.724375 118.0,471380.87375 5755027.68125 118.0,471380.855 5755027.595 118.0,471380.845625 5755027.551875 118.0,471380.83625 5755027.50875 118.0)),((471382.64 5755027.06 118.0,471382.29 5755027.2 118.0,471382.365 5755027.285 118.0,471382.44 5755027.37 118.0,471382.515 5755027.455 118.0,471382.59 5755027.54 118.0,471382.615 5755027.3 118.0,471382.6275 5755027.18 118.0,471382.64 5755027.06 118.0)),((471369.03 5755027.47 118.0,471368.91 5755027.39 118.0,471368.9 5755027.56 118.0,471369.1 5755027.625 118.0,471369.03 5755027.47 118.0)),((471366.25 5755024.31 118.0,471365.83 5755024.4 118.0,471365.44 5755024.71 118.0,471365.91 5755024.71 118.0,471366.08 5755024.51 118.0,471366.25 5755024.31 118.0)),((471379.305 5755024.72 118.0,471378.8 5755024.44 118.0,471379.29 5755024.85 118.0,471379.305 5755024.72 118.0)),((471377.35 5755024.61 118.0,471377.85 5755024.79 118.0,471377.665 5755024.515 118.0,471377.35 5755024.61 118.0)),((471381.4 5755025.05 118.0,471380.73 5755024.83 118.0,471380.68 5755024.97 118.0,471380.48 5755024.79 118.0,471380.63 5755025.11 118.0,471381.4 5755025.05 118.0)),((471374.61 5755029.77 118.0,471374.27 5755029.93 118.0,471374.54 5755030.05 118.0,471374.61 5755029.77 118.0)),((471375.23 5755030.06 118.0,471374.61 5755029.77 118.0,471374.605 5755029.905 118.0,471374.54 5755030.05 118.0,471374.6 5755030.04 118.0,471375.23 5755030.06 118.0)),((471383.097476283 5755030.68480952 118.0,471383.03 5755030.69 118.0,471383.049639755 5755030.73418945 118.0,471383.058788851 5755030.72474516 118.0,471383.097476283 5755030.68480952 118.0)),((471372.33 5755030.26 118.0,471371.8 5755030.16 118.0,471371.75 5755030.25 118.0,471371.7 5755030.34 118.0,471372.11 5755030.39 118.0,471372.33 5755030.26 118.0)),((471382.95 5755030.08 118.0,471382.6 5755030.35 118.0,471382.74 5755030.325 118.0,471382.88 5755030.3 118.0,471383.03 5755030.355 118.0,471383.18 5755030.41 118.0,471382.95 5755030.08 118.0)),((471383.472511271 5755030.29767436 118.0,471383.37 5755030.28 118.0,471383.29 5755030.36 118.0,471383.422544618 5755030.34925314 118.0,471383.450029252 5755030.32088174 118.0,471383.472511271 5755030.29767436 118.0)),((471375.225 5755030.34 118.0,471375.23 5755030.06 118.0,471374.89 5755030.55 118.0,471375.22 5755030.62 118.0,471375.225 5755030.34 118.0)),((471378.04125 5755030.465 118.0,471378.0275 5755030.4 118.0,471377.8475 5755030.465 118.0,471377.7575 5755030.4975 118.0,471377.81625 5755030.54625 118.0,471377.875 5755030.595 118.0,471377.9925 5755030.6925 118.0,471378.11 5755030.79 118.0,471378.0825 5755030.66 118.0,471378.06875 5755030.595 118.0,471378.055 5755030.53 118.0,471378.04125 5755030.465 118.0)),((471382.84 5755030.67 118.0,471382.6 5755030.35 118.0,471382.3 5755030.67 118.0,471382.43 5755030.635 118.0,471382.56 5755030.6 118.0,471382.82 5755030.96 118.0,471383.03 5755030.69 118.0,471382.84 5755030.67 118.0)),((471380.97 5755032.77 118.0,471380.67 5755032.83 118.0,471380.81 5755032.86 118.0,471380.95 5755032.89 118.0,471380.962702041 5755032.88846036 118.0,471380.970121357 5755032.88080166 118.0,471381.055967974 5755032.79218528 118.0,471380.97 5755032.77 118.0)),((471381.11 5755032.35 118.0,471380.76 5755032.27 118.0,471380.925 5755032.34 118.0,471381.09 5755032.41 118.0,471381.11 5755032.35 118.0)),((471380.76 5755032.27 118.0,471380.43 5755032.5 118.0,471380.565 5755032.5 118.0,471380.7 5755032.5 118.0,471380.99 5755032.59 118.0,471380.76 5755032.27 118.0)),((471381.29 5755032.525 118.0,471381.09 5755032.41 118.0,471381.193658808 5755032.65005198 118.0,471381.289186391 5755032.55144228 118.0,471381.305929193 5755032.53415929 118.0,471381.29 5755032.525 118.0)),((471367.64 5755032.1 118.0,471367.55 5755031.69 118.0,471367.545 5755031.91 118.0,471367.54 5755032.13 118.0,471367.64 5755032.1 118.0)),((471379.81 5755031.6 118.0,471379.31 5755031.67 118.0,471379.3375 5755031.705 118.0,471379.365 5755031.74 118.0,471379.42 5755031.81 118.0,471379.72 5755031.86 118.0,471380.02 5755031.91 118.0,471379.81 5755031.6 118.0)),((471378.05 5755031.51 118.0,471377.49 5755031.5 118.0,471377.51375 5755031.56 118.0,471377.5375 5755031.62 118.0,471377.585 5755031.74 118.0,471377.6325 5755031.86 118.0,471377.68 5755031.98 118.0,471378.065 5755032.03 118.0,471378.45 5755032.08 118.0,471378.25 5755031.79 118.0,471378.05 5755031.51 118.0)),((471381.91 5755031.78 118.0,471381.86 5755031.66 118.0,471381.51 5755031.96 118.0,471381.735 5755031.93 118.0,471381.914374089 5755031.90608346 118.0,471381.948050297 5755031.87132071 118.0,471381.91 5755031.78 118.0)),((471381.51 5755031.96 118.0,471380.98 5755031.93 118.0,471381.23 5755032.075 118.0,471381.48 5755032.22 118.0,471381.572399126 5755032.25909194 118.0,471381.597609894 5755032.23306777 118.0,471381.635392548 5755032.19406609 118.0,471381.697242576 5755032.13022052 118.0,471381.51 5755031.96 118.0)),((471379.22 5755032.24 118.0,471379.42 5755031.81 118.0,471379.3025 5755031.855 118.0,471379.185 5755031.9 118.0,471379.0675 5755031.945 118.0,471378.95 5755031.99 118.0,471378.9475 5755032.155 118.0,471378.67 5755032.4 118.0,471378.71 5755032.565 118.0,471378.75 5755032.73 118.0,471379.22 5755032.24 118.0)),((471376.43 5755032.03 118.0,471376.3 5755032.01 118.0,471376.305 5755032.14 118.0,471376.3075 5755032.205 118.0,471376.31 5755032.27 118.0,471376.42 5755032.46 118.0,471376.82 5755032.09 118.0,471376.56 5755032.05 118.0,471376.43 5755032.03 118.0)),((471378.67 5755032.4 118.0,471378.45 5755032.08 118.0,471378.27 5755032.185 118.0,471378.09 5755032.29 118.0,471377.91 5755032.395 118.0,471377.73 5755032.5 118.0,471378.2 5755032.45 118.0,471378.67 5755032.4 118.0)),((471378.08 5755034.07 118.0,471378.59 5755034.26 118.0,471378.3 5755033.78 118.0,471378.08 5755034.07 118.0)),((471379.815 5755033.4575 118.0,471379.74 5755033.43 118.0,471379.86 5755033.605 118.0,471379.98 5755033.78 118.0,471379.6 5755033.92 118.0,471379.871684994 5755034.0146781 118.0,471379.949794398 5755033.93404857 118.0,471380.032202401 5755033.84898174 118.0,471380.068579165 5755033.81143132 118.0,471380.097120673 5755033.78196894 118.0,471380.112732467 5755033.76585345 118.0,471380.163725282 5755033.71321539 118.0,471380.04 5755033.54 118.0,471379.89 5755033.485 118.0,471379.815 5755033.4575 118.0)),((471377.56 5755033.9 118.0,471378.08 5755034.07 118.0,471377.69 5755033.47 118.0,471377.56 5755033.9 118.0)),((471379.445 5755033.605 118.0,471379.29 5755033.29 118.0,471379.2875 5755033.4575 118.0,471379.285 5755033.625 118.0,471379.28 5755033.96 118.0,471379.6 5755033.92 118.0,471379.5225 5755033.7625 118.0,471379.445 5755033.605 118.0)),((471373.1 5755035.33 118.0,471372.56 5755035.14 118.0,471372.33 5755035.36 118.0,471372.38 5755035.545 118.0,471372.43 5755035.73 118.0,471372.765 5755035.53 118.0,471373.1 5755035.33 118.0)),((471371.555 5755034.8725 118.0,471370.85 5755034.57 118.0,471371.1 5755034.94 118.0,471371.35 5755035.31 118.0,471371.73 5755035.16 118.0,471371.775 5755035.02 118.0,471371.555 5755034.8725 118.0)),((471372.045 5755035.21 118.0,471371.73 5755035.16 118.0,471372.28 5755035.39 118.0,471372.025 5755035.2425 118.0,471372.045 5755035.21 118.0)),((471367.36 5755040.89 118.0,471367.052509632 5755040.98084943 118.0,471367.169070447 5755041.06010087 118.0,471367.284122468 5755041.13832647 118.0,471367.36 5755040.89 118.0)),((471372.255 5755039.2925 118.0,471372.29 5755039.19 118.0,471372.2625 5755039.23625 118.0,471372.24875 5755039.259375 118.0,471372.235 5755039.2825 118.0,471372.2075 5755039.32875 118.0,471372.18 5755039.375 118.0,471372.15974643 5755039.40906282 118.0,471372.168637249 5755039.4149744 118.0,471372.182098449 5755039.42392487 118.0,471372.189332161 5755039.42873462 118.0,471372.20493774 5755039.4391109 118.0,471372.22 5755039.395 118.0,471372.255 5755039.2925 118.0)),((471372.29 5755039.19 118.0,471371.981003426 5755039.2902151 118.0,471372.06219977 5755039.34420323 118.0,471372.096845327 5755039.36723935 118.0,471372.120913075 5755039.3832422 118.0,471372.15 5755039.35 118.0,471372.29 5755039.19 118.0)),((471376.72 5755032.73 118.0,471376.7 5755032.64 118.0,471376.17 5755032.78 118.0,471376.72 5755032.8 118.0,471376.93 5755033.1 118.0,471377.4 5755033.58 118.0,471377.63 5755033.25 118.0,471377.21 5755033.285 118.0,471377.02 5755032.99 118.0,471376.72 5755032.73 118.0)),((471380.14 5755032.86 118.0,471379.91 5755032.52 118.0,471379.57 5755032.88 118.0,471379.6125 5755033.0175 118.0,471379.29 5755033.29 118.0,471379.515 5755033.36 118.0,471379.6275 5755033.395 118.0,471379.74 5755033.43 118.0,471379.96 5755033.06 118.0,471380.33 5755033.44 118.0,471380.470187583 5755033.39686536 118.0,471380.5512768 5755033.31315987 118.0,471380.59609349 5755033.26689721 118.0,471380.621340147 5755033.24083599 118.0,471380.65336147 5755033.20778153 118.0,471380.37 5755033.17 118.0,471380.14 5755032.86 118.0)),((471379.29 5755033.29 118.0,471379.07 5755032.98 118.0,471378.9525 5755033.035 118.0,471378.835 5755033.09 118.0,471378.6 5755033.2 118.0,471378.945 5755033.245 118.0,471379.1175 5755033.2675 118.0,471379.29 5755033.29 118.0)),((471379.9 5755031.03 118.0,471379.4 5755031.01 118.0,471379.09 5755031.35 118.0,471379.23 5755031.3475 118.0,471379.37 5755031.345 118.0,471379.65 5755031.34 118.0,471379.875 5755031.335 118.0,471380.1 5755031.33 118.0,471379.9 5755031.03 118.0)),((471382.3 5755030.67 118.0,471381.76 5755030.68 118.0,471382 5755030.79 118.0,471382.24 5755030.9 118.0,471382.52 5755031.05 118.0,471382.3 5755030.67 118.0)),((471381.99 5755031.01 118.0,471381.63 5755031.33 118.0,471381.785 5755031.29 118.0,471381.94 5755031.25 118.0,471382.22 5755031.53 118.0,471382.287278553 5755031.52114756 118.0,471382.356935557 5755031.44924313 118.0,471382.386472043 5755031.41875368 118.0,471382.3964293 5755031.40847516 118.0,471382.23 5755031.35 118.0,471381.99 5755031.01 118.0)),((471371.48 5755029.595 118.0,471371.53 5755029.44 118.0,471371.21 5755029.53 118.0,471370.89 5755029.62 118.0,471371.43 5755029.75 118.0,471371.48 5755029.595 118.0)),((471383.82 5755029.14 118.0,471383.3 5755029.13 118.0,471383.525 5755029.24 118.0,471383.75 5755029.35 118.0,471384.1 5755029.6 118.0,471384.05 5755029.46 118.0,471383.82 5755029.14 118.0)),((471383.635 5755029.395 118.0,471383.52 5755029.44 118.0,471383.24 5755029.76 118.0,471383.49 5755029.71 118.0,471383.75 5755029.35 118.0,471383.635 5755029.395 118.0)),((471383.49 5755029.95 118.0,471383.24 5755029.76 118.0,471383.21 5755029.88 118.0,471383.18 5755030 118.0,471383.49 5755029.95 118.0)))");
        set4.add("non_geo", "Lindemann-Platz");
        jab.add(set4);
        JsonArray dataObject = jab.build();
        Entity<String> datasets = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(datasets);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateGeoSetsSimple---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            createdGeoSets += 4;
            if (responseText.contains(",")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test get multiple geometry sets
     *
     * @return
     */
    public boolean testGetGeoSetsSimple() {
        if (webTarget == null) {
            System.out.println("WebTarget is null!");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetGeoSetsSimple---");
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
            if (recordsArr.size() != createdGeoSets) {
                System.out.println("Expected that there are " + createdGeoSets + " datasets, but there where " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a contains geofilter
     *
     * @return
     */
    public boolean testSCOFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "point2d,sco,SRID=25832;POINT(472023.584332 5755448.168211)");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSCOFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a contains geofilter
     *
     * @return
     */
    public boolean testSCOFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "point2d,sco,SRID=25832;POINT(472059.584332 5755448.168211)");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSCOFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 0) {
                System.out.println("Expected that there are 0 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a contains geofilter on non existend column
     *
     * @return
     */
    public boolean testSCOFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,sco,SRID=25832;POINT(472059.584332 5755448.168211");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSCOFilterMissingAttribute---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a contains geofilter with 3d point value
     *
     * @return
     */
    public boolean testSCOFilter3DPoint() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "point3d,sco,SRID=5555;POINT(471802.553 5756386.124 173.5)");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSCOFilter3DPoint---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a contains geofilter with 2d multipolygon
     * value
     *
     * @return
     */
    public boolean testSCOFilter2DMultipolygon() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "multipolygon2d,sco,SRID=25832;MULTIPOLYGON(((471804.796 5756387.331,471810.576 5756384.561,471809.684 5756382.703,471810.975 5756382.084,471816.292 5756380.342,471821.961 5756379.797,471821.596 5756368.72,471818.571 5756370.037,471814.517 5756369.315,471811.926 5756370.78,471807.187 5756371.203,471805.32 5756373.097,471801.578 5756374.056,471799.658 5756376.306,471795.534 5756377.527,471794.316 5756379.705,471790.496 5756381.757,471789.345 5756384.437,471785.919 5756387.095,471794.769 5756394.049,471798.595 5756389.188,471802.553 5756386.124,471803.903 5756385.474,471804.796 5756387.331)))");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSCOFilter2DMultipolygon---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a contains geofilter with 3d multipolygon
     * value
     *
     * @return
     */
    public boolean testSCOFilter3DMultipolygon() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "multipolygon3d,sco,SRID=5555;MULTIPOLYGON(((471361.680002213 5755037.32800102 0.0, 471368.246999741 5755041.79300117 0.0, 471374.757001877 5755041.13599968 0.0, 471361.680002213 5755037.32800102 0.0)))");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSCOFilter3DMultipolygon---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a contains geofilter with 3d polygon value
     *
     * @return
     */
    public boolean testSCOFilter3DPolygon() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "multipolygon3d,sco,SRID=5555;POLYGON((471369.5 5755012.49 118.0,471369.493384076 5755012.46542657 118.0,471369.471282536 5755012.48198582 118.0,471369.431870814 5755012.51151448 118.0,471369.5 5755012.49 118.0))");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSCOFilter3DPolygon---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a crosses geofilter
     *
     * @return
     */
    public boolean testSCRFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "multipolygon2d,scr,SRID=25832;LINESTRING(471706.445001602 5755598.41200066, 471706.445001602 5755498.41200066, 471706.445001602 5755198.41200066, 471706.445001602 5755098.41200066, 471706.445001602 5754198.41200066, 471706.445001602 5754098.41200066)");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSCRFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a crosses geofilter
     *
     * @return
     */
    public boolean testSCRFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "point2d,sco,SRID=25832;MULTIPOINT(571706.445001602 5755598.41200066, 571706.445001602 5755498.41200066, 571706.445001602 5755198.41200066, 571706.445001602 5755098.41200066, 571706.445001602 5754198.41200066)");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSCRFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 0) {
                System.out.println("Expected that there are 0 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a crosses geofilter on non existend column
     *
     * @return
     */
    public boolean testSCRFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,scr,SRID=25832;POINT(472059.584332 5755448.168211");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSCRFilterMissingAttribute---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a disjoint geofilter
     *
     * @return
     */
    public boolean testSDIFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "multipolygon2d,sdi,SRID=25832;LINESTRING(571706.445001602 5755598.41200066, 571706.445001602 5755498.41200066, 571706.445001602 5755198.41200066, 571706.445001602 5755098.41200066, 571706.445001602 5754198.41200066, 571706.445001602 5754098.41200066)");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSDIFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != this.createdGeoSets) {
                System.out.println("Expected that there are " + this.createdGeoSets + " dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a disjoint geofilter
     *
     * @return
     */
    public boolean testSDIFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "point2d,sdi,SRID=25832;MULTIPOINT(471703.330843 5755213.129580, 472023.584332 5755448.168211, 471805.591493 5756379.729509, 955521.916581662 6790232.01843556)");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSDIFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 0) {
                System.out.println("Expected that there are 0 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a disjoint geofilter on non existend column
     *
     * @return
     */
    public boolean testSDIFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,sdi,SRID=25832;POINT(472059.584332 5755448.168211");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSDIFilterMissingAttribute---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a equals geofilter
     *
     * @return
     */
    public boolean testSEQFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "multipolygon2d,seq,SRID=25832;MULTIPOLYGON(((472024.026 5755455.491,472022.681 5755454.573,472027.693 5755447.905,472028.884 5755447.01,472029.878 5755448.176,472035.294 5755444.102,472042.444 5755441.325,472039.037 5755430.499,472038.647 5755430.62,472030.801 5755433.056,472031.472 5755434.537,472023.959 5755437.924,472024.74 5755439.125,472018.297 5755443.299,472019.341 5755444.388,472013.515 5755449.943,472014.716 5755450.943,472009.385 5755457.331,472011.948 5755458.633,472011.558 5755459.397,472009.756 5755462.933,472017.585 5755466.474,472017.971 5755465.619,472020.318 5755460.42,472024.026 5755455.491)))");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSEQFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a equals geofilter
     *
     * @return
     */
    public boolean testSEQFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "point2d,seq,SRID=25832;POINT(471374.757001877 5755041.13599968)");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSEQFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 0) {
                System.out.println("Expected that there are 0 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a equals geofilter on non existend column
     *
     * @return
     */
    public boolean testSEQFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,seq,SRID=25832;POINT(472059.584332 5755448.168211");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSEQFilterMissingAttribute---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a intersects geofilter
     *
     * @return
     */
    public boolean testSINFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "multipolygon2d,sin,SRID=25832;MULTIPOINT(471707.870000839 5755194.99500084, 471717.870000839 5755194.99500084, 472707.870000839 5755194.99500084)");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSINFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a intersects geofilter
     *
     * @return
     */
    public boolean testSINFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "point2d,sin,SRID=25832;POINT(471374.757001877 5755041.13599968)");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSINFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 0) {
                System.out.println("Expected that there are 0 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a intersects geofilter on non existend
     * column
     *
     * @return
     */
    public boolean testSINFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,sin,SRID=25832;POINT(472059.584332 5755448.168211");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSINFilterMissingAttribute---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a overlaps geofilter
     *
     * @return
     */
    public boolean testSOVFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "multipolygon2d,sov,SRID=25832;MULTIPOLYGON(((472035.294 5755464.102,472035.444 5755441.325,472039.037 5755430.499,472038.647 5755430.62,472030.801 5755423.056,472040.801 5755433.056,472050.801 5755453.056,472035.294 5755464.102)))");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSOVFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a overlaps geofilter
     *
     * @return
     */
    public boolean testSOVFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "point2d,sov,SRID=25832;POINT(471374.757001877 5755041.13599968)");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSOVFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 0) {
                System.out.println("Expected that there are 0 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a overlaps geofilter on non existend column
     *
     * @return
     */
    public boolean testSOVFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,sov,SRID=25832;POINT(472059.584332 5755448.168211");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSOVFilterMissingAttribute---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a touches geofilter
     *
     * @return
     */
    public boolean testSTOFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "multipolygon2d,sto,SRID=25832;POINT(471706.445001602 5755198.41200066)");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSTOFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a touches geofilter
     *
     * @return
     */
    public boolean testSTOFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "point2d,sto,SRID=25832;POINT(471374.757001877 5755041.13599968)");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSTOFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 0) {
                System.out.println("Expected that there are 0 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a touches geofilter on non existend column
     *
     * @return
     */
    public boolean testSTOFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,sto,SRID=25832;POINT(472059.584332 5755448.168211");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSTOFilterMissingAttribute---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a within geofilter
     *
     * @return
     */
    public boolean testSWIFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "point2d,swi,SRID=25832;POLYGON((955511.916581662 6790234.01843556, 955531.916581662 6790234.01843556, 955521.916581662 6790222.01843556, 955511.916581662 6790234.01843556))");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSWIFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 1) {
                System.out.println("Expected that there are 1 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a within geofilter
     *
     * @return
     */
    public boolean testSWIFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "point2d,swi,SRID=25832;POINT(471374.757001877 5755041.13599968)");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSWIFilterNotFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != 0) {
                System.out.println("Expected that there are 0 dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using a within geofilter on non existend column
     *
     * @return
     */
    public boolean testSWIFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,swi,SRID=25832;POINT(472059.584332 5755448.168211");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSWIFilterMissingAttribute---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using an is closed geofilter
     *
     * @return
     */
    public boolean testSICFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "point3d,sic");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSICFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != this.createdGeoSets) {
                System.out.println("Expected that there are " + this.createdGeoSets + " dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using an is closed geofilter on non existend
     * column
     *
     * @return
     */
    public boolean testSICFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,sic");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSICFilterMissingAttribute---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using an is simple geofilter
     *
     * @return
     */
    public boolean testSISFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "multipolygon3d,sis");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSISFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != this.createdGeoSets) {
                System.out.println("Expected that there are " + this.createdGeoSets + " dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using an is simple geofilter on non existend
     * column
     *
     * @return
     */
    public boolean testSISFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,sis");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSISFilterMissingAttribute---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using an is valid geofilter
     *
     * @return
     */
    public boolean testSIVFilterFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "multipolygon2d,siv");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSIVFilterFound---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // There should be one dataset
            JsonParser parser = Json.createParser(new StringReader(responseText));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray recordsArr = responseObj.getJsonArray("records");
            if (recordsArr == null) {
                System.out.println(">records< attribute is missing.");
                return false;
            }
            if (recordsArr.size() != this.createdGeoSets) {
                System.out.println("Expected that there are " + this.createdGeoSets + " dataset, but there were " + recordsArr.size());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests to get a dataset using an is valid geofilter on non existend column
     *
     * @return
     */
    public boolean testSIVFilterMissingAttribute() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testgeocol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "notexisting,siv");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testSIVFilterMissingAttribute---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }
}
