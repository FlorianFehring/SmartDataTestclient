package de.fhbielefeld.smartdata.testcllient.rest;

/**
 * Helper object for storing data for a thread
 *
 * @author Florian Fehring
 */
public class RecordsCreateThreadData {

    private String collection;
    private String storage;
    private int createRuns;

    public RecordsCreateThreadData(String collection, String storage, int createRuns) {
        this.collection = collection;
        this.storage = storage;
        this.createRuns = createRuns;
    }

    public String getCollection() {
        return collection;
    }

    public String getStorage() {
        return storage;
    }

    public int getCreateRuns() {
        return createRuns;
    }
    
    
}
