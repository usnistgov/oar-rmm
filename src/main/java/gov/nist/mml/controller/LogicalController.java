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

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.mml.domain.Record;
import gov.nist.mml.repositories.RecordRepository;
import io.swagger.annotations.Api;
import org.springframework.web.context.request.WebRequest;

/**
 * @author dsn1
 * Deoyani Nandrekar-Heinis
 *
 */
@RestController
@Api(value = "RESt api for searching data with logical operations", tags = "Logical API")
public class LogicalController {
	private Logger logger = LoggerFactory.getLogger(LogicalController.class);

	@Autowired
    private RecordRepository recordRepository;

	@Autowired
    public LogicalController(RecordRepository repo) { 
        recordRepository = repo;
    }
	
	@Autowired
    HttpServletRequest request;
	
	@Autowired
	MongoOperations mongoOps ;
	
	
	
	@RequestMapping(value = {"/records/exception"}, method = RequestMethod.GET, produces="application/json")
	public String SearchTest () throws IOException {
		throw new IOException("Getting  problem.");
	}
	@RequestMapping(value = {"/records/searchTest"}, method = RequestMethod.POST, produces="text/plain")//produces="application/json")
	public String SearchTest (WebRequest request) {
		Map<String,String[]> m = request.getParameterMap();
	   String[] test =  m.get("t");
    return "Nothing To show"+test[0];
		
//		ArrayList<Criteria> criterias = new ArrayList<Criteria>();
//        
//        logger.info("Requested search with OR:");
//		for (Entry<String, String> entry : params.entrySet()) {
//		
//			Criteria cri =  Criteria.where(entry.getKey()).regex(Pattern.compile(entry.getValue(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
//			criterias.add(cri);
//		}	
//		// return mongoOps.find(query(new Criteria().orOperator(criterias.toArray(new Criteria[criterias.size()]))), Record.class);
//        List<Record> test = recordRepository.findAllBy(query(new Criteria().orOperator(criterias.toArray(new Criteria[criterias.size()]))));
//		//return recordRepository.findAllBy(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()])));
//        return test;
	}
	

//	@RequestMapping(value = {"/records/searchOr"}, method = RequestMethod.GET, produces="application/json")
//	public List<Record> SearchOr (@RequestParam Map<String,String> params) {
//    
//		ArrayList<Criteria> criterias = new ArrayList<Criteria>();
//        
//        logger.info("Requested search with OR condition:");
//		for (Entry<String, String> entry : params.entrySet()) {
//		
//			Criteria cri =  Criteria.where(entry.getKey()).regex(Pattern.compile(entry.getValue(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
//			criterias.add(cri);
//		}	
//		 return mongoOps.find(query(new Criteria().orOperator(criterias.toArray(new Criteria[criterias.size()]))), Record.class);
//        
//    }
//	
//	
//	@RequestMapping(value = {"/records/searchAnd"}, method = RequestMethod.GET, produces="application/json")
//	public List<Record> SearchAnds (@RequestParam Map<String,String> params) {
//        ArrayList<Criteria> criterias = new ArrayList<Criteria>();
//        logger.info("Requested search with and:");
//		for (Entry<String, String> entry : params.entrySet()) {
//		  Criteria cri =  Criteria.where(entry.getKey()).regex(Pattern.compile(entry.getValue(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
//		  criterias.add(cri);
//		}
//		return mongoOps.find(query(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]))), Record.class);
//    }

}
