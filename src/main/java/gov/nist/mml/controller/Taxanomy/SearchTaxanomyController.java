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
package gov.nist.mml.controller.Taxanomy;

import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import gov.nist.mml.domain.Taxanomy;

import gov.nist.mml.repositories.TaxanomyRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dsn1
 *
 */
@RestController
@Api(value = "Test api for searching taxanomy data", tags = "Search Taxanomy API")
public class SearchTaxanomyController {
	
	private Logger logger = LoggerFactory.getLogger(SearchTaxanomyController.class);

	@Autowired
    private TaxanomyRepository taxRepository;

	@Autowired
    public SearchTaxanomyController(TaxanomyRepository repo) { 
       taxRepository = repo;
    }
	
	
	
	@RequestMapping(value = {"/taxanomy"}, method = RequestMethod.GET, produces="application/json")
	public List<Taxanomy> SearchAll (Pageable p) {
    
		logger.info("Requested searchAll:");
		
		List<Taxanomy> taxEntries = (List<Taxanomy>) taxRepository.findAll(p);
    	return taxEntries;
    }
	
	    //@ApiOperation(value = "Search All the entries using text search.",nickname = "seatrchbyphrase")
		@RequestMapping(value = {"/taxanomy/search"}, method = RequestMethod.GET, produces="application/json")
		public List<Taxanomy> SearchByText (@RequestParam String searchPhrase,@RequestParam int page, @RequestParam int pagesize ) {
	    
			logger.info("Requested searchPhrase:"+searchPhrase);
			TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(searchPhrase);
			return taxRepository.findAllBy(criteria,new PageRequest(page,pagesize,Sort.Direction.DESC,"title"));
		}
		
//		@RequestMapping(value = {"/taxanomy/searchbyName"}, method = RequestMethod.GET, produces="application/json")
//		public List<Taxanomy> SearchByTitle (@RequestParam String name) {
//	    
//			logger.info("Requested searchbyName:"+name);
//			TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(name);
//			//Criteria cr1 = Criteria.where("title").regex(Pattern.compile(title, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
//			
//			//return RecordRepository.findTitleBy(criteria);
//			//return taxRepository.findByNameContainingIgnoreCase(name);
//	    }
}
