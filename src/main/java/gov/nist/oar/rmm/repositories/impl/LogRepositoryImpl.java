package gov.nist.oar.rmm.repositories.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
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

    /**
     * 
     */
    @Override
    public Document findRecord(String ediid) {
	Pattern legal = Pattern.compile("[^a-z0-9:/-]", Pattern.CASE_INSENSITIVE);
	Matcher m = legal.matcher(ediid);
	if (m.find())
	    throw new IllegalArgumentException("Illegal identifier");

	MongoCollection<Document> recordLogs = mconfig.getrecordLogCollection();

	String useid = ediid;

	logger.debug("Searching for " + ediid + " as " + useid);
	long count = recordLogs.count(Filters.eq("ediid", useid));
	if (count == 0 && useid.length() < 30 && !useid.startsWith("ark:")) {
	    // allow an ediid be an abbreviation of the ARK ID as specified
	    // by its local portion
	    // useid = "ark:/" + appconfig.getDefaultNAAN() + "/" + ediid;
	    logger.debug("Searching for " + ediid + " as " + useid);
	    count = recordLogs.count(Filters.eq("ediid", useid));
	}
	if (count == 0)
	    throw new ResourceNotFoundException("No record available for given id.");

	return recordLogs.find(Filters.eq("ediid", useid)).first();
    }

    /**
     * 
     */
    @Override
    public Document findFileInfo(String filePath) {

	MongoCollection<Document> fileLogs = mconfig.getfilesLogCollection();
	logger.debug("Searching for filePath:" + filePath);
	long count = fileLogs.count(Filters.eq("filepath", filePath));
	if (count == 0)
	    throw new ResourceNotFoundException("No record available for given filePath.");

	return fileLogs.find(Filters.eq("filepath", filePath)).first();
    }

    /**
     * 
     */
    @Override
    public Document findSortPage(MultiValueMap<String, String>  params) {
	ProcessRequest request = new ProcessRequest();
	request.parseSearch(params);
	
	MongoCollection<Document> fileLogs = mconfig.getfilesLogCollection();

	long count = 0;
	count = fileLogs.count(request.getFilter());

	AggregateIterable<Document> aggre = null;
	try {
	    aggre = fileLogs.aggregate(request.getQueryList());
	} catch (Exception e) {
	    logger.error(e.getMessage());
	}

	Document resultDoc = new Document();
	resultDoc.put("LogResultsCount", count);
	resultDoc.put("PageSize", request.getPageSize());
	resultDoc.put("LogResultData", aggre);
	return resultDoc;
    }

    /**
     * 
     */
    @Override
    public Document listBundles() {
	MongoCollection<Document> bundleLogs = mconfig.getbundleLogCollection();
	logger.debug("Get Unique bundles and sizes." );
	long count = bundleLogs.count();
	if (count == 0)
	    throw new ResourceNotFoundException("No record available for given filePath.");

	return bundleLogs.find().first();
    }

    /**
     * 
     */
    @Override
    public Document listBundlePlan(MultiValueMap<String, String> params) {
	ProcessRequest request = new ProcessRequest();
	request.parseSearch(params);
	
	MongoCollection<Document> bundlePlanLogs = mconfig.getbundlePlanLogCollection();

	long count = 0;
	count = bundlePlanLogs.count(request.getFilter());

	AggregateIterable<Document> aggre = null;
	try {
	    aggre = bundlePlanLogs.aggregate(request.getQueryList());
	} catch (Exception e) {
	    logger.error(e.getMessage());
	}

	Document resultDoc = new Document();
	resultDoc.put("BundlePlansCount", count);
	resultDoc.put("PageSize", request.getPageSize());
	resultDoc.put("BundlePlansLogs", aggre);
	return resultDoc;
    }

    
}
