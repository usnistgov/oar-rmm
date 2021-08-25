package gov.nist.oar.rmm.repositories;

import org.bson.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface VersionReleasesetsRepository {

    
    public Document findVersion(MultiValueMap<String, String> param, Pageable p);
        
    public Document findReleaseset(MultiValueMap<String, String> param, Pageable p);
    
    public Document findVersion(String id);
    
    public Document findReleaseset(String id);
}