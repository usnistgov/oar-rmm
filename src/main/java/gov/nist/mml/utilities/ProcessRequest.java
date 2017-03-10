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
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;

public class ProcessRequest {
	
	private Logger logger = LoggerFactory.getLogger(ProcessRequest.class);
	private String[] fieldKeywords =  {"theme","searchphrase"};
	boolean theme =false; 
	boolean searchphrase=false;
	
	public Query handleRequest(Map<String,String> params) throws IOException{
	Boolean logical =false; 
	Criteria criteria = null;
	ArrayList<Criteria> criterias = new ArrayList<Criteria>();
	Query  mainQuery = null;
	Query  textQuery =null; 

	
//	if(params.size() ==2 && params.containsKey(fieldKeywords[0]) && params.containsKey(fieldKeywords[1]))
//	{
//			if(params.get(fieldKeywords[0]).isEmpty() && params.get(fieldKeywords[1]).isEmpty())
//				return null;
//	}
//	
//	if(params.size() == 1 && params.containsKey(fieldKeywords[1])){
//		textQuery  = parseSearchPhrase(params.get(fieldKeywords[1]));
//	}
	if(!params.entrySet().isEmpty()){
		
		for (Entry<String, String> entry : params.entrySet()) {
			
			switch(entry.getKey().toLowerCase()){
			
			case "logicalop": if("or".equalsIgnoreCase(entry.getValue()))
								logical = false;
							  if("and".equalsIgnoreCase(entry.getValue())) 
								logical = true;
							  break;
			case "searchphrase": if(!entry.getValue().isEmpty()) 
										textQuery  = parseSearchPhrase(entry.getValue());
								 else
										searchphrase = true;
								 break;
			case "page":
			case "size": 
			case "sort": break;
			
			case "theme" : if("all".equalsIgnoreCase(entry.getValue())) 
							theme = true;
						   else
							   criterias.add(addCriteria(entry.getKey(),entry.getValue()));
							break;
		    default :criterias.add(addCriteria(entry.getKey(),entry.getValue()));
	    			 break;
			}
		}	
		
		if(!criterias.isEmpty()){
			if(!logical)
				criteria = new Criteria().orOperator(criterias.toArray(new Criteria[criterias.size()]));
			if(logical)
				criteria =new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
			
    	}
		if(textQuery != null){
			if(criteria == null)
				mainQuery = textQuery;
			else
				mainQuery = textQuery.addCriteria(criteria);
		}
		else{
			if(criteria != null)
			mainQuery = query(criteria);
		}
    }
	if(params.entrySet().size() == 2 && theme && searchphrase) 
		return null;
	
	if(mainQuery != null)
		return mainQuery;
	else 
		throw new IOException("Check all the request parameters.");
	}
	
	/**
	 * 
	 */
	private Query parseSearchPhrase(String phrase){

		TextCriteria textCriteria =TextCriteria.forDefaultLanguage().matchingAny(phrase);
		return TextQuery.queryText(textCriteria);
		
	}
	
	private Criteria addCriteria(String key, String value){
		return Criteria.where(key).regex(Pattern.compile(value, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
		
	}
}