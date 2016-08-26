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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
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
import gov.nist.mml.domain.Record;
import gov.nist.mml.domain.nestedpod.ContactPoint;
import gov.nist.mml.domain.nestedpod.Distribution;
import gov.nist.mml.domain.nestedpod.Publisher;
import gov.nist.mml.exception.ResourceNotFoundException;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.query.Query.query;
@RestController
@Api(value = "Test api for searching all data", tags = "Search API")
public class SearchController {
	
	private Logger logger = LoggerFactory.getLogger(SearchController.class);

	@Autowired
    private RecordRepository RecordRepository;

	@Autowired
    public SearchController(RecordRepository repo) { 
        RecordRepository = repo;
    }
	
	@Autowired
	MongoOperations mongoOps ;
	
	@RequestMapping(value = {"/records"}, method = RequestMethod.GET, produces="application/json")
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
	public List<Record> SearchAll (@ApiIgnore Pageable p) {
    
		logger.info("Requested searchAll:");
		
		List<Record> pdlEntries = RecordRepository.findAllBy(p);
    	return pdlEntries;
    }
	
	//*** This is the main /search method which accepts various kinds of request parameters including 
    // advanced search with logical operations on the columns/fields
    // This search api can take any key=value pair.
    // for logical operations "logicalOp" will accept and/or op
    // for searching any text use "searchPhrase" 
    /// This was added for SwaggerUI to show the options for pageable
    @ApiOperation(value = "pageable.",nickname = "searchpage")
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
	public List<Record> Search (@ApiIgnore Pageable p,@RequestParam @ApiIgnore  Map<String,String> params )
		   throws IOException {
    	
    	Boolean logical =false; TextCriteria textCriteria = null; Criteria criteria = null;
    	ArrayList<Criteria> criterias = new ArrayList<Criteria>();
    	Query  mainQuery = null,textQuery =null; 
    	
    	if(!params.entrySet().isEmpty()){
    		
    		for (Entry<String, String> entry : params.entrySet()) {
    			
    			switch(entry.getKey().toLowerCase()){
    			
    			case "logicalop":  if(!entry.getValue().isEmpty()&& entry.getValue().equalsIgnoreCase("or") ) 
    	    								logical = false;
    								if(!entry.getValue().isEmpty()&& entry.getValue().equalsIgnoreCase("and") ) 
    										logical = true;
    								break;
    			case "searchphrase": if(!entry.getValue().isEmpty()) {
    									textCriteria = TextCriteria.forDefaultLanguage().matchingAny(entry.getValue());
    									textQuery = TextQuery.queryText(textCriteria);}
    								break;
    			case "page": break;
    			case "size": break;
    			case "sort": break;
    			default :
    					Criteria cri =  Criteria.where(entry.getKey()).regex(Pattern.compile(entry.getValue(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
    	    			criterias.add(cri);
    	    			break;
    			}
    		}	
    		if(!criterias.isEmpty()){
    			if(!logical)
    				criteria = new Criteria().orOperator(criterias.toArray(new Criteria[criterias.size()]));
    			if(logical)
    				criteria =new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
    			
        	}
    		if(textQuery!=null){
    			if(criteria == null)
    				mainQuery = textQuery;
    			else
    				mainQuery = textQuery.addCriteria(criteria);
    		}
			else
				mainQuery = query(criteria);
	    }
    	
    	logger.info("Requested searchAll:");
		if(mainQuery != null)
			return mongoOps.find(mainQuery.with(p), Record.class);
		else 
			throw new IOException("Check all the request parameters.");
    }
	
//	    @ApiOperation(value = "Search All the entries using text search.",nickname = "seatrchbyphrase")
//		@RequestMapping(value = {"/records/search"}, method = RequestMethod.GET, produces="application/json")
//		public List<Record> SearchByText (@RequestParam String searchPhrase,@RequestParam int page, @RequestParam int pagesize ) {
//	    
//			logger.info("Requested searchPhrase:"+searchPhrase);
//			TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(searchPhrase);
//			//return RecordRepository.findAllBy(criteria,new PageRequest(1,1,Sort.Direction.DESC,"title"));
//			return RecordRepository.findAllBy(criteria,new PageRequest(page,pagesize,Sort.Direction.DESC,"title"));
//		}
		
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
