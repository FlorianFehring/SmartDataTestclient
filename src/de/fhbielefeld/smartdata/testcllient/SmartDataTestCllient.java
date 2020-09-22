package de.fhbielefeld.smartdata.testcllient;

import de.fhbielefeld.smartdata.testcllient.rest.BaseRessourceTest;
import de.fhbielefeld.smartdata.testcllient.rest.RecordsRessourceTest;
import de.fhbielefeld.smartdata.testcllient.rest.TableRessourceTest;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

/**
 * Client for testing SmartData REST webservices
 *
 * @author Florian Fehring
 */
public class SmartDataTestCllient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Executeing tests
        testRecordsRessource();
    }

    public static boolean testRecordsRessource() {

        JsonObjectBuilder builder = Json.createObjectBuilder();

        BaseRessourceTest brt = new BaseRessourceTest();
        builder.add("testCreateSchema", brt.testCreateSchema());
        builder.add("testCreateSchemaAllreadyExists", brt.testCreateSchemaAllreadyExists());
        builder.add("testGetTablesNoOne", brt.testGetTablesNoOne());

        TableRessourceTest trt = new TableRessourceTest();
        builder.add("testCreateTable", trt.testCreateTable());
        builder.add("testCreateTableAllreadyExists", trt.testCreateTableAllreadyExists());
        builder.add("testGetColumns", trt.testGetColumns());
        builder.add("testAddColumns", trt.testAddColumns());
        builder.add("testAddGeoColumns", trt.testAddGeoColumns());
        builder.add("testGetGeoColumns", trt.testGetGeoColumns());
        builder.add("testChangeSRID", trt.testChangeSRID());
        RecordsRessourceTest rrt = new RecordsRessourceTest();
//        builder.add("testCreateSetSimple", rrt.testCreateSetSimple());
//        builder.add("testGetSetSimple", rrt.testGetSetSimple());
//        builder.add("testCreateSetsSimple", rrt.testCreateSetsSimple());
//        builder.add("testGetSetsSimple", rrt.testGetSetsSimple());
//        builder.add("testCreateSetUnicode", rrt.testCreateSetUnicode());
//        builder.add("testUpdateSetSimple", rrt.testUpdateSetSimple());
//        builder.add("testUpdateSetsSimple", rrt.testUpdateSetsSimple());
//        builder.add("testDeleteSet", rrt.testDeleteSet());
//        builder.add("testDeleteSets", rrt.testDeleteSets());

        builder.add("testDeleteSchema", brt.testDeleteSchema());

        JsonObject dataObject = builder.build();

        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataObject);
        jsonWriter.close();

        System.out.println(sw.toString());
        return true;
    }

}
