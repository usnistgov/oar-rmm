package gov.nist.oar.rmm.repositories;

import org.bson.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface VersionReleasesetsRepository {

    /**
     * Search Versions dataset to get version of the record
     * This can be queried using key,value pairs as parameters
     * @param param key,value parameters
     * @param p pagination
     * @return
     */
    public Document findVersion(MultiValueMap<String, String> param, Pageable p);
        
    /**
     * Search Releasesets dataset using key,value pair where key is any field in the Releasets metadata schema
     * @param param
     * @param p
     * @return
     */
    public Document findReleaseset(MultiValueMap<String, String> param, Pageable p);
    
    /**
     * Find the document from Versions dataset for given version identifier
     * @param id version identifier
     * @return
     */
    public Document findVersion(String id);
    
    /**
     * Find the document from the ReleaseSets for given releasesets identifier
     * 
     * @param id
     * @return
     */
    public Document findReleaseset(String id);
}