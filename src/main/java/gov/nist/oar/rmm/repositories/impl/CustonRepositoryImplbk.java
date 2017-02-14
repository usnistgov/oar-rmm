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
//package gov.nist.oar.rmm.repositories.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.PostConstruct;
//
//import org.bson.Document;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.mongodb.MongoClient;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.model.Projections;
//
//import gov.nist.oar.rmm.config.MongoConfig;
//import gov.nist.oar.rmm.repositories.CustomRepository;
//
//@Service
//public class CustonRepositoryImplbk implements CustomRepository {
//
//	
//
//	
//	@Autowired
//	MongoConfig mconfig;
//	
//	/* (non-Javadoc)
//	 * @see gov.nist.oar.rmm.repositories.RecordRepository#findOne()
//	 */
//	@Override
//	public Document testfindOne() {
//		return mconfig.getRecordCollection().find().projection(Projections.exclude("license","publisher")).first();	
//	}
//
//	@Override
//	public List<Document> findExclude(String... args) {
//		
//		return (List<Document>)mconfig.getRecordCollection().find().projection(Projections.exclude(args)).into(new ArrayList<Document>());	
//	}
//
//	/* (non-Javadoc)
//	 * @see gov.nist.oar.rmm.repositories.RecordRepositoryCustom#findAll()
//	 */
//	@Override
//	public List<Document> findAll() {
//		return (List<Document>)mconfig.getRecordCollection().find().into(new ArrayList<Document>());	
//
//	}
//
//	/* (non-Javadoc)
//	 * @see gov.nist.oar.rmm.repositories.RecordRepositoryCustom#findInclude(java.lang.String[])
//	 */
//	@Override
//	public List<Document> findInclude(String... fields) {
//		return (List<Document>)mconfig.getRecordCollection().find().projection(Projections.include(fields)).into(new ArrayList<Document>());	
//
//	}
//	
//	
//}
