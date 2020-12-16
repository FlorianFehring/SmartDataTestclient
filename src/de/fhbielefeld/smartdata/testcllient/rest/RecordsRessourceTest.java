package de.fhbielefeld.smartdata.testcllient.rest;

import de.fhbielefeld.scl.rest.util.WebTargetCreator;
import java.io.StringReader;
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
                    .queryParam("storage", STORAGE+"2");
            
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
                    .queryParam("storage", STORAGE+"2");
            
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
     * Tests to get a dataset using a contains geofilter with 2d multipolygon value
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
     * Tests to get a dataset using a contains geofilter with 3d multipolygon value
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
     * Tests to get a dataset using a intersects geofilter on non existend column
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
     * Tests to get a dataset using an is closed geofilter on non existend column
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
     * Tests to get a dataset using an is simple geofilter on non existend column
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
