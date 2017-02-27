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
package gov.nist.oar.rmm.utilities;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;


/**
 * Process the RestApi request to parse parameters and create part of query.
 * @author dsn1
 *
 */
public class ProcessRequest{
	
	private Logger logger = LoggerFactory.getLogger(ProcessRequest.class);
	private Bson filter = null;
	private Bson projections= null;
	private Bson sort = null;
	private int pagenumber = 0;
	private int pagesize = 0;
	private ArrayList<String> logicalOps = new ArrayList<String>();
	private ArrayList<Bson> bsonObjs = new ArrayList<Bson>();
	private List<Bson> queryList = new ArrayList<Bson>();
	private Map<String, String> advMap = new LinkedHashMap<String, String>();
	
	/**
	 * Filter on the search query
	 * @return Bson object
	 */
	public Bson getFilter(){
		return this.filter;
	}
	/**
	 * Projection  for result
	 * @return Bson object
	 */
	public Bson getProjections(){
		return this.projections;
	}
	/**
	 * Sort information for query
	 * @return
	 */
	public Bson getSorts(){
		return this.sort;
	}
	/**
	 * Get list of queries for Aggregate function
	 * @return
	 */
	public List<Bson> getQueryList(){
		return queryList;
	}
	
	/**
	 * return pagination page size
	 * @return
	 */
	public int getPageSize(){
		return pagesize;
	}
	
	/**
	 * Parse the request query parameters
	 * @param serachparams 
	 */
	public void parseSearch(Map<String,String> serachparams ) {
		
		logger.info("Query parsing starts");
		if(!serachparams.entrySet().isEmpty()){
			for (Entry<String, String> entry : serachparams.entrySet()) {
				treatSearch(entry.getKey(), entry.getValue());
			}
			if(!advMap.isEmpty())
				advFilter(advMap);
		}
		logger.info("Query parsing ends");
	    //excludeId();
		createQuerylist();
		
	}
	
