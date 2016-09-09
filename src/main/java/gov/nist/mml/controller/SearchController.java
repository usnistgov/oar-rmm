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
package gov.nist.mml.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import gov.nist.mml.repositories.RecordRepository;
import gov.nist.mml.utilities.ProcessRequest;
import gov.nist.mml.domain.catalog;
import gov.nist.mml.domain.Record;
import gov.nist.mml.domain.nestedpod.ContactPoint;
import gov.nist.mml.domain.nestedpod.Distribution;
import gov.nist.mml.domain.nestedpod.Publisher;
import gov.nist.mml.exception.ResourceNotFoundException;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.query.Query.query;
import gov.nist.mml.repositories.CatalogRepository;;
@RestController
@Api(value = "Test api for searching EDI/PDL data", tags = "Search API")
public class SearchController {
	
	private Logger logger = LoggerFactory.getLogger(SearchController.class);

	@Autowired
    private RecordRepository RecordRepository;
	
	@Autowired
    private CatalogRepository catRepository;

	@Autowired
    public SearchController(RecordRepository repo) { 
        RecordRepository = repo;
    }
	
	@Autowired
	MongoOperations mongoOps ;

	@ApiOperation(value = "Get data.json format data.",nickname = "PDL",
				  notes = "This will return the high level data.json fields, dataset is part of it.")
	@RequestMapping(value = {"/catalog"}, method = RequestMethod.GET, produces="application/json")
	public List<catalog> Search ( )throws IOException {
	    List<catalog> pdlEntries = catRepository.findAll();
	    return pdlEntries;
	}
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                value = "Number of records per page."),
        @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                value = "Sorting criteria in the format: property(,asc|desc). " +
                        "Default sort order is ascending. " +
                        "Multiple sort criteria are supported.")
    })
	@ApiOperation(value = "List all dataset records.",nickname = "Allrecords")
	@RequestMapping(value = {"/records"}, method = RequestMethod.GET, produces="application/json")
	public List<Record> SearchAll (@ApiIgnore Pageable p) {
    
		logger.info("Requested searchAll:");
		List<Record> pdlEntries = RecordRepository.findAllBy(p);
    	return pdlEntries;
    }
	
	  @ApiOperation(value = "Search records using any search phrase.",nickname = "searchanything",
			  		notes = " searchphrase can accept any combination of words and do text search in complete database."
			  				+ " Logical operations can also be performed by doing following combinations "
			  				+ " e.g. "
			  				+ "\n 1. /records/search?searchphrase=chemistry srd 69 data  ---> This will search all the words  "
			  				+ "\n 2. /records/search?searchphrase=chemistry \"srd 69\" data  ---> This will search OR operation between phrase \"srd 69\" and other words "
			  				+ "\n 3. /records/search?searchphrase=\"chemistry srd 69 data\"  ---> This will search complete phrase."
			  				+ "\n 4. /records/search?searchphrase=\"chemistry\" \"srd 69\" \"data\"  ---> This will do \"AND\" operation between all words. ")
	  @ApiImplicitParams({
	      @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
	              value = "Results page you want to retrieve (0..N)"),
	      @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
	              value = "Number of records per page."),
	      @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
	              value = "Sorting criteria in the format: property(,asc|desc). " +
	                      "Default sort order is ascending. " +
	                      "Multiple sort criteria are supported.")
	  })
	  @RequestMapping(value = {"/records/search"}, method = RequestMethod.GET, produces="application/json")
	  public List<Record> Search (@ApiIgnore Pageable p,@RequestParam  String searchphrase )
			   throws IOException {
	  	  /** this is added only for swagger-ui to work once it is fixed we will use Map parameters directly **/  
		  	TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matchingAny(searchphrase);
			Query query = TextQuery.queryText(textCriteria);
	        return mongoOps.find(query.with(p), Record.class);
	  }

	
	//*** This is commented because SwaggerUI does not work with Map parameters
	//This is the main /search method which accepts various kinds of request parameters including 
    // advanced search with logical operations on the columns/fields
    // This search api can take any key=value pair.
    // for logical operations "logicalOp" will accept and/or op
    // for searching any text use "searchPhrase" 
    /// This was added for SwaggerUI to show the options for pageable
//    @ApiOperation(value = "pageable.",nickname = "searchpage")
//	 @ApiImplicitParams({
//        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
//                value = "Results page you want to retrieve (0..N)"),
//        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
//                value = "Number of records per page."),
//        @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
//                value = "Sorting criteria in the format: property(,asc|desc). " +
//                        "Default sort order is ascending. " +
//                        "Multiple sort criteria are supported.")
//    })
//    @RequestMapping(value = {"/records/search"}, method = RequestMethod.GET, produces="application/json")
//	public List<Record> Search (@ApiIgnore Pageable p,@RequestParam @ApiIgnore  Map<String,String> params )
//		   throws IOException {
//    	
//          ProcessRequest processRequest = new ProcessRequest();
//          //return processRequest.handleRequest(params);
//          return mongoOps.find(processRequest.handleRequest(params).with(p), Record.class);
//    }
	
