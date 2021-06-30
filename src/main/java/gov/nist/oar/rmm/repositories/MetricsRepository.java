package gov.nist.oar.rmm.repositories;

import org.bson.Document;
import org.springframework.util.MultiValueMap;

public interface MetricsRepository {

    /**
     * Search record for given identifier in RecordsLog collection
     * 
     * @param id
     * @return Document in JSON format
     */
    public Document findRecord(String id, MultiValueMap<String, String>  params);

    
    /**
     * Search and sort logs from the filesLogs Collection in the database
     * @return
     */
    public Document findFile(String id, String fileid,  MultiValueMap<String, String> params);
    
    /**
     * GEt the total size download per month
     */
    
    public Document findRepo(MultiValueMap<String, String>  params);
    
    /**
     * Get total unique users
     */
    public Document totalUsers(MultiValueMap<String, String>  params);
    
//  
//  /**
//   * Search file in the Logs collection to get all the logging information
//   * 
//   * @param filePtah
//   * @return Document in JSON format
//   */
//  public Document findFileInfo(String filePath);
//    
//    /**
//     * Return list of unique bundles and sizes
//     */
//    public Document listBundles(MultiValueMap<String, String> params);
//    
//    /**
//     * Get the logs of BundlePlan requests
//     */
//    public Document listBundlePlan(MultiValueMap<String, String> params);
//    
//    /**
//     * Get the list of BundlePlanSummary
//     * @param params
//     * @return
//     */
//    public Document findBundlePlanSummary(MultiValueMap<String, String> params);
    
//    /**
//     * Get the given record related unique downloads.
//     * These are partial or complete downloads
//     */
//    
//    public Document getRecordDownloads(String recordid);
}
