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
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.schema.JsonSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.mml.domain.Record;
import gov.nist.mml.domain.Catalog;
import gov.nist.mml.repositories.CatalogRepository;
import gov.nist.mml.repositories.RecordRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;;
@RestController
@Api(value = "Api endpoints to search EDI/PDL data", tags = "Search API")
public class SearchController {
	
	private Logger logger = LoggerFactory.getLogger(SearchController.class);

	@Autowired
    private RecordRepository recordRepository;
	
	@Autowired
    private CatalogRepository catRepository;

	@Autowired
	MongoOperations mongoOps ;
	
	@Autowired
    public SearchController(RecordRepository repo) { 
        recordRepository = repo;
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
	@ApiOperation(value = "Get complete data.json.",nickname = "PDL",
				  notes = "This will return the high level data.json fields, dataset is part of it.")
	@RequestMapping(value = {"/catalog"}, method = RequestMethod.GET, produces="application/json")
	public List<Catalog> search (@ApiIgnore @PageableDefault(size=1000) Pageable p)throws IOException {
	   return catRepository.findAll();
	   
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
	@RequestMapping(value = {"/catalog/records"}, method = RequestMethod.GET, produces="application/json")
	public List<Record> searchAll (@ApiIgnore @PageableDefault(size=1000) Pageable p) {
		 
		logger.info("Requested searchAll:");
		List<Record> pdlEntries = recordRepository.findAllBy(p);
    	return pdlEntries;
    }
	
	  @ApiOperation(value = "Search records using any search phrase.",nickname = "searchanything",
			  		notes = " searchphrase can accept any combination of words and do text search in complete database."
			  				+ " Logical operations can also be performed by doing following combinations "
			  				+ " e.g. "
			  				+ "<br> <br> 1. /records/search?searchphrase=chemistry srd 69 data  --> search all the words  "
			  				+ "<br> <br> 2. /records/search?searchphrase=chemistry \"srd 69\" data  --> search OR  between phrase \"srd 69\" and other words "
			  				+ "<br> <br> 3. /records/search?searchphrase=\"chemistry srd 69 data\"  --> search complete phrase. AND"
			  				+ "<br> <br> 4. /records/search?searchphrase=\"chemistry\" \"srd 69\" \"data\"  --> \"OR\" operation between all \"AND\" phrases "
			  				+ "<br> <br> To get more information <a href=\"/RMMApi/apidoc.html\" >Refer document</a>. "
			  				+ "<br> <br> Note: Please ignore 'p' field below, input your pagination values in page,size and sort fields.")
    		      		
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
	  @RequestMapping(value = {"/catalog/records/search"}, method = RequestMethod.GET, produces="application/json")
	  public List<Record> search (@ApiIgnore @PageableDefault(size=1000) Pageable p,@RequestParam  String searchphrase )
			   throws IOException {
		    
		    if(searchphrase == null || searchphrase.equals("")) 
		    	return recordRepository.findAll();
		    
		    else{
		    	
		    	logger.info("search phrase in the records. "+searchphrase);
		    	
		    	/** this is added only for swagger-ui to work once it is fixed we will use Map parameters directly **/  
		    	TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matchingAny(searchphrase);
		    	Query query = TextQuery.queryText(textCriteria);
		    	
		    			
		    	return mongoOps.find(query.with(p), Record.class);
	        }
	  }

	
	
 

	@ApiOperation(value = "Get the record using identifier.",nickname = "searchbyID",
				  notes= "We plan to use DOI once it is ready or plan to write code our own unique identfier. "	)
    @RequestMapping(value = {"/catalog/records/search/{id}"}, method = RequestMethod.GET, produces="application/json")
	public List<Record> searchbyId (@PathVariable String id,Pageable p){
			return recordRepository.findByIdentifier(id, p);
	}
    
	 @ApiOperation(value = "Search record by title.",nickname = "searchbyTitle",
			  notes= "It can search with full title or any world in title."	)
	 @RequestMapping(value = {"/catalogs/records/searchbyTitle"}, method = RequestMethod.GET, produces="application/json")
	 public List<Record> searchbyTitle (@RequestParam String title) {
	    
			logger.info("Requested searchbyTitle:"+title);
					return recordRepository.findByTitleContainingIgnoreCase(title);
	   }
		
	 @RequestMapping(value = {"/catalogs/records/searchbyType"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> searchbyType(@RequestParam String type) {
			
			logger.info("Requested searchbyType:"+type);
			return recordRepository.findByTypeContainingIgnoreCase(type);
	    }
		
		@RequestMapping(value = {"/catalogs/records/searchbyKeyword"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> searchbyKeyword (@RequestParam String keyword) {
	    
			logger.info("Requested keyword:"+keyword);
			return recordRepository.findByKeywordContainingIgnoreCase(keyword);
	    	
	    }
		@RequestMapping(value = {"/catalogs/records/searchByProgramCode"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> searchbyProgramcode (@RequestParam String programcode) {
	    
			logger.info("Requested programcode:"+programcode);
			return recordRepository.findByProgramCodeContainingIgnoreCase(programcode);
	    	
	    }
		
		@RequestMapping(value = {"/catalogs/records/searchBylandingPage"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> searchbyLandingPage (@RequestParam String landingPage) {
	    
			logger.info("Requested landingPage:"+landingPage);
			return recordRepository.findByLandingPageContainingIgnoreCase(landingPage);
	    	
		}
		
		
		@RequestMapping(value = {"/catalogs/records/searchByAccessLevel"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> searchByAccessLevel (@RequestParam String accessLevel) {
	    
			logger.info("Requested accessLevel:"+accessLevel);
			return  recordRepository.findByAccessLevelContainingIgnoreCase(accessLevel);
	    	
	    }
		
		@RequestMapping(value = {"/catalogs/records/searchByDistribution"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> searchByDistribution (@RequestParam String distribution) {
	    
			logger.info("Requested distribution:"+distribution);
			return recordRepository.findByDistributionContainingIgnoreCase(distribution);
	    	
	    }
		
		@RequestMapping(value = {"/catalogs/records/searchByModified"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> searchByModified (@RequestParam String modified) {
	    
			logger.info("Requested modified:"+modified);
			return recordRepository.findByModifiedContainingIgnoreCase(modified);
	    	
	    }
		
		@RequestMapping(value = {"/catalogs/records/searchByPublisher"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> searchByPublisher (@RequestParam String publisher) {
	    
			logger.info("Requested publisher:"+publisher);
			return recordRepository.findByPublisherContainingIgnoreCase(publisher);
	    	
	    }
		
		@RequestMapping(value = {"/catalogs/records/searchByReferences"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> searchByReferences (@RequestParam String references) {
	    
			logger.info("Requested references:"+references);
			return recordRepository.findByReferencesContainingIgnoreCase(references);
	    	
	    }
	
		@RequestMapping(value = {"/catalogs/records/searchByBureauCode"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> searchByBureauCode (@RequestParam String bureauCode) {
	    
			logger.info("Requested references:"+bureauCode);
			return  recordRepository.findByBureauCodeContainingIgnoreCase(bureauCode);
	    	
	    }
		
		@RequestMapping(value = {"/catalogs/records/searchByDescription"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> searchBydescription (@RequestParam String description) {
	    
			logger.info("Requested description:"+description);
			return recordRepository.findByDescriptionContainingIgnoreCase(description);
	    	
	    }

		@RequestMapping(value = {"/catalogs/records/searchByContactPoint"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> searchByContactPoint (@RequestParam String contactPoint) {
	    
			logger.info("Requested contactPoint:"+contactPoint);
			return recordRepository.findByContactPointContainingIgnoreCase(contactPoint);
	    	
	    }
		
		@RequestMapping(value = {"/catalogs/records/searchByLanguage"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> searchByLanguage (@RequestParam String language) {
	    
			logger.info("Requested language:"+language);
			return recordRepository.findByLanguageContainingIgnoreCase(language);
	    	
	    }
		
		@RequestMapping(value = {"/catalogs/records/searchByLicense"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> searchByLicense(@RequestParam String license) {
	    
			logger.info("Requested license:"+license);
			return recordRepository.findByLanguageContainingIgnoreCase(license);
	    	
	    }

		
		/// This is the placeholder for returning list of field names and types.
		@RequestMapping(value = {"/catalogs/records/fieldsnames"}, method = RequestMethod.GET, produces="application/json")
		public String getFieldNames(){
			logger.info("Test to get field names");
			try{
				org.codehaus.jackson.map.ObjectMapper mapper = new ObjectMapper();
				//There are other configuration options you can set.  This is the one I needed.
				mapper.configure(org.codehaus.jackson.map.SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, true);

				JsonSchema schema = mapper.generateJsonSchema(Record.class);

				return mapper.defaultPrettyPrintingWriter().writeValueAsString(schema);
		    }catch(Exception e){
				return e.getMessage();
			}
		}

}
