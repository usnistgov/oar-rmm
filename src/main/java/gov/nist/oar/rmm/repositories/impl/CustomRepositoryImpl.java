/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 * @author: Deoyani Nandrekar-Heinis
 */
package gov.nist.oar.rmm.repositories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.mongodb.client.model.Filters;

import gov.nist.oar.rmm.config.AppConfig;
import gov.nist.oar.rmm.config.MongoConfig;
import gov.nist.oar.rmm.exceptions.ResourceNotFoundException;
import gov.nist.oar.rmm.repositories.CustomRepository;
import gov.nist.oar.rmm.utilities.ProcessRequest;

/**
 * Custom Repository interface implementation
 * 
 * @author Deoyani Nandrekar-Heinis
 *
 */
@Service
public class CustomRepositoryImpl implements CustomRepository {

	private Logger logger = LoggerFactory.getLogger(CustomRepositoryImpl.class);
	@Autowired
	MongoConfig mconfig;

	@Autowired
	AppConfig appconfig;

	/**
	 * Find the record with given search parameters. Returns JSON document of
	 * results.
	 */
	@Override
	public Document find(MultiValueMap<String, String> params) {
    	ProcessRequest request = new ProcessRequest();
		request.parseSearch(params);
		MongoCollection<Document> mcollection = mconfig.getRecordCollection();
		long count =0;
//		count = mcollection.count(request.getFilter());
		if(request.getFilters().size() == 0)
			count = mcollection.count(null);
		for(int i=0; i<request.getFilters().size(); i++) {
			count += mcollection.count(request.getFilters().get(i));
		}
		//logger.info("Result Count :" + count);
		AggregateIterable<Document> aggre = null;
		try {
			aggre = mcollection.aggregate(request.getQueryList());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		Document resultDoc = new Document();
		resultDoc.put("ResultCount", count);
		resultDoc.put("PageSize", request.getPageSize());
		resultDoc.put("ResultData", aggre);
		return resultDoc;
	}

	/**
	 * Get Taxonomy List in the form of JSON document
	 */
	@Override
	public List<Document> findtaxonomy(Map<String, String> param) {
		MongoCollection<Document> mcollection = mconfig.getTaxonomyCollection();
		ProcessRequest request = new ProcessRequest();
		if (request.parseTaxonomy(param) == null)
			return mcollection.find().into(new ArrayList<Document>());
		else
			return mcollection.find(request.parseTaxonomy(param)).into(new ArrayList<Document>());
	}

	/**
	 * Find resources APIs in the form of list of documents.
	 */
	@Override
	public List<Document> findResourceApis() {

		MongoCollection<Document> mcollection = mconfig.getResourceApiCollection();
		return mcollection.find().into(new ArrayList<Document>());
	}

	/**
	 * Search and return document for given identifier.
	 */
	@Override
	public Document findRecord(String ediid) {

		Pattern legal = Pattern.compile("[^a-z0-9:/-]", Pattern.CASE_INSENSITIVE);
		Matcher m = legal.matcher(ediid);
		if (m.find())
			throw new IllegalArgumentException("Illegal identifier");

		MongoCollection<Document> mcollection = mconfig.getRecordCollection();

		String useid = ediid;

		logger.debug("Searching for " + ediid + " as " + useid);
		long count = mcollection.count(Filters.eq("ediid", useid));
		if (count == 0 && useid.length() < 30 && !useid.startsWith("ark:")) {
			// allow an ediid be an abbreviation of the ARK ID as specified
			// by its local portion
			useid = "ark:/" + appconfig.getDefaultNAAN() + "/" + ediid;
			logger.debug("Searching for " + ediid + " as " + useid);
			count = mcollection.count(Filters.eq("ediid", useid));
		}
		if (count == 0) {
			// return new Document("Message", "No record available for given id.");
			throw new ResourceNotFoundException("No record available for given id.");
		}

		return mcollection.find(Filters.eq("ediid", useid)).first();

	}

	/**
	 * Return list of Fields in the any document or record
	 */
	@Override
	public List<Document> findFieldnames() {

		MongoCollection<Document> mcollection = mconfig.getRecordFieldsCollection();
		return mcollection.find().into(new ArrayList<Document>());

	}

//	/**
//	 * Get the record with search parameters
//	 * 
//	 * @param params
//	 * @return List of documents in the form of JSON
//	 */
//	public List<Document> findOrig(Map<String, String> params) {
//		ProcessRequest request = new ProcessRequest();
//		request.parseSearch(params);
//		MongoCollection<Document> mcollection = mconfig.getRecordCollection();
////		FindIterable<Document> findResults;
//		return mcollection.aggregate(request.getQueryList()).into(new ArrayList<Document>());
//
//	}

	/**
	 * 
	 */
	@Override
	public List<Document> find(Map<String, String> param, Pageable p) {
		return null;
	}

	/***
	 * Get List of Taxonomy terms in the form of flat JSON document
	 */
	@Override
	public List<Document> findtaxonomy() {
		MongoCollection<Document> mcollection = mconfig.getTaxonomyCollection();
		return mcollection.find().into(new ArrayList<Document>());
	}

}
