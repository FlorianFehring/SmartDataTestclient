package de.fhbielefeld.smartdata.testcllient.rest;

import de.fhbielefeld.scl.rest.util.WebTargetCreator;
import java.io.StringReader;
import java.time.LocalDateTime;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.stream.JsonParser;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
        // JSON column
        JsonObjectBuilder jsoncol = Json.createObjectBuilder();
        jsoncol.add("name", "json_value");
        jsoncol.add("type", "JSON");
        colarr.add(jsoncol);
        // BYTEA column
        JsonObjectBuilder byteacol = Json.createObjectBuilder();
        byteacol.add("name", "bytea_value");
        byteacol.add("type", "BYTEA");
        colarr.add(byteacol);
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
                    .queryParam("storage", STORAGE);
            Response responseQ = targetQ.request(MediaType.APPLICATION_JSON).get();
            String responseTextQ = responseQ.readEntity(String.class);
            System.out.println("Response from definition check: " + responseQ.getStatus());
            System.out.println(responseTextQ);

            JsonParser parserQ = Json.createParser(new StringReader(responseTextQ));
            parserQ.next();
            JsonObject responseObj = parserQ.getObject();
            JsonArray listArr = responseObj.getJsonArray("attributes");
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
                    .queryParam("storage", STORAGE);
            Response responseQ = targetQ.request(MediaType.APPLICATION_JSON).get();
            String responseTextQ = responseQ.readEntity(String.class);

            JsonParser parserQ = Json.createParser(new StringReader(responseTextQ));
            parserQ.next();
            JsonObject responseObj = parserQ.getObject();
            JsonArray listArr = responseObj.getJsonArray("attributes");
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
     * Test create a collection with a id column of type string
     *
     * @return true if the collection could be created
     */
    public boolean testCreateCollectionGivingStringId() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("colwithstrid")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder colarr = Json.createArrayBuilder();
        // Name column
        JsonObjectBuilder idcol = Json.createObjectBuilder();
        idcol.add("name", "id"); // Name of the id column can freely choosen
        idcol.add("type", "varchar");
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
                    .queryParam("storage", STORAGE);
            Response responseQ = targetQ.request(MediaType.APPLICATION_JSON).get();
            String responseTextQ = responseQ.readEntity(String.class);
            System.out.println("Response from definition check: " + responseQ.getStatus());
            System.out.println(responseTextQ);

            JsonParser parserQ = Json.createParser(new StringReader(responseTextQ));
            parserQ.next();
            JsonObject responseObj = parserQ.getObject();
            JsonArray listArr = responseObj.getJsonArray("attributes");
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
     * Create a collection with attributes that are flattend datasets (e.g. #
     * numbered attributes {U1,I11},{U2,I2},...
     *
     * @return true if the collection was created
     */
    public boolean testCreateCollectionFlattend() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("colflattend")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder colarr = Json.createArrayBuilder();
        // Name column
        JsonObjectBuilder namecol = Json.createObjectBuilder();
        namecol.add("name", "lonevalue");
        namecol.add("type", "VARCHAR(255)");
        colarr.add(namecol);
        // Create deflatten columns
        for (int i = 0; i < 255; i++) {
            JsonObjectBuilder flatcolU = Json.createObjectBuilder();
            flatcolU.add("name", "U" + i);
            flatcolU.add("type", "FLOAT");
            colarr.add(flatcolU);

            JsonObjectBuilder flatcolI = Json.createObjectBuilder();
            flatcolI.add("name", "I" + i);
            flatcolI.add("type", "FLOAT");
            colarr.add(flatcolI);
        }
        builder.add("attributes", colarr);
        JsonObject dataObject = builder.build();
        Entity<String> coldef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(coldef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateCollectionFlattend---");
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
     * Test create a collection with binary data columns
     *
     * @return true if the collection could be created
     */
    public boolean testCreateCollectionBinary() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("testcolbinary")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder colarr = Json.createArrayBuilder();
        // BYTEA column
        JsonObjectBuilder byteacol = Json.createObjectBuilder();
        byteacol.add("name", "bytea_value");
        byteacol.add("type", "BYTEA");
        colarr.add(byteacol);
        // Create attribute array
        builder.add("attributes", colarr);
        JsonObject dataObject = builder.build();
        Entity<String> coldef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(coldef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateCollectionBinary---");
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
     * Tests the response for creating a collection, that is allready existing.
     *
     * @return true if response states no changes done
     */
    public boolean testCreateCollectionAllreadyExists() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("testcol")
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
        WebTarget target = webTarget.path("testcol")
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
                .queryParam("storage", STORAGE);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetGeoAttributes---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }

        if (Response.Status.OK.getStatusCode() != response.getStatus()) {
            return false;
        }

        JsonParser parser = Json.createParser(new StringReader(responseText));
        parser.next();
        JsonObject responseObj = parser.getObject();
        JsonArray listArr = responseObj.getJsonArray("attributes");
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
            JsonArray listArr = responseObj.getJsonArray("attributes");
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

    /**
     * Test if the attributes can be get and with that data a new table can be
     * created on the second storage.
     *
     * @return true if possible
     */
    public boolean testGetAttributesAndCreateTable() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("testcol")
                .queryParam("storage", STORAGE);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetAttributesAndCreateTable---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            WebTarget target2 = webTarget
                    .path("testcol")
                    .queryParam("storage", STORAGE + "2");

            // Recreate on second storage
            Entity<String> coldef2 = Entity.json(responseText);

            Response response2 = target2.request(MediaType.APPLICATION_JSON).post(coldef2);
            String responseText2 = response2.readEntity(String.class);
            if (PRINT_DEBUG_MESSAGES) {
                System.out.println(response2.getStatusInfo());
                System.out.println(responseText2);
            }
            if (Response.Status.CREATED.getStatusCode() == response2.getStatus()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Test create a collection with geometry attributes
     *
     * @return true if the collection could be created
     */
    public boolean testCreateGeoCollection() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("testgeocol")
                .queryParam("storage", STORAGE);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder colarr = Json.createArrayBuilder();
        // 2D Point attribute
        JsonObjectBuilder point2dcol = Json.createObjectBuilder();
        point2dcol.add("name", "point2d");
        point2dcol.add("type", "geometry(Point,25832)");
        colarr.add(point2dcol);
        // 3D Point attribute
        JsonObjectBuilder point3dcol = Json.createObjectBuilder();
        point3dcol.add("name", "point3d");
        point3dcol.add("type", "geometry(PointZ,5555)");
        colarr.add(point3dcol);
        // 2D Multipolygon attribute
        JsonObjectBuilder multipolygon2dcol = Json.createObjectBuilder();
        multipolygon2dcol.add("name", "multipolygon2d");
        multipolygon2dcol.add("type", "geometry(Multipolygon,25832)");
        colarr.add(multipolygon2dcol);
        // 3D Multipolygon attribute
        JsonObjectBuilder multipolygon3dcol = Json.createObjectBuilder();
        multipolygon3dcol.add("name", "multipolygon3d");
        multipolygon3dcol.add("type", "geometry(MultipolygonZ,5555)");
        colarr.add(multipolygon3dcol);
        // Non-geometry attribute
        JsonObjectBuilder non_geocol = Json.createObjectBuilder();
        non_geocol.add("name", "non_geo");
        non_geocol.add("type", "VARCHAR(255)");
        colarr.add(non_geocol);
        builder.add("attributes", colarr);
        JsonObject dataObject = builder.build();
        Entity<String> coldef = Entity.json(dataObject.toString());

        Response response = target.request(MediaType.APPLICATION_JSON).post(coldef);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateGeoCollection---");
            System.out.println(response.getStatusInfo());
            System.out.println(responseText);
        }
        if (Response.Status.CREATED.getStatusCode() == response.getStatus()) {
            return true;
        } else {
            return false;
        }
    }
}
