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
package gov.nist.mml.utilities;

import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;

import gov.nist.mml.domain.Record;

public class ProcessRequest {

	
	
	
	private Logger logger = LoggerFactory.getLogger(ProcessRequest.class);
	
	public Query handleRequest(Map<String,String> params) throws IOException{
	Boolean logical =false; TextCriteria textCriteria = null; Criteria criteria = null;
	ArrayList<Criteria> criterias = new ArrayList<Criteria>();
	Query  mainQuery = null,textQuery =null; 
	
	if(!params.entrySet().isEmpty()){
		
		for (Entry<String, String> entry : params.entrySet()) {
			
			switch(entry.getKey().toLowerCase()){
			
			case "logicalop":  
							if("or".equalsIgnoreCase(entry.getValue())) 
										logical = false;
							if("and".equalsIgnoreCase(entry.getValue())) 
										logical = true;
								break;
								
			case "searchphrase": if(!entry.getValue().isEmpty()) {
										textQuery  = parseSearchPhrase(entry.getValue());
									}
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
		//return mongoOps.find(mainQuery.with(p), Record.class);
		return mainQuery;
	else 
		throw new IOException("Check all the request parameters.");
	}
	
	/**
	 * 
	 */
	private Query parseSearchPhrase(String phrase){
		
		TextCriteria textCriteria = null;
		Query q = null;
//		boolean b = Pattern.matches("AND", phrase);
//		String[] test1 = null; 
//		if (b)
//			test1  = phrase.split("AND");
		
		textCriteria = TextCriteria.forDefaultLanguage().matchingAny(phrase);
		q = TextQuery.queryText(textCriteria);
		return q;
	}
}