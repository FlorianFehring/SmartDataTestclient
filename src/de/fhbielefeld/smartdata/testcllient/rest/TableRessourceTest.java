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
 * Test methods for the TestRessource
 *
 * @author Florian Fehring
 */
public class TableRessourceTest {

    private static LocalDateTime startDateTime;
    private static final String SERVER = "http://localhost:8080/SmartData/smartdata/";
    private static final String RESOURCE = "table";
    private static final String STORAGE = "test";
    private static WebTarget webTarget;
    private static final boolean PRINT_DEBUG_MESSAGES = true;

    public TableRessourceTest() {
        startDateTime = LocalDateTime.now();
        webTarget = WebTargetCreator.createWebTarget(SERVER, RESOURCE);
    }

    /**
     * Test create a collection
     *
     * @return true if the collection could be created
     */
    public boolean testCreateTable() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("testtable")
                .path("create")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", "testtable");
        JsonArrayBuilder colarr = Json.createArrayBuilder();
        // Name column
        JsonObjectBuilder namecol = Json.createObjectBuilder();
        namecol.add("name", "name");
        namecol.add("type", "VARCHAR(255)");
        colarr.add(namecol);
        // Float value column
        JsonObjectBuilder floatcol = Json.createObjectBuilder();
        floatcol.add("name", "float_value");
        floatcol.add("type", "REAL");
        colarr.add(floatcol);
        // Int value column
        JsonObjectBuilder intcol = Json.createObjectBuilder();
        intcol.add("name", "int_value");
        intcol.add("type", "INT");
        colarr.add(intcol);
        // Timestamp column
        JsonObjectBuilder tscol = Json.createObjectBuilder();
        tscol.add("name", "ts_value");
        tscol.add("type", "TIMESTAMP");
        colarr.add(tscol);
        builder.add("columns", colarr);
        JsonObject dataObject = builder.build();
        Entity<String> tabledef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(tabledef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateTable---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.CREATED.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests the response for creating a table, that is allready existing.
     *
     * @return true if response states no changes done
     */
    public boolean testCreateTableAllreadyExists() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("testtable")
                .path("create")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", "testtable");
        JsonArrayBuilder colarr = Json.createArrayBuilder();
        // Name column
        JsonObjectBuilder namecol = Json.createObjectBuilder();
        namecol.add("name", "name");
        namecol.add("type", "VARCHAR(255)");
        colarr.add(namecol);
        // Value column
        JsonObjectBuilder valcol = Json.createObjectBuilder();
        valcol.add("name", "value");
        valcol.add("type", "REAL");
        colarr.add(valcol);
        builder.add("columns", colarr);
        JsonObject dataObject = builder.build();
        Entity<String> tabledef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(tabledef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateTableAllreadyExists---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.NOT_MODIFIED.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests if there comes an empty list, if there are no tables in the storage
     *
     * @return
     */
    public boolean testGetColumns() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("testtable")
                .path("getColumns")
                .queryParam("storage", STORAGE);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetColumns---");
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
     * Tests the adding of additional columns
     *
     * @return true if response states no changes done
     */
    public boolean testAddColumns() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("testtable")
                .path("addColumns")
                .queryParam("storage", STORAGE);

        JsonArrayBuilder colarr = Json.createArrayBuilder();
        // Name column
        JsonObjectBuilder namecol = Json.createObjectBuilder();
        namecol.add("name", "addedColumn1");
        namecol.add("type", "INT");
        colarr.add(namecol);
        // Value column
        JsonObjectBuilder valcol = Json.createObjectBuilder();
        valcol.add("name", "addedColumn2");
        valcol.add("type", "REAL");
        colarr.add(valcol);
        JsonArray dataObject = colarr.build();
        Entity<String> tabledef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).put(tabledef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testAddColumns---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.CREATED.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests the adding of additional columns
     *
     * @return true if response states no changes done
     */
    public boolean testAddGeoColumns() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("testtable")
                .path("addColumns")
                .queryParam("storage", STORAGE);

        JsonArrayBuilder colarr = Json.createArrayBuilder();
        // Name column
        JsonObjectBuilder pointcol = Json.createObjectBuilder();
        pointcol.add("name", "addedGeoColumn1");
        pointcol.add("type", "geometry(Point,4326)");
        colarr.add(pointcol);
        JsonArray dataObject = colarr.build();
        Entity<String> tabledef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).put(tabledef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testAddGeoColumns---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.CREATED.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Testing getting information about geo columns
     *
     * @return
     */
    public boolean testGetGeoColumns() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("testtable")
                .path("getColumns")
                .queryParam("storage", STORAGE);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetGeoColumns---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }

        JsonParser parser = Json.createParser(new StringReader(responseText));
        parser.next();
        JsonObject responseObj = parser.getObject();
        JsonArray listArr = responseObj.getJsonArray("list");
        for (int i = 0; i < listArr.size(); i++) {
            JsonObject curObj = (JsonObject) listArr.get(i);
            if (curObj.getString("name").equals("addedGeoColumn1")) {
                String type = curObj.getString("type");
                if (!type.equalsIgnoreCase("geometry")) {
                    System.out.println("Expected >geometry< but was >" + type + "<");
                    return false;
                }
            }
        }

        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests the adding of additional columns
     *
     * @return true if response states no changes done
     */
    public boolean testChangeSRID() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("testtable")
                .path("changeColumn")
                .queryParam("storage", STORAGE);

        JsonArrayBuilder colarr = Json.createArrayBuilder();
        // Name column
        JsonObjectBuilder pointcol = Json.createObjectBuilder();
        pointcol.add("name", "addedGeoColumn1");
        pointcol.add("srid", "3857");
        colarr.add(pointcol);
        JsonArray dataObject = colarr.build();
        Entity<String> tabledef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).put(tabledef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testChangeSRID---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // Request 
            WebTarget targetCheck = webTarget.path("testtable")
                    .path("getColumns")
                    .queryParam("storage", STORAGE);
            Response responseCheck = targetCheck.request(MediaType.APPLICATION_JSON).get();
            String responseTextCheck = responseCheck.readEntity(String.class);
            if (PRINT_DEBUG_MESSAGES) {
                System.out.println("---testChangeSRIDCheck---");
                System.out.println(responseCheck.getStatusInfo());
                System.out.println(responseTextCheck);
            }

            JsonParser parser = Json.createParser(new StringReader(responseTextCheck));
            parser.next();
            JsonObject responseObj = parser.getObject();
            JsonArray listArr = responseObj.getJsonArray("list");
            for (int i = 0; i < listArr.size(); i++) {
                JsonObject curObj = (JsonObject) listArr.get(i);
                if (curObj.getString("name").equals("addedGeoColumn1")) {
                    String type = curObj.getString("type");
                    if (!type.equalsIgnoreCase("geometry")) {
                        System.out.println("Expected >geometry< but was >" + type + "<");
                        return false;
                    }
                    int srid = curObj.getInt("srid");
                    if (srid != 3857) {
                        System.out.println("Expected srid to be >3857< but was >" + srid + "<");
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
