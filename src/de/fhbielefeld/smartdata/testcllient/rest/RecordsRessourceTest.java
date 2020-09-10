package de.fhbielefeld.smartdata.testcllient.rest;

import de.fhbielefeld.scl.rest.util.WebTargetCreator;
import java.time.LocalDateTime;
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
    private static final String SCHEMA = "test";
    private static WebTarget webTarget;
    private static final boolean PRINT_DEBUG_MESSAGES = true;

    public RecordsRessourceTest() {
        startDateTime = LocalDateTime.now();
        System.out.println("TEST beforeAll");
        webTarget = WebTargetCreator.createWebTarget(SERVER, RESOURCE);
    }

    
    
    public boolean testGetOne() {
        if (webTarget == null) {
            System.out.println("WebTarget is null! Änderung?");
        }

        WebTarget target = webTarget.path("testdaten")
                .path("1");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        String responseText = response.readEntity(String.class);
        if (PRINT_DEBUG_MESSAGES) {
            System.out.println("---testGetOne---");
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
