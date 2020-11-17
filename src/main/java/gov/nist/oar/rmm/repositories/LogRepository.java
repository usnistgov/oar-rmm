package gov.nist.oar.rmm.repositories;

import org.bson.Document;

public interface LogRepository {

    /**
     * Search record for given identifier in Logs collection
     * 
     * @param id
     * @return Document in JSON format
     */
    public Document findRecord(String id);

    /**
     * Search file in the Logs collection to get all the logging information
     * 
     * @param filePtah
     * @return Document in JSON format
     */
    public Document findFileInfo(String filePath);

}
