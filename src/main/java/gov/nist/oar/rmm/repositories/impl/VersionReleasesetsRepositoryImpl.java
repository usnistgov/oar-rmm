package gov.nist.oar.rmm.repositories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import gov.nist.oar.rmm.config.AppConfig;
import gov.nist.oar.rmm.config.MongoConfig;
import gov.nist.oar.rmm.exceptions.ResourceNotFoundException;
import gov.nist.oar.rmm.repositories.VersionReleasesetsRepository;
import gov.nist.oar.rmm.utilities.ProcessRequest;

@Service
public class VersionReleasesetsRepositoryImpl implements VersionReleasesetsRepository {

    private Logger logger = LoggerFactory.getLogger(VersionReleasesetsRepositoryImpl.class);

    @Autowired
    MongoConfig mconfig;

    @Autowired
    AppConfig appconfig;

    /**
     * Search Versions dataset to get version of the record This can be queried
     * using key,value pairs as parameters
     * 
     * @param param key,value parameters
     * @param p     pagination
     * @return
     */
    @Override
    public Document findVersion(MultiValueMap<String, String> param, Pageable p) {

        return this.queryResults(param, p, mconfig.getVersionsName());
    }

    /**
     * Search Releasesets dataset using key,value pair where key is any field in the
     * Releasets metadata schema
     * 
     * @param param
     * @param p
     * @return
     */
    @Override
    public Document findReleaseset(MultiValueMap<String, String> param, Pageable p) {
        return this.queryResults(param, p, mconfig.getReleaseSetsName());
    }

    /**
     * This is a common utility used by the findVersions and findReleasesets. This
     * helps query given collection with specified criteria.
     * 
     * @param param          key,value pairs
     * @param p              pagination information
     * @param collectionName Name of the dataset to search from
     * @return
     */
    private Document queryResults(MultiValueMap<String, String> param, Pageable p, String collectionName) {
        ProcessRequest request = new ProcessRequest();
        request.parseSearch(param);
        MongoCollection<Document> mcollection = null;
        if (mconfig.getVersionsName().equalsIgnoreCase(collectionName))
            mcollection = mconfig.getVersionsCollection();
        else if (mconfig.getReleaseSetsName().equalsIgnoreCase(collectionName))
            mcollection = mconfig.getReleaseSetsCollection();

        long count = 0;
        MongoCursor<Document> iter = null;
        List<Document> batch = new ArrayList<>();
        count = mcollection.countDocuments(request.getFilter());

        AggregateIterable<Document> aggre = null;
        try {
            aggre = mcollection.aggregate(request.getQueryList());
            iter = aggre.iterator();
    	    
    	    while (iter.hasNext()) {
    	        batch.add(iter.next());
    	    }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        Document resultDoc = new Document();
        resultDoc.put(collectionName + " Count", count);
        resultDoc.put("PageSize", request.getPageSize());
        resultDoc.put(collectionName + " data", batch);
        return resultDoc;
    }

    /**
     * Find the document from Versions dataset for given version identifier
     * 
     * @param id version identifier
     * @return
     */
    @Override
    public Document findVersion(String ediid) {
        return queryResult(ediid, mconfig.getVersionsName());
    }

    /**
     * Find the document from the ReleaseSets for given releasesets identifier
     * 
     * @param id
     * @return
     */
    @Override
    public Document findReleaseset(String id) {
        return queryResult(id, mconfig.getReleaseSetsName());
    }

    /**
     * The common query processor and executor for both versions and releasesets
     * 
     * @param id             requested identifier
     * @param collectionName Name of the collection to be queried
     * @return
     */
    private Document queryResult(String id, String collectionName) {

        Pattern legal = Pattern.compile("[^a-z0-9:/-][.]", Pattern.CASE_INSENSITIVE);
        Matcher m = legal.matcher(id);
        if (m.find())
            throw new IllegalArgumentException("Illegal identifier");

        MongoCollection<Document> mcollection = null;
        if ("versions".equalsIgnoreCase(collectionName))
            mcollection = mconfig.getVersionsCollection();
        else if ("releaseSets".equalsIgnoreCase(collectionName))
            mcollection = mconfig.getReleaseSetsCollection();
        String useid = id;

        logger.debug("Searching for " + id + " as " + useid);
        long count = mcollection.countDocuments(Filters.eq("@id", useid));
        if (count == 0 && useid.length() < 30 && !useid.startsWith("ark:")) {
            // allow an id be an abbreviation of the ARK ID as specified
            // by its local portion
            useid = "ark:/" + appconfig.getDefaultNAAN() + "/" + id;
            logger.debug("Searching for " + id + " as " + useid);
            count = mcollection.countDocuments(Filters.eq("@id", useid));
        }
        if (count == 0) {
            // return new Document("Message", "No record available for given id.");
            throw new ResourceNotFoundException("No record available for given id.");
        }

        return mcollection.find(Filters.eq("@id", useid)).first();
    }

}
