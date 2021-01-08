package gov.nist.oar.rmm.repositories;

import org.bson.Document;
import org.springframework.util.MultiValueMap;

public interface LogRepository {

    /**
     * Search record for given identifier in RecordsLog collection
     * 
     * @param id
     * @return Document in JSON format
     */
//    public Document findRecord(String id);
    
    /**
     * Search file in the Logs collection to get all the logging information
     * 
     * @param filePtah
     * @return Document in JSON format
     */
    public Document findFileInfo(String filePath);
    
    /**
     * Search and sort logs from the filesLogs Collection in the database
     * @return
     */
    public Document findSortPage(MultiValueMap<String, String> params);
    
    /**
     * Return list of unique bundles and sizes
     */
    public Document listBundles();
    
    /**
     * Get the logs of BundlePlan requests
     */
    public Document listBundlePlan(MultiValueMap<String, String> params);
    
    /**
     * Get the list of BundlePlanSummary
     * @param params
     * @return
     */
    public Document findBundlePlanSummary(MultiValueMap<String, String> params);
}
