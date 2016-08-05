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

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import gov.nist.mml.repositories.RecordRepository;
import gov.nist.mml.domain.Record;
import gov.nist.mml.domain.nestedpod.ContactPoint;
import gov.nist.mml.domain.nestedpod.Distribution;
import gov.nist.mml.domain.nestedpod.Publisher;
import gov.nist.mml.exception.ResourceNotFoundException;;

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
	
	@RequestMapping(value = {"/records"}, method = RequestMethod.GET, produces="application/json")
	public List<Record> SearchAll () {
    
		logger.info("Requested searchAll:");
		
		List<Record> pdlEntries = RecordRepository.findAll();
    	return pdlEntries;
    }
	
	    @ApiOperation(value = "Search All the entries using text search.",nickname = "seatrchbyphrase")
		@RequestMapping(value = {"/records/search"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByText (@RequestParam String searchPhrase) {
	    
			logger.info("Requested searchPhrase:"+searchPhrase);
			TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(searchPhrase);
			return RecordRepository.findAllBy(criteria);
	    	
		}
		
		@RequestMapping(value = {"/records/searchbyTitle"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByTitle (@RequestParam String title) {
	    
			logger.info("Requested searchbyTitle:"+title);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(title);
			//return RecordRepository.findAllByTitle(criteria);
			return RecordRepository.findByTitleContaining(title);
	    }
		
		@RequestMapping(value = {"/records/searchbyType"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByType(@RequestParam String type) {
	    
			logger.info("Requested searchbyType:"+type);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(type);
			return RecordRepository.findByTypeContaining(type);
	    }
		
		@RequestMapping(value = {"/records/searchbyKeyword"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByKeyword (@RequestParam String keyword) {
	    
			logger.info("Requested keyword:"+keyword);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(keyword);
			return RecordRepository.findByKeywordContaining(keyword);
	    	
	    }
		@RequestMapping(value = {"/records/searchByProgramCode"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByProgramCode (@RequestParam String programcode) {
	    
			logger.info("Requested programcode:"+programcode);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(programcode);
			return RecordRepository.findByProgramCodeContaining(programcode);
	    	
	    }
		
		@RequestMapping(value = {"/records/searchBylandingPage"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByLandingPage (@RequestParam String landingPage) {
	    
			logger.info("Requested landingPage:"+landingPage);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(landingPage);
			return RecordRepository.findByLandingPageContaining(landingPage);
	    	
		}
		
		
		@RequestMapping(value = {"/records/searchByAccessLevel"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByAccessLevel (@RequestParam String accessLevel) {
	    
			logger.info("Requested accessLevel:"+accessLevel);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(accessLevel);
			return  RecordRepository.findByAccessLevelContaining(accessLevel);
	    	
	    }
		
		@RequestMapping(value = {"/records/searchByDistribution"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByDistribution (@RequestParam String distribution) {
	    
			logger.info("Requested distribution:"+distribution);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(distribution);
			return RecordRepository.findByDistributionContaining(distribution);
	    	
	    }
		
		@RequestMapping(value = {"/records/searchByModified"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByModified (@RequestParam String modified) {
	    
			logger.info("Requested modified:"+modified);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(modified);
			return RecordRepository.findByModifiedContaining(modified);
	    	
	    }
		
		@RequestMapping(value = {"/records/searchByPublisher"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByPublisher (@RequestParam String publisher) {
	    
			logger.info("Requested publisher:"+publisher);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(publisher);
			return RecordRepository.findByPublisherContaining(publisher);
	    	
	    }
		@RequestMapping(value = {"/records/searchByReferences"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByReferences (@RequestParam String references) {
	    
			logger.info("Requested references:"+references);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(references);
			return RecordRepository.findByReferencesContaining(references);
	    	
	    }
	
		@RequestMapping(value = {"/records/searchByBureauCode"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByBureauCode (@RequestParam String bureauCode) {
	    
			logger.info("Requested references:"+bureauCode);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(bureauCode);
			return  RecordRepository.findByBureauCodeContaining(bureauCode);
	    	
	    }
		
		@RequestMapping(value = {"/records/searchByDescription"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchBydescription (@RequestParam String description) {
	    
			logger.info("Requested description:"+description);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(description);
			return RecordRepository.findByDescriptionContaining(description);
	    	
	    }

		@RequestMapping(value = {"/records/searchByContactPoint"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByContactPoint (@RequestParam String contactPoint) {
	    
			logger.info("Requested contactPoint:"+contactPoint);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(contactPoint);
			return RecordRepository.findByContactPointContaining(contactPoint);
	    	
	    }
		
		@RequestMapping(value = {"/records/searchByLanguage"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByLanguage (@RequestParam String language) {
	    
			logger.info("Requested language:"+language);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(language);
			return RecordRepository.findByLanguageContaining(language);
	    	
	    }
		@RequestMapping(value = {"/records/searchByLicense"}, method = RequestMethod.GET, produces="application/json")
		public List<Record> SearchByLicense(@RequestParam String license) {
	    
			logger.info("Requested license:"+license);
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(license);
			return RecordRepository.findByLanguageContaining(license);
	    	
	    }
	/*@Bean
	public CommonsRequestLoggingFilter loggingFilter() {
		final CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
		filter.setIncludePayload(true);
		return filter;
	}*/

}
