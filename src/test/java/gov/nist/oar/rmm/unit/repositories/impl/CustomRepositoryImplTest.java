///**
// * This software was developed at the National Institute of Standards and Technology by employees of
// * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
// * of the United States Code this software is not subject to copyright protection and is in the
// * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
// * use by other parties, and makes no guarantees, expressed or implied, about its quality,
// * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
// * used. This software can be redistributed and/or modified freely provided that any derivative
// * works bear some notice that they are derived from it, and any modified versions bear some notice
// * that they have been modified.
// * @author: Deoyani Nandrekar-Heinis
// */
//package gov.nist.oar.rmm.unit.repositories.impl;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.bson.Document;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import com.mongodb.client.FindIterable;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.model.Filters;
//
//import ch.qos.logback.core.filter.Filter;
//import gov.nist.oar.rmm.config.MongoConfig;
//import gov.nist.oar.rmm.repositories.CustomRepository;
//import gov.nist.oar.rmm.unit.configuration.TestMongo;
//import gov.nist.oar.rmm.unit.repositories.CustomRepositoryTest;
//import gov.nist.oar.rmm.utilities.ProcessRequest;
//
//@Service
//public class CustomRepositoryImplTest implements CustomRepositoryTest {
//
//	private Logger logger = LoggerFactory.getLogger(CustomRepositoryImplTest.class);
//	TestMongo mconfig = new TestMongo();
//	
//   @Override
//	public Document find(Map<String,String> params) {		
//		ProcessRequest request  = new ProcessRequest();
//		request.parseSearch(params);
//		MongoCollection<Document> mcollection = mconfig.getRecordCollection();
//		long count  = mcollection.count(request.getFilter());
//		logger.info("Count :"+count);
//		Document resultDoc = new Document();
//		resultDoc.put("ResultCount", count);
//		resultDoc.put("PageSize", request.getPageSize());
//		resultDoc.put("ResultData",mcollection.aggregate(request.getQueryList()));
//		return resultDoc;
//	}
//
//	@Override
//	public List<Document> findtaxonomy(Map<String, String> param) {
//		MongoCollection<Document> mcollection = mconfig.getTaxonomyCollection();
//		ProcessRequest request  = new ProcessRequest();
//		if(request.parseTaxonomy(param) == null)
//			return mcollection.find().into(new ArrayList<Document>());
//		else
//			return mcollection.find(request.parseTaxonomy(param)).into(new ArrayList<Document>());
//	}
//
//	@Override
//	public List<Document> findResourceApis() {
//		
//		MongoCollection<Document> mcollection = mconfig.getResourceApiCollection();
//		return mcollection.find().into(new ArrayList<Document>());
//	}
//
//	@Override
//	public Document findRecord(String ediid) {
//		
//		MongoCollection<Document> mcollection = mconfig.getRecordCollection();
//		
//		return mcollection.find(Filters.eq("ediid",ediid)).first();
//		
//	}
//
//	@Override
//	public List<Document> findFieldnames() {
//		
//		MongoCollection<Document> mcollection = mconfig.getRecordFieldsCollection();
//		return mcollection.find().into(new ArrayList<Document>());
//		
//	}
//	
//	public List<Document> findOrig(Map<String,String> params) {		
//		ProcessRequest request  = new ProcessRequest();
//		request.parseSearch(params);
//		
//		MongoCollection<Document> mcollection = mconfig.getRecordCollection();
//		FindIterable<Document> findResults;
//		
//		return mcollection.aggregate(request.getQueryList())
//				.into(new ArrayList<Document>());
//				
//	}
//
//	@Override
//	public List<Document> find(Map<String, String> param, Pageable p) {
//			return null;
//	}
//
//	@Override
//	public List<Document> findtaxonomy() {
//		MongoCollection<Document> mcollection = mconfig.getTaxonomyCollection();
//		return mcollection.find().into(new ArrayList<Document>());
//	}
//
//}
