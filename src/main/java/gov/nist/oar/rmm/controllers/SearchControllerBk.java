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
//package gov.nist.oar.rmm.controllers;
//
//import java.util.List;
//
//import org.bson.Document;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.mongodb.MongoClient;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//
//import gov.nist.oar.rmm.repositories.CustomRepository;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import javax.annotation.PostConstruct;
//
//@RestController
//@Api(value = "Api endpoints to search EDI/PDL data", tags = "Search API")
//public class SearchControllerBk {
//	
//@Autowired
//MongoClient mongoClient;
//
//
//
//	@ApiImplicitParams({
//        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
//                value = "Results page you want to retrieve (0..N)"),
//        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
//                value = "Number of records per page."),
//        @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
//                value = "Sorting criteria in the format: property(,asc|desc). " +
//                        "Default sort order is ascending. " +
//                        "Multiple sort criteria are supported.")
//    })
//	@ApiOperation(value = "Get complete data.json.",nickname = "PDL",
//				  notes = "This will return the high level data.json fields, dataset is part of it.")
//	@RequestMapping(value = {"/recordsall"}, method = RequestMethod.GET, produces="application/json")
//
//	public String searchall(){
//		try{
//			MongoDatabase database = mongoClient.getDatabase("TestDB");
//			MongoCollection<Document> collection = database.getCollection("record");
//			Document myDoc = collection.find().first();
//			return myDoc.toJson();
//		}catch(Exception e){
//			throw e;
//		}
//	}
//	
//	@ApiOperation(value = "Get complete data.json.",nickname = "PDL",
//			  notes = "This will return the high level data.json fields, dataset is part of it.")
//@RequestMapping(value = {"/records"}, method = RequestMethod.GET, produces="application/json")
//
//public String search(@RequestParam String searchparam,@RequestParam String... exclude ){
//	try{
//		MongoDatabase database = mongoClient.getDatabase("TestDB");
//		MongoCollection<Document> collection = database.getCollection("record");
//		Document myDoc = collection.find().first();
//		return myDoc.toJson();
//	}catch(Exception e){
//		throw e;
//	}
//}
//	
//	@Autowired 
//	CustomRepository repos;
//	
////	
////	@RequestMapping(value = {"/reporecord"}, method = RequestMethod.GET, produces="application/json")
////
////	public Document repoSearch(){
////		return repos.testfindOne();
////	}
//	
//	@RequestMapping(value = {"/reporecord"}, method = RequestMethod.GET, produces="application/json")
//
//	public List<Document> findExclude(String... fields ){
//			return repos.findExclude(fields);
//	}
//	
//	@RequestMapping(value = {"/taxonomy"}, method = RequestMethod.GET, produces="application/json")
//
//	public List<Document> serachTaxonoy(String... fields ){
//			return repos.findExclude(fields);
//	}
//	
//	
////	@Autowired
////	RepositoryService repoService;
////	
////	@RequestMapping(value = {"/repocount"}, method = RequestMethod.GET, produces="application/json")
////
////	public long testSearch(){
////		return repoService.getCount();
////	}
//}
