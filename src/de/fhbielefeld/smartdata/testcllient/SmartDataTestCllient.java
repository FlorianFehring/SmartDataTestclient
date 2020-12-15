package de.fhbielefeld.smartdata.testcllient;

import de.fhbielefeld.smartdata.testcllient.rest.StorageRessourceTest;
import de.fhbielefeld.smartdata.testcllient.rest.RecordsRessourceTest;
import de.fhbielefeld.smartdata.testcllient.rest.CollectionRessourceTest;
import de.fhbielefeld.smartdata.testcllient.rest.RecordsPerformanceTest;
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
        builder.add("testCreateSecondStorage", brt.testCreateSecondStorage());
        builder.add("testGetCollectionsNoOne", brt.testGetCollectionsNoOne());

        CollectionRessourceTest trt = new CollectionRessourceTest();
        builder.add("testCreateCollection", trt.testCreateCollection());
        builder.add("testCreateCollectionAllreadyExists", trt.testCreateCollectionAllreadyExists());
        builder.add("testCreateCollectionGivingAutoId",trt.testCreateCollectionGivingAutoId());
        builder.add("testCreateCollectionGivingId",trt.testCreateCollectionGivingId());
        builder.add("testGetAttributes", trt.testGetAttributes());
        builder.add("testGetAttributesNoExSchema", trt.testGetAttributesNoExSchema());
        builder.add("testAddAttributes", trt.testAddAttributes());
        builder.add("testAddGeoAttributes", trt.testAddGeoAttributes());
        builder.add("testGetGeoAttributes", trt.testGetGeoAttributes());
        builder.add("testChangeSRID", trt.testChangeSRID());
        builder.add("testGetAttributesAndCreateTable", trt.testGetAttributesAndCreateTable());
        builder.add("testCreateGeoCollection", trt.testCreateGeoCollection());
        RecordsRessourceTest rrt = new RecordsRessourceTest();
        builder.add("testCreateSetSimple", rrt.testCreateSetSimple());
        builder.add("testGetSetSimple", rrt.testGetSetSimple());
        builder.add("testCreateSetsSimple", rrt.testCreateSetsSimple());
        builder.add("testGetSetsSimple", rrt.testGetSetsSimple());
        builder.add("testReCreateSetSimple",rrt.testReCreateSetSimple());
        builder.add("testGetSetsLimit", rrt.testGetSetsLimit());
        builder.add("testGetSetsNegativeLimit", rrt.testGetSetsNegativeLimit());
        builder.add("testGetSetsNotExists", rrt.testGetSetsNotExists());
        builder.add("testGetSetsWithExistingInclude",rrt.testGetSetsWithExistingInclude());
        builder.add("testGetSetsWithExistingIncludes",rrt.testGetSetsWithExistingIncludes());
        builder.add("testGetSetsWithNotExistingInclude",rrt.testGetSetsWithNotExistingInclude());
        builder.add("testGetSetsWithOrderDESC",rrt.testGetSetsWithOrderDESC());
        builder.add("testGetSetsWithOrderASC",rrt.testGetSetsWithOrderASC());
        builder.add("testGetSetsWithNotExistingOrder",rrt.testGetSetsWithNotExistingOrder());
        // Unicode support tests
        builder.add("testCreateSetUnicode", rrt.testCreateSetUnicode());
        builder.add("testGetSetUnicode", rrt.testGetSetUnicode());
        // Geometry support tests
        builder.add("testCreateGeoSetsSimple", rrt.testCreateGeoSetsSimple());
        builder.add("testGetGeoSetsSimple", rrt.testGetGeoSetsSimple());
        // Tests with filter
        builder.add("testEQFilterString",rrt.testEQFilterString());
        builder.add("testEQFilterFloat",rrt.testEQFilterFloat());
        builder.add("testEQFilterBoolean",rrt.testEQFilterBoolean());
        builder.add("testEQFilterNotFound",rrt.testEQFilterNotFound());
        builder.add("testEQFilterMissingColumn",rrt.testEQFilterMissingAttribute());
        builder.add("testNEQFilterFound",rrt.testNEQFilterFound());
        builder.add("testNEQFilterMissingColumn",rrt.testNEQFilterMissingAttribute());
        //Tests with contain filter
        builder.add("testCSFilterFound",rrt.testCSFilterFound());
        builder.add("testCSFilterNotFound",rrt.testCSFilterNotFound());
        builder.add("testCSFilterMissingColumn",rrt.testCSFilterMissingAttribute());
        builder.add("testNCSFilterFound",rrt.testNCSFilterFound());
        builder.add("testNCSFilterNotFound",rrt.testNCSFilterNotFound());
        builder.add("testNCSFilterMissingColumn",rrt.testNCSFilterMissingAttribute());
        //Tests with starts with filter
        builder.add("testSWFilterFound",rrt.testSWFilterFound());
        builder.add("testSWFilterNotFound",rrt.testSWFilterNotFound());
        builder.add("testSWFilterMissingColumn",rrt.testSWFilterMissingAttribute());
        builder.add("testNSWFilterFound",rrt.testNSWFilterFound());
        builder.add("testNSWFilterNotFound",rrt.testNSWFilterNotFound());
        builder.add("testNSWFilterMissingColumn",rrt.testNSWFilterMissingAttribute());
        //Tests with ends with filter
        builder.add("testEWFilterFound",rrt.testEWFilterFound());
        builder.add("testEWFilterNotFound",rrt.testEWFilterNotFound());
        builder.add("testEWFilterMissingColumn",rrt.testEWFilterMissingAttribute());
        builder.add("testNEWFilterFound",rrt.testNEWFilterFound());
        builder.add("testNEWFilterMissingColumn",rrt.testNEWFilterMissingAttribute());
        //Tests with lower than filter
        builder.add("testLTFilterFound",rrt.testLTFilterFound());
        builder.add("testLTFilterTimestamp",rrt.testLTFilterTimestamp());
        builder.add("testLTFilterNotFound",rrt.testLTFilterNotFound());
        builder.add("testLTFilterMissingColumn",rrt.testLTFilterMissingAttribute());
        builder.add("testNLTFilterFound",rrt.testNLTFilterFound());
        builder.add("testNLTFilterNotFound",rrt.testNLTFilterNotFound());
        builder.add("testNLTFilterMissingColumn",rrt.testNLTFilterMissingAttribute());
        //Tests with lower or equal filter
        builder.add("testLEFilterFound",rrt.testLEFilterFound());
        builder.add("testLEFilterTimestamp",rrt.testLEFilterTimestamp());
        builder.add("testLEFilterNotFound",rrt.testLEFilterNotFound());
        builder.add("testLEFilterMissingColumn",rrt.testLEFilterMissingAttribute());
        builder.add("testNLEFilterFound",rrt.testNLEFilterFound());
        builder.add("testNLEFilterNotFound",rrt.testNLEFilterNotFound());
        builder.add("testNLEFilterMissingColumn",rrt.testNLEFilterMissingAttribute());
        //Tests with lower or equal filter
        builder.add("testGEFilterFound",rrt.testGEFilterFound());
        builder.add("testGEFilterTimestamp",rrt.testGEFilterTimestamp());
        builder.add("testGEFilterNotFound",rrt.testGEFilterNotFound());
        builder.add("testGEFilterMissingColumn",rrt.testGEFilterMissingAttribute());
        builder.add("testNGEFilterFound",rrt.testNGEFilterFound());
        builder.add("testNGEFilterNotFound",rrt.testNGEFilterNotFound());
        builder.add("testNGEFilterMissingColumn",rrt.testNGEFilterMissingAttribute());
        //Tests with greater than filter
        builder.add("testGTFilterFound",rrt.testGTFilterFound());
        builder.add("testGTFilterTimestamp",rrt.testGTFilterTimestamp());
        builder.add("testGTFilterNotFound",rrt.testGTFilterNotFound());
        builder.add("testGTFilterMissingColumn",rrt.testGTFilterMissingAttribute());
        builder.add("testNGTFilterFound",rrt.testNGTFilterFound());
        builder.add("testNGTFilterNotFound",rrt.testNGTFilterNotFound());
        builder.add("testNGTFilterMissingColumn",rrt.testNGTFilterMissingAttribute());
        //Tests with between filter
        builder.add("testBTFilterFound",rrt.testBTFilterFound());
        builder.add("testBTFilterNotFound",rrt.testBTFilterNotFound());
        builder.add("testBTFilterMissingColumn",rrt.testBTFilterMissingAttribute());
        builder.add("testNBTFilterFound",rrt.testNBTFilterFound());
        builder.add("testNBTFilterNotFound",rrt.testNBTFilterNotFound());
        builder.add("testNBTFilterMissingColumn",rrt.testNBTFilterMissingAttribute());
        builder.add("testBTFilterTimestamp",rrt.testBTFilterTimestamp());
        //Tests with in filter
        builder.add("testINFilterFound",rrt.testINFilterFound());
        builder.add("testINFilterNotFound",rrt.testINFilterNotFound());
        builder.add("testINFilterMissingColumn",rrt.testINFilterMissingAttribute());
        builder.add("testNINFilterFound",rrt.testNINFilterFound());
        builder.add("testNINFilterNotFound",rrt.testNINFilterNotFound());
        builder.add("testNINFilterMissingColumn",rrt.testNINFilterMissingAttribute());
        builder.add("testINilterTimestamp",rrt.testINFilterTimestamp());
        builder.add("testINilterString",rrt.testINFilterString());
        builder.add("testINilterDouble",rrt.testINFilterDouble());
        //Tests with is null filter
        builder.add("testISFilterFound",rrt.testISFilterFound());
        builder.add("testISFilterNotFound",rrt.testISFilterNotFound());
        builder.add("testISFilterMissingColumn",rrt.testISFilterMissingAttribute());
        builder.add("testNISFilterFound",rrt.testNISFilterFound());
        builder.add("testNISFilterMissingColumn",rrt.testNISFilterMissingAttribute());
        // Test updateing datasets
        builder.add("testUpdateSetSimple", rrt.testUpdateSetSimple());
        builder.add("testUpdateSetsSimple", rrt.testUpdateSetsSimple());
        // Test copy datasets
        builder.add("testGetSetAndCreateOnSecond",rrt.testGetSetAndCreateOnSecond());
        builder.add("testGetSetsAndCreateOnSecond", rrt.testGetSetsAndCreateOnSecond());
        // Performance mesurement
        RecordsPerformanceTest rpt = new RecordsPerformanceTest();
        builder.add("performanceCreateSetSimple", rpt.performanceCreateSetSimple());
        builder.add("performanceGetSetSimple", rpt.performanceGetSetSimple());
        builder.add("performanceGetSetsSimple", rpt.performanceGetSetsSimple());
        // Tests with geofilter
        // Tests with contains geofilter
        builder.add("testSCOFilterFound",rrt.testSCOFilterFound());
        builder.add("testSCOFilterNotFound",rrt.testSCOFilterNotFound());
        builder.add("testSCOFilterMissingColumn",rrt.testSCOFilterMissingAttribute());
        builder.add("testSCOFilter3DPoint",rrt.testSCOFilter3DPoint());
        builder.add("testSCOFilter2DMultipolygon",rrt.testSCOFilter2DMultipolygon());
        builder.add("testSCOFilter3DMultipolygon",rrt.testSCOFilter3DMultipolygon());
        builder.add("testSCOFilter3DPolygon",rrt.testSCOFilter3DPolygon());
        // Tests with crosses geofilter
        builder.add("testSCRFilterFound",rrt.testSCRFilterFound());
        builder.add("testSCRFilterNotFound",rrt.testSCRFilterNotFound());
        builder.add("testSCRFilterMissingColumn",rrt.testSCRFilterMissingAttribute());
        // Tests with disjoint geofilter
        builder.add("testSDIFilterFound",rrt.testSDIFilterFound());
        builder.add("testSDIFilterNotFound",rrt.testSDIFilterNotFound());
        builder.add("testSDIFilterMissingColumn",rrt.testSDIFilterMissingAttribute());
        // Tests with equals geofilter
        builder.add("testSEQFilterFound",rrt.testSEQFilterFound());
        builder.add("testSEQFilterNotFound",rrt.testSEQFilterNotFound());
        builder.add("testSEQFilterMissingColumn",rrt.testSEQFilterMissingAttribute());
        // Tests with intersects geofilter
        builder.add("testSINFilterFound",rrt.testSINFilterFound());
        builder.add("testSINFilterNotFound",rrt.testSINFilterNotFound());
        builder.add("testSINFilterMissingColumn",rrt.testSINFilterMissingAttribute());
        // Tests with overlaps geofilter
        builder.add("testSOVFilterFound",rrt.testSOVFilterFound());
        builder.add("testSOVFilterNotFound",rrt.testSOVFilterNotFound());
        builder.add("testSOVFilterMissingColumn",rrt.testSOVFilterMissingAttribute());
        // Tests with touches geofilter
        builder.add("testSTOFilterFound",rrt.testSTOFilterFound());
        builder.add("testSTOFilterNotFound",rrt.testSTOFilterNotFound());
        builder.add("testSTOFilterMissingColumn",rrt.testSTOFilterMissingAttribute());
        // Test deletion
        builder.add("testDeleteSet", rrt.testDeleteSet());
        builder.add("testDeleteSets", rrt.testDeleteSets());

        builder.add("testDeleteStorage", brt.testDeleteStorage());
        builder.add("testDeleteSecondStorage", brt.testDeleteSecondStorage());

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
