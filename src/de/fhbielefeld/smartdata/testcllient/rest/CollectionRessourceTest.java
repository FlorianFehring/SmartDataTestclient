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
public class CollectionRessourceTest {

    private static LocalDateTime startDateTime;
    private static final String SERVER = "http://localhost:8080/SmartData/smartdata/";
    private static final String RESOURCE = "collection";
    private static final String STORAGE = "test";
    private static WebTarget webTarget;
    private static final boolean PRINT_DEBUG_MESSAGES = true;

    public CollectionRessourceTest() {
        startDateTime = LocalDateTime.now();
        webTarget = WebTargetCreator.createWebTarget(SERVER, RESOURCE);
    }

    /**
     * Test create a collection
     *
     * @return true if the collection could be created
     */
    public boolean testCreateCollection() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("testcol")
                .path("create")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder colarr = Json.createArrayBuilder();
        // Name column
        JsonObjectBuilder namecol = Json.createObjectBuilder();
        namecol.add("name", "name");
        namecol.add("type", "VARCHAR(255)");
        colarr.add(namecol);
        // Float value column
        JsonObjectBuilder floatcol = Json.createObjectBuilder();
        floatcol.add("name", "float_value");
        floatcol.add("type", "FLOAT");
        colarr.add(floatcol);
        // Int value column
        JsonObjectBuilder intcol = Json.createObjectBuilder();
        intcol.add("name", "int_value");
        intcol.add("type", "INT");
        colarr.add(intcol);
        // Boolean cloumn
        JsonObjectBuilder boolcol = Json.createObjectBuilder();
        boolcol.add("name", "bool_value");
        boolcol.add("type", "BOOLEAN");
        colarr.add(boolcol);
        // Timestamp column
        JsonObjectBuilder tscol = Json.createObjectBuilder();
        tscol.add("name", "ts_value");
        tscol.add("type", "TIMESTAMP");
        colarr.add(tscol);
        // Text column
        JsonObjectBuilder txtcol = Json.createObjectBuilder();
        txtcol.add("name", "txt_value");
        txtcol.add("type", "TEXT");
        colarr.add(txtcol);
        // Create attribute array
        builder.add("attributes", colarr);
        JsonObject dataObject = builder.build();
        Entity<String> coldef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(coldef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateCollection---");
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
     * Test create a collection with givin id column
     *
     * @return true if the collection could be created
     */
    public boolean testCreateCollectionGivingAutoId() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("colwithautoid")
                .path("create")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", "testcol");
        JsonArrayBuilder colarr = Json.createArrayBuilder();
        // Name column
        JsonObjectBuilder idcol = Json.createObjectBuilder();
        idcol.add("name", "tid"); // Name of the id column can freely choosen
        idcol.add("type", "int8");
        idcol.add("isAutoIncrement", true);
        idcol.add("isIdentity", true);
        colarr.add(idcol);
        // Name column
        JsonObjectBuilder namecol = Json.createObjectBuilder();
        namecol.add("name", "name");
        namecol.add("type", "VARCHAR(255)");
        colarr.add(namecol);
        builder.add("attributes", colarr);
        JsonObject dataObject = builder.build();
        Entity<String> coldef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(coldef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateCollectionGivingAutoId---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.CREATED.getStatusCode() == response.getStatus()) {
            // Check definition
            WebTarget targetQ = webTarget
                    .path("colwithautoid")
                    .path("getAttributes")
                    .queryParam("storage", STORAGE);
            Response responseQ = targetQ.request(MediaType.APPLICATION_JSON).get();
            String responseTextQ = responseQ.readEntity(String.class);

            JsonParser parserQ = Json.createParser(new StringReader(responseTextQ));
            parserQ.next();
            JsonObject responseObj = parserQ.getObject();
            JsonArray listArr = responseObj.getJsonArray("list");
            for (int i = 0; i < listArr.size(); i++) {
                JsonObject curObj = (JsonObject) listArr.get(i);
                if (curObj.getString("name").equals("tid")) {
                    boolean isId = curObj.getBoolean("isIdentity");
                    if (!isId) {
                        System.out.println("Expected >tid< to be identity column, but is not");
                        return false;
                    }
                    boolean isAutoInc = curObj.getBoolean("isAutoIncrement");
                    if (!isAutoInc) {
                        System.out.println("Expected >tid< to be autoincrement, but is not");
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

        /**
     * Test create a collection with givin id column
     *
     * @return true if the collection could be created
     */
    public boolean testCreateCollectionGivingId() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("colwithid")
                .path("create")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", "testcol");
        JsonArrayBuilder colarr = Json.createArrayBuilder();
        // Name column
        JsonObjectBuilder idcol = Json.createObjectBuilder();
        idcol.add("name", "tid"); // Name of the id column can freely choosen
        idcol.add("type", "int8");
        idcol.add("isIdentity", true);
        colarr.add(idcol);
        // Name column
        JsonObjectBuilder namecol = Json.createObjectBuilder();
        namecol.add("name", "name");
        namecol.add("type", "VARCHAR(255)");
        colarr.add(namecol);
        builder.add("attributes", colarr);
        JsonObject dataObject = builder.build();
        Entity<String> coldef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(coldef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateCollectionGivingId---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.CREATED.getStatusCode() == response.getStatus()) {
            // Check definition
            WebTarget targetQ = webTarget
                    .path("colwithid")
                    .path("getAttributes")
                    .queryParam("storage", STORAGE);
            Response responseQ = targetQ.request(MediaType.APPLICATION_JSON).get();
            String responseTextQ = responseQ.readEntity(String.class);

            JsonParser parserQ = Json.createParser(new StringReader(responseTextQ));
            parserQ.next();
            JsonObject responseObj = parserQ.getObject();
            JsonArray listArr = responseObj.getJsonArray("list");
            for (int i = 0; i < listArr.size(); i++) {
                JsonObject curObj = (JsonObject) listArr.get(i);
                if (curObj.getString("name").equals("tid")) {
                    boolean isId = curObj.getBoolean("isIdentity");
                    if (!isId) {
                        System.out.println("Expected >tid< to be identity column, but is not");
                        return false;
                    }
                    boolean isAutoInc = curObj.getBoolean("isAutoIncrement");
                    if (isAutoInc) {
                        System.out.println("Expected >tid< to be NOT autoincrement, but is");
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Tests the response for creating a collection, that is allready existing.
     *
     * @return true if response states no changes done
     */
    public boolean testCreateCollectionAllreadyExists() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("testcol")
                .path("create")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", "testcol");
        JsonArrayBuilder colarr = Json.createArrayBuilder();
        // Name column
        JsonObjectBuilder namecol = Json.createObjectBuilder();
        namecol.add("name", "name");
        namecol.add("type", "VARCHAR(255)");
        colarr.add(namecol);
        // Value column
        JsonObjectBuilder valcol = Json.createObjectBuilder();
        valcol.add("name", "value");
        valcol.add("type", "FLOAT");
        colarr.add(valcol);
        builder.add("attributes", colarr);
        JsonObject dataObject = builder.build();
        Entity<String> coldef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(coldef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateCollectionAllreadyExists---");
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
     * Tests if there comes an empty list, if there are no collections in the
     * storage
     *
     * @return
     */
    public boolean testGetAttributes() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("testcol")
                .path("getAttributes")
                .queryParam("storage", STORAGE);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetAttributes---");
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
     * Tests getting attributes from not existing schema
     *
     * @return
     */
    public boolean testGetAttributesNoExSchema() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }
//NOCH ZU DEN AUFRUFEN HINZUFÜGEN!
        WebTarget target = webTarget.path("testcol")
                .path("getAttributes")
                .queryParam("storage", "notexisting");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetAttributesNoExSchema---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.NOT_FOUND.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Tests the adding of additional attributes
     *
     * @return true if response states no changes done
     */
    public boolean testAddAttributes() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("testcol")
                .path("addAttributes")
                .queryParam("storage", STORAGE);

        JsonArrayBuilder colarr = Json.createArrayBuilder();
        // Name attribute
        JsonObjectBuilder namecol = Json.createObjectBuilder();
        namecol.add("name", "addedAttribute1");
        namecol.add("type", "INT");
        colarr.add(namecol);
        // Value attribute
        JsonObjectBuilder valcol = Json.createObjectBuilder();
        valcol.add("name", "addedAttribute2");
        valcol.add("type", "FLOAT");
        colarr.add(valcol);
        JsonArray dataObject = colarr.build();
        Entity<String> coldef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).put(coldef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testAddAttributes---");
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
     * Tests the adding of additional attributes
     *
     * @return true if response states no changes done
     */
    public boolean testAddGeoAttributes() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("testcol")
                .path("addAttributes")
                .queryParam("storage", STORAGE);

        JsonArrayBuilder colarr = Json.createArrayBuilder();
        // Name attribute
        JsonObjectBuilder pointcol = Json.createObjectBuilder();
        pointcol.add("name", "addedGeoAttribute1");
        pointcol.add("type", "geometry(Point,4326)");
        colarr.add(pointcol);
        JsonArray dataObject = colarr.build();
        Entity<String> coldef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).put(coldef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testAddGeoAttributes---");
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
     * Testing getting information about geo attributes
     *
     * @return
     */
    public boolean testGetGeoAttributes() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("testcol")
                .path("getAttributes")
                .queryParam("storage", STORAGE);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetGeoAttributes---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }

        JsonParser parser = Json.createParser(new StringReader(responseText));
        parser.next();
        JsonObject responseObj = parser.getObject();
        JsonArray listArr = responseObj.getJsonArray("list");
        for (int i = 0; i < listArr.size(); i++) {
            JsonObject curObj = (JsonObject) listArr.get(i);
            if (curObj.getString("name").equals("addedGeoAttribute1")) {
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
     * Tests the adding of additional attributes
     *
     * @return true if response states no changes done
     */
    public boolean testChangeSRID() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("testcol")
                .path("changeAttribute")
                .queryParam("storage", STORAGE);

        JsonArrayBuilder colarr = Json.createArrayBuilder();
        // Name attribute
        JsonObjectBuilder pointcol = Json.createObjectBuilder();
        pointcol.add("name", "addedGeoAttribute1");
        pointcol.add("srid", "3857");
        colarr.add(pointcol);
        JsonArray dataObject = colarr.build();
        Entity<String> coldef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).put(coldef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testChangeSRID---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            // Request 
            WebTarget targetCheck = webTarget.path("testcol")
                    .path("getAttributes")
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
                if (curObj.getString("name").equals("addedGeoAttribute1")) {
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
