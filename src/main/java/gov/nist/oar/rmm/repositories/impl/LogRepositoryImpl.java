package gov.nist.oar.rmm.repositories.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import gov.nist.oar.rmm.config.MongoConfig;
import gov.nist.oar.rmm.exceptions.ResourceNotFoundException;
import gov.nist.oar.rmm.repositories.LogRepository;

@Service
public class LogRepositoryImpl implements LogRepository {

    private Logger logger = LoggerFactory.getLogger(LogRepositoryImpl.class);

    @Autowired
    MongoConfig mconfig;

    @Override
    public Document findRecord(String ediid) {
	Pattern legal = Pattern.compile("[^a-z0-9:/-]", Pattern.CASE_INSENSITIVE);
	Matcher m = legal.matcher(ediid);
	if (m.find())
	    throw new IllegalArgumentException("Illegal identifier");

	MongoCollection<Document> mcollection = mconfig.getLogCollection();

	String useid = ediid;

	logger.debug("Searching for " + ediid + " as " + useid);
	long count = mcollection.count(Filters.eq("ediid", useid));
	if (count == 0 && useid.length() < 30 && !useid.startsWith("ark:")) {
	    // allow an ediid be an abbreviation of the ARK ID as specified
	    // by its local portion
	    // useid = "ark:/" + appconfig.getDefaultNAAN() + "/" + ediid;
	    logger.debug("Searching for " + ediid + " as " + useid);
	    count = mcollection.count(Filters.eq("ediid", useid));
	}
	if (count == 0)
	    throw new ResourceNotFoundException("No record available for given id.");

	return mcollection.find(Filters.eq("ediid", useid)).first();
    }

    @Override
    public Document findFileInfo(String filePath) {

	MongoCollection<Document> mcollection = mconfig.getLogCollection();
	logger.debug("Searching for filePath:" + filePath);
	long count = mcollection.count(Filters.eq("filePath", filePath));
	if (count == 0)
	    throw new ResourceNotFoundException("No record available for given filePath.");

	return mcollection.find(Filters.eq("filePath", filePath)).first();
    }

}
