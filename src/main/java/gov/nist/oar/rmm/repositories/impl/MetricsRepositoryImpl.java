package gov.nist.oar.rmm.repositories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

import gov.nist.oar.rmm.config.MongoConfig;
import gov.nist.oar.rmm.exceptions.ResourceNotFoundException;
import gov.nist.oar.rmm.repositories.LogRepository;
import gov.nist.oar.rmm.utilities.ProcessRequest;

@Service
public class LogRepositoryImpl implements LogRepository {

    private Logger logger = LoggerFactory.getLogger(LogRepositoryImpl.class);
//    private Bson searchphraseFilter = null;
//    private List<Bson> queryList = new ArrayList<Bson>();
    
    @Autowired
    MongoConfig mconfig;

//    /**
//     * 
//     */
//    @Override
//    public Document findRecord(String ediid) {
//	Pattern legal = Pattern.compile("[^a-z0-9:/-]", Pattern.CASE_INSENSITIVE);
//	Matcher m = legal.matcher(ediid);
//	if (m.find())
//	    throw new IllegalArgumentException("Illegal identifier");
//
//	MongoCollection<Document> recordLogs = mconfig.getrecordLogCollection();
//
//	String useid = ediid;
//
//	logger.debug("Searching for " + ediid + " as " + useid);
//	long count = recordLogs.count(Filters.eq("ediid", useid));
//	if (count == 0 && useid.length() < 30 && !useid.startsWith("ark:")) {
//	    // allow an ediid be an abbreviation of the ARK ID as specified
//	    // by its local portion
//	    // useid = "ark:/" + appconfig.getDefaultNAAN() + "/" + ediid;
//	    logger.debug("Searching for " + ediid + " as " + useid);
//	    count = recordLogs.count(Filters.eq("ediid", useid));
//	}
//	if (count == 0)
//	    throw new ResourceNotFoundException("No record available for given id.");
//
//	return recordLogs.find(Filters.eq("ediid", useid)).first();
//    }


//    @Override
//    public Document findFileInfo(String filePath) {
//
//	MongoCollection<Document> fileLogs = mconfig.getfilesLogCollection();
//	logger.debug("Searching for filePath:" + filePath);
//	long count = fileLogs.count(Filters.eq("filepath", filePath));
//	if (count == 0)
//	    throw new ResourceNotFoundException("No record available for given filePath.");
//
//	return fileLogs.find(Filters.eq("filepath", filePath)).first();
//    }

    /**
     * 
     */
    @Override
    public Document listfiles(MultiValueMap<String, String>  params) {
//	ProcessRequest request = new ProcessRequest();
//	request.parseSearch(params);
//	
//	MongoCollection<Document> fileLogs = mconfig.getfilesLogCollection();
//
//	long count = 0;
//	count = fileLogs.count(request.getFilter());
//
//	AggregateIterable<Document> aggre = null;
//	try {
//	    aggre = fileLogs.aggregate(request.getQueryList());
//	} catch (Exception e) {
//	    logger.error(e.getMessage());
//	}
//
//	Document resultDoc = new Document();
//	resultDoc.put("FilesCount", count);
//	resultDoc.put("PageSize", request.getPageSize());
//	resultDoc.put("FilesData", aggre);
//	return resultDoc;
	return this.processInputAndData(mconfig.getfilesLogCollection(), params, "Files");
    }

    /**
     * 
     */
    @Override
    public Document listBundles(MultiValueMap<String, String> params) {
//	ProcessRequest request = new ProcessRequest();
//	request.parseSearch(params);
//	
//	MongoCollection<Document> bundlePlanLogs = mconfig.getbundleLogCollection();
//
//	long count = 0;
//	count = bundlePlanLogs.count(request.getFilter());
//
//	AggregateIterable<Document> aggre = null;
//	try {
//	    
//	    aggre = bundlePlanLogs.aggregate(request.getQueryList());
//	} catch (Exception e) {
//	    logger.error(e.getMessage());
//	}
//
//	Document resultDoc = new Document();
//	resultDoc.put("BundleCount", count);
//	resultDoc.put("PageSize", request.getPageSize());
//	resultDoc.put("Bundles", aggre);
//	return resultDoc;
	return this.processInputAndData(mconfig.getbundleLogCollection(), params, "Bundle");
    }