//	 @ApiOperation(value = "pageable.",nickname = "searchpage")
//	 @ApiImplicitParams({
//        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
//                value = "Results page you want to retrieve (0..N)"),
//        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
//                value = "Number of records per page."),
//        @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
//                value = "Sorting criteria in the format: property(,asc|desc). " +
//                        "Default sort order is ascending. " +
//                        "Multiple sort criteria are supported.")
//    })
//    @RequestMapping(value = {"/records/search"}, method = RequestMethod.GET, produces="application/json")
//	public List<Record> Search (@ApiIgnore Pageable p,@RequestParam @ApiIgnore  Map<String,String> params )
//		   throws IOException {
//    	
//          ProcessRequest processRequest = new ProcessRequest();
//          //return processRequest.handleRequest(params);
//          return mongoOps.find(processRequest.handleRequest(params).with(p), Record.class);
//    }
	
 

	@ApiOperation(value = "Get the record using identifier.",nickname = "searchbyID",
				  notes= "We plan to use DOI once it is ready or plan to write code our own unique identfier. "	)
    @RequestMapping(value = {"/records/search/{id}"}, method = RequestMethod.GET, produces="application/json")
	public List<Record> SearchById (@PathVariable String id,Pageable p){
			return RecordRepository.findByIdentifier(id, p);
	}
    
	 @ApiOperation(value = "Search record by title.",nickname = "searchbyTitle",
			  notes= "It can search with full title or any world in title."	)
	 @RequestMapping(value = {"/records/searchbyTitle"}, method = RequestMethod.GET, produces="application/json")
	 public List<Record> SearchByTitle (@RequestParam String title) {
	    
			logger.info("Requested searchbyTitle:"+title);
			TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(title);
			//Criteria cr1 = Criteria.where("title").regex(Pattern.compile(title, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));			
			//return RecordRepository.findTitleBy(criteria);
			return RecordRepository.findByTitleContainingIgnoreCase(title);
	   }
		
	 @RequestMapping(value = {"/records/searchbyType"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByType(@RequestParam String type) {
			
			logger.info("Requested searchbyType:"+type);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(type);
			return RecordRepository.findByTypeContainingIgnoreCase(type);
	    }
		
		@RequestMapping(value = {"/records/searchbyKeyword"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByKeyword (@RequestParam String keyword) {
	    
			logger.info("Requested keyword:"+keyword);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(keyword);
			return RecordRepository.findByKeywordContainingIgnoreCase(keyword);
	    	
	    }
		@RequestMapping(value = {"/records/searchByProgramCode"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByProgramCode (@RequestParam String programcode) {
	    
			logger.info("Requested programcode:"+programcode);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(programcode);
			return RecordRepository.findByProgramCodeContainingIgnoreCase(programcode);
	    	
	    }
		
		@RequestMapping(value = {"/records/searchBylandingPage"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByLandingPage (@RequestParam String landingPage) {
	    
			logger.info("Requested landingPage:"+landingPage);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(landingPage);
			return RecordRepository.findByLandingPageContainingIgnoreCase(landingPage);
	    	
		}
		
		
		@RequestMapping(value = {"/records/searchByAccessLevel"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByAccessLevel (@RequestParam String accessLevel) {
	    
			logger.info("Requested accessLevel:"+accessLevel);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(accessLevel);
			return  RecordRepository.findByAccessLevelContainingIgnoreCase(accessLevel);
	    	
	    }
		
		@RequestMapping(value = {"/records/searchByDistribution"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByDistribution (@RequestParam String distribution) {
	    
			logger.info("Requested distribution:"+distribution);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(distribution);
			return RecordRepository.findByDistributionContainingIgnoreCase(distribution);
	    	
	    }
		
		@RequestMapping(value = {"/records/searchByModified"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByModified (@RequestParam String modified) {
	    
			logger.info("Requested modified:"+modified);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(modified);
			return RecordRepository.findByModifiedContainingIgnoreCase(modified);
	    	
	    }
		
		@RequestMapping(value = {"/records/searchByPublisher"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByPublisher (@RequestParam String publisher) {
	    
			logger.info("Requested publisher:"+publisher);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(publisher);
			return RecordRepository.findByPublisherContainingIgnoreCase(publisher);
	    	
	    }
		
		@RequestMapping(value = {"/records/searchByReferences"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByReferences (@RequestParam String references) {
	    
			logger.info("Requested references:"+references);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(references);
			return RecordRepository.findByReferencesContainingIgnoreCase(references);
	    	
	    }
	
		@RequestMapping(value = {"/records/searchByBureauCode"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByBureauCode (@RequestParam String bureauCode) {
	    
			logger.info("Requested references:"+bureauCode);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(bureauCode);
			return  RecordRepository.findByBureauCodeContainingIgnoreCase(bureauCode);
	    	
	    }
		
		@RequestMapping(value = {"/records/searchByDescription"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchBydescription (@RequestParam String description) {
	    
			logger.info("Requested description:"+description);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(description);
			return RecordRepository.findByDescriptionContainingIgnoreCase(description);
	    	
	    }

		@RequestMapping(value = {"/records/searchByContactPoint"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByContactPoint (@RequestParam String contactPoint) {
	    
			logger.info("Requested contactPoint:"+contactPoint);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(contactPoint);
			return RecordRepository.findByContactPointContainingIgnoreCase(contactPoint);
	    	
	    }
		
		@RequestMapping(value = {"/records/searchByLanguage"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByLanguage (@RequestParam String language) {
	    
			logger.info("Requested language:"+language);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(language);
			return RecordRepository.findByLanguageContainingIgnoreCase(language);
	    	
	    }
		
		@RequestMapping(value = {"/records/searchByLicense"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByLicense(@RequestParam String license) {
	    
			logger.info("Requested license:"+license);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(license);
			return RecordRepository.findByLanguageContainingIgnoreCase(license);
	    	
	    }
		
		
	/*@Bean
	public CommonsRequestLoggingFilter loggingFilter() {
		final CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
		filter.setIncludePayload(true);
		return filter;
	}*/


}
