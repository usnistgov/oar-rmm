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

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import gov.nist.oar.rmm.config.MongoConfig;
import gov.nist.oar.rmm.exceptions.ResourceNotFoundException;
import gov.nist.oar.rmm.repositories.CustomRepository;
import gov.nist.oar.rmm.utilities.ProcessRequest;

/**
 * Custom Repository interface implementation
 * @author Deoyani Nandrekar-Heinis
 *
 */
@Service
public class CustomRepositoryImpl implements CustomRepository {

	private Logger logger = LoggerFactory.getLogger(CustomRepositoryImpl.class);
	@Autowired
	MongoConfig mconfig;
	
	/* (non-Javadoc)
	 * @see gov.nist.oar.rmm.repositories.RecordRepository#find()
	 * Search with given criteria
	 */
	@Override
	public Document find(Map<String,String> params){	
		
		ProcessRequest request  = new ProcessRequest();
		request.parseSearch(params);
		MongoCollection<Document> mcollection = mconfig.getRecordCollection();
		long count  = mcollection.count(request.getFilter());
		logger.info("Count :"+count);
		AggregateIterable<Document> aggre = null;
		try{
		 aggre = mcollection.aggregate(request.getQueryList());
		}
		catch(Exception e){
			logger.info(e.getMessage());
		}
		Document resultDoc = new Document();
		resultDoc.put("ResultCount", count);
		resultDoc.put("PageSize", request.getPageSize());
		resultDoc.put("ResultData",aggre);
		return resultDoc;
		
	}

	/* (non-Javadoc)
	 * @see gov.nist.oar.rmm.repositories.CustomRepository#findtaxonomy(java.util.Map)
	 * Find Taxonomy 
	 */
	@Override
	public List<Document> findtaxonomy(Map<String, String> param) {
		MongoCollection<Document> mcollection = mconfig.getTaxonomyCollection();
		ProcessRequest request  = new ProcessRequest();
		if(request.parseTaxonomy(param) == null)
			return mcollection.find().into(new ArrayList<Document>());
		else
			return mcollection.find(request.parseTaxonomy(param)).into(new ArrayList<Document>());
	}

	/* 
	 * Find Resource apis
	 */
	@Override
	public List<Document> findResourceApis() {
		
		MongoCollection<Document> mcollection = mconfig.getResourceApiCollection();
		return mcollection.find().into(new ArrayList<Document>());
	}

	/* (non-Javadoc)
	 * @see gov.nist.oar.rmm.repositories.CustomRepository#findRecord(java.lang.String)
	 */
	@Override
	public Document findRecord(String ediid) {
		
		Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(ediid);
		if(m.find()) 
			throw new IllegalArgumentException("check input parameters.");
		
		MongoCollection<Document> mcollection = mconfig.getRecordCollection();
		
		
		long count  = mcollection.count(Filters.eq("ediid",ediid));
		if(count == 0) {
			//return new Document("Message", "No record available for given id.");
			throw new ResourceNotFoundException("No record available for given id.");
		}
		else
		return mcollection.find(Filters.eq("ediid",ediid)).first();
		
	}

	/* (non-Javadoc)
	 * @see gov.nist.oar.rmm.repositories.CustomRepository#findFieldnames()
	 */
	@Override
	public List<Document> findFieldnames() {
		
		MongoCollection<Document> mcollection = mconfig.getRecordFieldsCollection();
		return mcollection.find().into(new ArrayList<Document>());
		
	}
	
	///This is extra function

	public List<Document> findOrig(Map<String,String> params) {		
		ProcessRequest request  = new ProcessRequest();
		request.parseSearch(params);
		
		MongoCollection<Document> mcollection = mconfig.getRecordCollection();
		FindIterable<Document> findResults;
		

//		 Bson filter= Filters.and(Filters.regex("identifier", "EBC9DB05EDEA5B0EE043065706812DF81"), Filters.regex("title", "Enterprise Data Inventory"));
//		 Bson filter2 = ps.getFilter();
//		 if(filter.equals(ps.getFilter())) logger.info("Test if equal");
		 		//findResults= mcollection.find(Filters.text("SRD"));
		
//		if(ps.getFilter() != null )
//			//findResults = mcollection.find(ps.getFilter()).projection(ps.getProjections());
//			findResults = mcollection.find(ps.getFilter());
//		else
//			//findResults = mcollection.find().projection(ps.getProjections());
//			findResults = mcollection.find();
//		
//		long count  = mcollection.count(ps.getFilter());
//		logger.info("Count"+count);
//		Document resultDoc = new Document();
//		resultDoc.put("ResultCount", count);
//		resultDoc.put("ResultData", findResults);
//		logger.info("Results :::: "+resultDoc.toJson());
//		int test =(p.getPageNumber() > 0 ? ((p.getPageNumber()-1)*p.getPageSize()) : 0);
//		logger.info("Pagination::: pagenumber"+test+"::pagesize::"+p.getPageSize());
//		
//	
//		
//		return  findResults.skip(p.getPageNumber() > 0 ? ((p.getPageNumber()-1)*p.getPageSize()) : 0).limit(p.getPageSize())
//				.sort(ps.getSorts())
//				.into(new ArrayList<Document>());
		
		return mcollection.aggregate(request.getQueryList())
				.into(new ArrayList<Document>());
				
	}

	/* (non-Javadoc)
	 * @see gov.nist.oar.rmm.repositories.CustomRepository#find(java.util.Map, org.springframework.data.domain.Pageable)
	 */
	@Override
	public List<Document> find(Map<String, String> param, Pageable p) {
			return null;
	}

	/* (non-Javadoc)
	 * @see gov.nist.oar.rmm.repositories.CustomRepository#findtaxonomy()
	 * To get all the taxonomy data
	 */
	@Override
	public List<Document> findtaxonomy() {
		MongoCollection<Document> mcollection = mconfig.getTaxonomyCollection();
		return mcollection.find().into(new ArrayList<Document>());
	}

}
