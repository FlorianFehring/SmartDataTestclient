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
                .path("testcol")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", "testwert");
        builder.add("float_value", 12.2323);
        builder.add("int_value", 42);
        builder.add("ts_value", "2011-12-30T10:15:30");
        JsonObject dataObject = builder.build();
        Entity<String> collectiondef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(collectiondef);
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
        JsonObjectBuilder job1 = Json.createObjectBuilder();
        job1.add("name", "testwert1");
        job1.add("float_value", 12.2323);
        job1.add("int_value", 42);
        job1.add("ts_value", "31.12.2019 12:14");
        jab.add(job1);
        JsonObjectBuilder job2 = Json.createObjectBuilder();
        job2.add("name", "testwert2");
        job2.add("float_value", -11.1111);
        job2.add("int_value", 42);
        job2.add("ts_value", "2013-12-30T10:15:30");
        jab.add(job2);
        JsonObjectBuilder job3 = Json.createObjectBuilder();
        job3.add("name", "testwert3");
        job3.add("float_value", 42.0);
        job3.add("int_value", 42);
        job3.add("ts_value", "2011-12-30 10:15:30.123");
        jab.add(job3);
        JsonArray dataObject = jab.build();
        Entity<String> collectiondef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(collectiondef);
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
        job1.add("name", "Oława - Żołnierzy");
        job1.add("float_value", 99.999);
        job1.add("int_value", 99);
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
                if(intval > maxval) {
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
                if(intval < minval) {
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
        set1.add("int_value", 0);
        sets.add(set1);
        JsonObjectBuilder set2 = Json.createObjectBuilder();
        set2.add("id", 2);
        set2.add("name", "neuer testwert id 2");
        set2.add("float_value", 0.3333);
        set2.add("int_value", 0);
        sets.add(set2);
        JsonObjectBuilder set3 = Json.createObjectBuilder();
        set3.add("id", 3);
        set3.add("name", "neuer testwert id 3");
        set3.add("float_value", 0.3333);
        set3.add("int_value", 0);
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
    public boolean testEQFilterFound() {
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
     * Tests to get a dataset using a negative equals filter
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
            System.out.println("---testNEQFilter---");
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
     * Tests to get a dataset using a negative equals filter
     *
     * @return
     */
    public boolean testNEQFilterNotFound() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE)
                .queryParam("filter", "int_value,neq,42");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testNEQNotFilter---");
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
     * Tests to get a dataset using a negative filter on non existend column
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
                .queryParam("filter", "name,cs,stwe");
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
            if (recordsArr.size() != 4) {
                System.out.println("Expected that there are 4 dataset, but there were " + recordsArr.size());
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
            if (recordsArr.size() != 4) {
                System.out.println("Expected that there are 4 dataset, but there were " + recordsArr.size());
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
                .queryParam("filter", "name,ncs,stwe");
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
                .queryParam("filter", "name,sw,test");
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
            if (recordsArr.size() != 4) {
                System.out.println("Expected that there are 4 dataset, but there were " + recordsArr.size());
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
                .queryParam("filter", "name,nsw,wert");
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
            if (recordsArr.size() != 4) {
                System.out.println("Expected that there are 4 dataset, but there were " + recordsArr.size());
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
                .queryParam("filter", "name,nsw,test");
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
     * Tests to get a dataset using a not starts with filter on non existend column
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
                .queryParam("filter", "name,ew,wert");
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
     * Tests to get a dataset using a not ends with filter on non existend column
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
                .queryParam("filter", "int_value,lt,2");
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
                .queryParam("filter", "int_value,nlt,52");
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
     * Tests to get a dataset using a not lower than filter on non existend column
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
                .queryParam("filter", "int_value,le,2");
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
     * Tests to get a dataset using a lower or equal filter on non existend column
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
                .queryParam("filter", "int_value,nle,52");
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
     * Tests to get a dataset using a not lower or equal filter on non existend column
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
                .queryParam("filter", "int_value,ge,52");
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
     * Tests to get a dataset using a greater or equal filter on non existend column
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
                .queryParam("filter", "int_value,nge,42");
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
     * Tests to get a dataset using a not greater or equal filter on non existend column
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
            if (recordsArr.size() != 4) {
                System.out.println("Expected that there are 4 dataset, but there were " + recordsArr.size());
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
                .queryParam("filter", "int_value,gt,52");
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
                .queryParam("filter", "int_value,ngt,41");
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
     * Tests to get a dataset using a not greater than filter on non existend column
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
                .queryParam("filter", "int_value,nbt,41,43");
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
}