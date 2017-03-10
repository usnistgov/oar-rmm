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

import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.mml.domain.Record;
import io.swagger.annotations.Api;
@RestController
@Api(value = "Test api for searching all data", tags = "Test API")
public class TestController {

	private Logger logger = LoggerFactory.getLogger(TestController.class);

	
	@Autowired
	MongoOperations mongoOps ;
	@RequestMapping(value = {"/testOr"}, method = RequestMethod.GET, produces="application/json")
	public List<Record> searchOr (@RequestParam String title, @RequestParam String access) {
    
		logger.info("Requested search with Or:");
		Criteria cr1 = Criteria.where("title").regex(Pattern.compile(title, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
		Criteria cr2 = Criteria.where("accessLevel").is(access);
				
        return mongoOps.find(query(new Criteria().orOperator(cr1,cr2)), Record.class);
    }
	
	
	@RequestMapping(value = {"/testAnd"}, method = RequestMethod.GET, produces="application/json")
	public List<Record> searchAnd (@RequestParam String title, @RequestParam String access) {
    
		logger.info("Requested search with and:");
		Criteria cr1 = Criteria.where("title").regex(Pattern.compile(title, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
		Criteria cr2 = Criteria.where("accessLevel").is(access);
					
        return mongoOps.find(query(new Criteria().andOperator(cr1,cr2)), Record.class);
    }
	
	
	@RequestMapping(value = {"/testAnds"}, method = RequestMethod.GET, produces="application/json")
	public List<Record> searchAnds (@RequestParam Map<String,String> params) {
        ArrayList<Criteria> criterias = new ArrayList<Criteria>();
        
        logger.info("Requested search with multiple and ops:");
		for (Entry<String, String> entry : params.entrySet()) {
		   Criteria cri =  Criteria.where(entry.getKey()).regex(Pattern.compile(entry.getValue(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
		  criterias.add(cri);
		}
		return mongoOps.find(query(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]))), Record.class);
    }

}
