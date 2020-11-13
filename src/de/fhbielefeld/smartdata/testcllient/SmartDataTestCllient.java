package de.fhbielefeld.smartdata.testcllient;

import de.fhbielefeld.smartdata.testcllient.rest.StorageRessourceTest;
import de.fhbielefeld.smartdata.testcllient.rest.RecordsRessourceTest;
import de.fhbielefeld.smartdata.testcllient.rest.CollectionRessourceTest;
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

        StorageRessourceTest brt = new StorageRessourceTest();
        builder.add("testGetAbilities", brt.testGetAbilities());
        builder.add("testCreateStorage", brt.testCreateStorage());
        builder.add("testCreateStorageAllreadyExists", brt.testCreateStorageAllreadyExists());
        builder.add("testGetCollectionsNoOne", brt.testGetCollectionsNoOne());

        CollectionRessourceTest trt = new CollectionRessourceTest();
        builder.add("testCreateCollection", trt.testCreateCollection());
        builder.add("testCreateCollectionAllreadyExists", trt.testCreateCollectionAllreadyExists());
        builder.add("testGetAttributes", trt.testGetAttributes());
        builder.add("testAddAttributes", trt.testAddAttributes());
        builder.add("testAddGeoAttributes", trt.testAddGeoAttributes());
        builder.add("testGetGeoAttributes", trt.testGetGeoAttributes());
        builder.add("testChangeSRID", trt.testChangeSRID());
        RecordsRessourceTest rrt = new RecordsRessourceTest();
        builder.add("testCreateSetSimple", rrt.testCreateSetSimple());
        builder.add("testGetSetSimple", rrt.testGetSetSimple());
        builder.add("testCreateSetsSimple", rrt.testCreateSetsSimple());
        builder.add("testGetSetsSimple", rrt.testGetSetsSimple());
        builder.add("testGetSetsLimit", rrt.testGetSetsLimit());
        builder.add("testGetSetsNegativeLimit", rrt.testGetSetsNegativeLimit());
        builder.add("testGetSetsNotExists", rrt.testGetSetsNotExists());
        builder.add("testGetSetsWithExistingInclude",rrt.testGetSetsWithExistingInclude());
        builder.add("testGetSetsWithExistingIncludes",rrt.testGetSetsWithExistingIncludes());
        builder.add("testGetSetsWithNotExistingInclude",rrt.testGetSetsWithNotExistingInclude());
        builder.add("testGetSetsWithOrderDESC",rrt.testGetSetsWithOrderDESC());
        builder.add("testGetSetsWithOrderASC",rrt.testGetSetsWithOrderASC());
        builder.add("testGetSetsWithNotExistingOrder",rrt.testGetSetsWithNotExistingOrder());
        // Tests with filter
        builder.add("testEQFilterFound",rrt.testEQFilterFound());
        builder.add("testEQFilterNotFound",rrt.testEQFilterNotFound());
        builder.add("testEQFilterMissingColumn",rrt.testEQFilterMissingAttribute());
        // Additional tests
        builder.add("testCreateSetUnicode", rrt.testCreateSetUnicode());
        builder.add("testUpdateSetSimple", rrt.testUpdateSetSimple());
        builder.add("testUpdateSetsSimple", rrt.testUpdateSetsSimple());
        builder.add("testDeleteSet", rrt.testDeleteSet());
        builder.add("testDeleteSets", rrt.testDeleteSets());

        builder.add("testDeleteStorage", brt.testDeleteStorage());

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