	/**
	 * This is to create aggregate of all the queries
	 */
	private void createQuerylist(){
		
		
		if(filter != null)
			queryList.add(Aggregates.match(filter));
		if(projections !=  null)
			queryList.add(Aggregates.project(projections));
		if(sort !=  null)
			queryList.add(Aggregates.sort(sort));
		if(pagenumber >= 0)
			queryList.add(Aggregates.skip(pagenumber> 0 ? ((pagenumber-1)*pagesize) : 0));
		if(pagesize > 0)
			queryList.add(Aggregates.limit(pagesize));
		
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	private void treatSearch(String key, String value){
		
		switch(key){
		
		case "searchphrase": 
			parseFilter(Filters.text(value));
			break;
		case "exclude":
			parseProjection(Projections.exclude(value.split(","))) ;
			break;
		case "include":
			parseProjection(Projections.include(value.split(","))) ;
			break;
		case "page": pagenumber = Integer.parseInt(value);
				break;
		case "size": pagesize = Integer.parseInt(value);
				break;
		case "sort.desc": 
			parseSorting(Sorts.descending(value.split(",")))	;
			break;
		case "sort.asc":
			parseSorting( Sorts.ascending(value.split(",")))	;
			break;	
		default:
			advMap.put(key,value);
			break;
		}
	}
	
	/**
	 * 
	 */
	 private void excludeId(){
		 if(projections != null)
				projections = Projections.fields(projections,Projections.excludeId());
		 else
			 projections = Projections.fields(Projections.excludeId());
	 }
	
	/**
	 * 
	 * @param projectionRequest
	 */
	private void parseProjection(Bson projectionRequest){
		if(projections != null)
			projections = Projections.fields(projections,projectionRequest);
		else
			projections = projectionRequest;
	} 
	
	/**
	 * 
	 * @param sortingRequest
	 */
	private void parseSorting(Bson sortingRequest){
		if(sort !=  null )
			sort = Sorts.orderBy(sort,sortingRequest);
		else
			sort = sortingRequest;
	}
	/**
	 * 
	 * @param filterRequest
	 */
	private void parseFilter(Bson filterRequest){
		if(filter == null)
			filter = filterRequest;
		else
			filter = Filters.or(filter, filterRequest);
	}
	
	/**
	 * Parse advancedquery key value paramters
	 * 
	 * @param map
	 * @return
	 */
	private void advFilter(Map<String, String> map) {
		if(!map.entrySet().isEmpty()){
			sortLogic(map);
		}
		if(logicalOps.isEmpty()){
			noLogicalOps();
		}else{
			withLogicalOps();
		}
	}
	
	/**
	 * check logical operators in advanced query and separate them from key,value pair
	 * @param map
	 */
	private void sortLogic(Map<String, String> map){
			for (Entry<String, String> entry : map.entrySet()) {
				
				if("logicalOp".equalsIgnoreCase(entry.getKey()))
					logicalOps.add(entry.getValue());
				else
					bsonObjs.add(Filters.regex(entry.getKey(), Pattern.compile(entry.getValue(),Pattern.CASE_INSENSITIVE)));
					
			}
			
	}
	
	/**
	 * sort the filters without logical operators
	 */
	private void noLogicalOps(){
		 //if(bsonObjs.size() > 1){
			  int i= 0;
			  while(i < bsonObjs.size()){
				  if(filter == null && bsonObjs.size() == 1){
					  filter = bsonObjs.get(0); 
					  i++;
				  }
				  else if(filter == null && bsonObjs.size() > 1){
						filter = Filters.and(bsonObjs.get(i),bsonObjs.get(i+1));
						i = i+2;
				  }
				  else{
						filter = Filters.and(filter,bsonObjs.get(i));
						i++;
				  }
			  }
		// }	 
	}
	/**
	 * Create filters with logical options
	 */
	private void withLogicalOps(){
		int j=0;
		for(int i = 0;i< logicalOps.size(); i++){
			switch(logicalOps.get(i).toLowerCase()){
				
				case "and":
					if(filter == null)
						filter = Filters.and(bsonObjs.get(j),bsonObjs.get(j+1));
					else
						filter = Filters.and(filter,bsonObjs.get(j));
					break;
				case "or":
					if(filter == null)
						filter = Filters.or(bsonObjs.get(j),bsonObjs.get(j+1));
					else
						filter = Filters.or(filter,bsonObjs.get(j));
					break;
				case "not":
					if(filter != null)
						filter = Filters.not(bsonObjs.get(j));
					else
						filter = Filters.and(filter,Filters.not(bsonObjs.get(j)));
					break;
				default:
					break;
			
		   }
			j++;
		}
	}
	
	
	///Functions for taxonomy
	
	public Bson parseTaxonomy(Map<String,String> serachparams ) {
		
		Bson taxFilter = null;
		ArrayList<Bson> bsonTax = new ArrayList<Bson>();
		if(!serachparams.entrySet().isEmpty()){
			for (Entry<String, String> entry : serachparams.entrySet()) {
				bsonTax.add(Filters.regex(entry.getKey(), Pattern.compile(entry.getValue(),Pattern.CASE_INSENSITIVE)));
			}
		}
		int i= 0;
		while(i < bsonTax.size()){
			if(taxFilter == null){
				taxFilter = Filters.and(bsonTax.get(i),bsonTax.get(i++));
				i=i+2;
			}else{
				taxFilter = Filters.and(taxFilter,bsonTax.get(i));
				i++;
			}
		}
			
//			for(int i = 0;i< bsonTax.size(); i++){
//				if(taxFilter == null){
//					taxFilter = Filters.and(bsonTax.get(i),bsonTax.get(i++));
//					i=i+2;
//				}else
//					taxFilter = Filters.and(taxFilter,bsonTax.get(i));
//				
//			}
		
		return taxFilter;
	}
//Not using right now	
//	private Map<String, String>  createMap(String advanced){
//		Map<String, String> map = new LinkedHashMap<String, String>();
//		for(String keyValue : advanced.split(" *& *")) {
//		   String[] pairs = keyValue.split(" *= *", 2);
//		   map.put(pairs[0], pairs.length == 1 ? "" : pairs[1]);
//		}
//		
//		return map;
//	}
}

//testquery
//Title=mytitle&logicalOp=AND&DOI=test&logicalOp=OR&filter=r&logicalOp=Not&Autor=testauthor  
//