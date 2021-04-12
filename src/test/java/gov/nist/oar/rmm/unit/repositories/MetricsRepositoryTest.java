package gov.nist.oar.rmm.unit.repositories;

import org.bson.Document;
import org.springframework.util.MultiValueMap;

public interface MetricsRepositoryTest {

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
    
    
}