    /**
     * 
     */
    @Override
    public Document listBundlePlan(MultiValueMap<String, String> params) {
//	ProcessRequest request = new ProcessRequest();
//	request.parseSearch(params);
//	
//	MongoCollection<Document> bundlePlanLogs = mconfig.getbundlePlanLogCollection();
//
//	long count = 0;
//	count = bundlePlanLogs.count(request.getFilter());
//
//	AggregateIterable<Document> aggre = null;
//	try {
//	   
//	    aggre = bundlePlanLogs.aggregate(request.getQueryList());
//	} catch (Exception e) {
//	    logger.error(e.getMessage());
//	}
//
//	Document resultDoc = new Document();
//	resultDoc.put("BundlePlansCount", count);
//	resultDoc.put("PageSize", request.getPageSize());
//	resultDoc.put("BundlePlans", aggre);
//	return resultDoc;
	return this.processInputAndData(mconfig.getbundlePlanLogCollection(), params, "BundlePlan");
    }

    /**
     * Return bundle plan summary
     */
    @Override
    public Document findBundlePlanSummary(MultiValueMap<String, String> params) {
//	ProcessRequest request = new ProcessRequest();
//	request.parseSearch(params);
//	
//	MongoCollection<Document> bundlePlanLogs = mconfig.getbundlePlanSummarylogsCollection();
//
//	long count = 0;
//	count = bundlePlanLogs.count(request.getFilter());
//
//	AggregateIterable<Document> aggre = null;
//	try {
//	    aggre = bundlePlanLogs.aggregate(request.getQueryList());
//	} catch (Exception e) {
//	    logger.error(e.getMessage());
//	}
//
//	Document resultDoc = new Document();
//	resultDoc.put("BundlePlanSummaryCount", count);
//	resultDoc.put("PageSize", request.getPageSize());
//	resultDoc.put("BundlePlansSummary", aggre);
//	return resultDoc;
	return processInputAndData(mconfig.getbundlePlanSummarylogsCollection(),params, "Bundle Plan Summary ");
    }

    /**
     * 
     */
    
    private Document processInputAndData(MongoCollection<Document> mdCollection,MultiValueMap<String, String> params, String collectionName ) {
	
	ProcessRequest request = new ProcessRequest();
	request.parseSearch(params);

	long count = 0;
	count = mdCollection.count(request.getFilter());

	AggregateIterable<Document> aggre = null;
	try {
	    aggre = mdCollection.aggregate(request.getQueryList());
	} catch (Exception e) {
	    logger.error(e.getMessage());
	}

	Document resultDoc = new Document();
	resultDoc.put(collectionName+"Count", count);
	resultDoc.put("PageSize", request.getPageSize());
	resultDoc.put(collectionName, aggre);
	return resultDoc;
    }
    /**
     * For given recordid fine the distinct requestids to count total number of downloads attempted.
     */
    @Override
    public Document getRecordDownloads(String recordid) {
	
	MongoCollection<Document> bundles = mconfig.getbundleLogCollection();
	List<Bson> queryList = new ArrayList<Bson>();
	queryList.add(Aggregates.match(Filters.in("recordid",recordid)));
	queryList.add(Aggregates.group(recordid,com.mongodb.client.model.Accumulators.addToSet("requestid", "$requestid")));
//	queryList.add(Aggregates.count());
	
	AggregateIterable<Document> aggre = null;
	try {
	    aggre = bundles.aggregate(queryList);
	} catch (Exception e) {
	    logger.error(e.getMessage());
	}
	
	Document resultDoc = new Document();
	resultDoc.put("Record Downloads", aggre);
	return resultDoc;
    }

    
}
