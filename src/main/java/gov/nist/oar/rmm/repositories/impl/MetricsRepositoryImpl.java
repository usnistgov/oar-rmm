package gov.nist.oar.rmm.repositories.impl;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gov.nist.oar.rmm.config.MongoConfig;
import gov.nist.oar.rmm.repositories.MetricsRepository;
import gov.nist.oar.rmm.utilities.ProcessRequest;

@Service
public class MetricsRepositoryImpl implements MetricsRepository {

    private Logger logger = LoggerFactory.getLogger(MetricsRepositoryImpl.class);

    @Autowired
    MongoConfig mconfig;

    /**
     * Get the input requested parameters and process to create Mongodb query,
     * process results and return in desired format
     */

    private Document processInputAndData(MongoCollection<Document> mdCollection, MultiValueMap<String, String> params,
	    String collectionName) {

	if (params.containsKey("include"))
	    params.remove("include");
	if (params.containsKey("exclude"))
	    params.remove("exclude");

	params.add("exclude", "_id,ip_list");
	
	ProcessRequest request = new ProcessRequest();
	request.parseSearch(params);

	long count = 0;
	count = mdCollection.countDocuments(request.getFilter());
	MongoCursor<Document> iter = null;
	AggregateIterable<Document> aggre = null;
	List<Document> batch = new ArrayList<>();
	try {
	    aggre = mdCollection.aggregate(request.getQueryList());
	    iter = aggre.iterator();
	    while (iter.hasNext()) {
	        batch.add(iter.next());
	    }
	} catch (Exception e) {
	    logger.error(e.getMessage());
	}finally {
	    if (iter != null) { iter.close();}
	}

	Document resultDoc = new Document();
	resultDoc.put(collectionName + "Count", count);
	if (collectionName.equalsIgnoreCase("TotalUsers")) {
	    return resultDoc;
	}
	resultDoc.put("PageSize", request.getPageSize());
	resultDoc.put(collectionName, batch);
	return resultDoc;
    }

    /**
     * get the individual files
     */
    @Override
    public Document findFile(String recordid, String filepath, MultiValueMap<String, String> params) {


	if (!recordid.isEmpty() && filepath.isEmpty()) {
	    params.add("ediid", recordid);
	    return this.processInputAndData(mconfig.getfileMetricsCollection(), params, "FilesMetrics");
	} else if (!filepath.isEmpty()) {
	    params.add("filepath", filepath);
	    return this.processInputAndData(mconfig.getfileMetricsCollection(), params, "FilesMetrics");
	}
	return this.processInputAndData(mconfig.getfileMetricsCollection(), params, "FilesMetrics");
    }

    /**
     * Find the record/dataset related information about downloads and number of
     * users
     */
    @Override
    public Document findRecord(String id, MultiValueMap<String, String> params) {
	MongoCollection<Document> recordMetrics = mconfig.getRecordMetricsCollection();
	if (id != null)
	    params.add("ediid", id);
	return this.processInputAndData(recordMetrics, params, "DataSetMetrics");

    }

    /**
     * Get monthly downloads size and number of unique users per repository
     */
    @Override
    public Document findRepo(MultiValueMap<String, String> params) {
	MongoCollection<Document> downloadMetrics = mconfig.getRepoMetricsCollection();
	return this.processInputAndData(downloadMetrics, params, "RepoMetrics");
    }

    /**
     * 
     */
    @Override
    public Document totalUsers(MultiValueMap<String, String> params) {
	MongoCollection<Document> userMetrics = mconfig.getUniqueUsersMetricsCollection();
	return this.processInputAndData(userMetrics, params, "TotalUsers");

    }
}
