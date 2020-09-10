package de.fhbielefeld.smartdata.testcllient.rest;

import de.fhbielefeld.scl.rest.util.WebTargetCreator;
import java.time.LocalDateTime;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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
    private static final String SCHEMA = "test";
    private static WebTarget webTarget;
    private static final boolean PRINT_DEBUG_MESSAGES = true;

    public TableRessourceTest() {
        startDateTime = LocalDateTime.now();
        webTarget = WebTargetCreator.createWebTarget(SERVER, RESOURCE);
    }

    /**
     * Test create a schema
     *
     * @return true if the schema could be created
     */
    public boolean testCreateTable() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget
                .path("testtable")
                .path("create")
                .queryParam("schema", SCHEMA);
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
     * Tests the response for creating a schema, that is allready existing.
     *
     * @return true if response states no changes done
     */
    public boolean testCreateSchemaAllreadyExists() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("createSchema")
                .queryParam("schema", SCHEMA);
        Response response = target.request(MediaType.APPLICATION_JSON).post(null);
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testCreateSchemaAllreadyExists---");
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
     * Tests if there comes an empty list, if there are no tables in the schema
     *
     * @return
     */
    public boolean testGetTablesNoOne() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("getTables")
                .queryParam("schema", SCHEMA);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetTablesNoOne---");
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
     * Tests if the list of tables is delivered
     *
     * @return
     */
    public boolean testGetTables() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("getTables")
                .queryParam("schema", SCHEMA);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetTables---");
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
     * Tests the deleteing of a schema
     *
     * @return true if schema was deleted
     */
    public boolean testDeleteSchema() {
        if (webTarget == null) {
            System.out.println("WebTarget is missing could not connect to WebService.");
        }

        WebTarget target = webTarget.path("deleteSchema")
                .queryParam("schema", SCHEMA);
        Response response = target.request(MediaType.APPLICATION_JSON).delete();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testDeleteSchema---");
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
