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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

/**
 * This is parse any request to Rest API endpoints.
 * 
 * @author Deoyani Nandrekar-Heinis
 *
 */
public class ProcessRequest {

	private Logger logger = LoggerFactory.getLogger(ProcessRequest.class);
	private Bson filter = null;
	private Bson projections = null;
	private Bson sort = null;
	private int pagenumber = 0;
	private int pagesize = 0;
	private ArrayList<String> logicalOps = new ArrayList<String>();
	private ArrayList<Bson> bsonObjs = new ArrayList<Bson>();
	private List<Bson> queryList = new ArrayList<Bson>();
//	private Map<String, String> advMap = new LinkedHashMap<String, String>();
	private Map<String, List<String>> advMap = new HashMap<String, List<String>>();
	private String include = "", exclude = "";
	private ArrayList<Bson> filtersList = new ArrayList<Bson>();
//	private  ArrayList<Bson> searchphraseFilter = new ArrayList<Bson>();
	private Bson searchphraseFilter = null;

	/**
	 * Filter on the search query
	 * 
	 * @return Bson object
	 */
	public List<Bson> getFilters() {
		return this.filtersList;
	}
	/**
	 * Filter on the search query
	 * 
	 * @return Bson object
	 */
	public Bson getFilter() {
		return this.filter;
	}

	/**
	 * Projection for result
	 * 
	 * @return Bson object
	 */
	public Bson getProjections() {
		return this.projections;
	}

	/**
	 * Sort information for query
	 * 
	 * @return
	 */
	public Bson getSorts() {
		return this.sort;
	}

	/**
	 * Get list of queries for Aggregate function
	 * 
	 * @return
	 */
	public List<Bson> getQueryList() {
		return queryList;
	}

	/**
	 * return pagination page size
	 * 
	 * @return
	 */
	public int getPageSize() {
		return pagesize;
	}

	/**
	 * Parse the request query parameters
	 * 
	 * @param serachparams
	 */
	public void parseSearch(MultiValueMap<String, String> serachparams) {
		boolean searchInput = false;
		// logger.info("Query parsing starts");
		if (!serachparams.entrySet().isEmpty()) {
			validateInput(serachparams);
			for (Entry<String, List<String>> entry : serachparams.entrySet()) {
				treatSearch(entry.getKey(), entry.getValue());
				if (entry.getKey().equalsIgnoreCase("searchphrase")) {
					if (entry.getValue().size() > 0) {
						searchInput = true;
					}
				}
			}
			validateProjections();
			if (!advMap.isEmpty())
				advFilter(advMap);
		}

		// logger.info("Query parsing ends");
		createQuerylist(searchInput);

	}

	/**
	 * Validate requested input parameters to check whether any forbidden syntax is
	 * used.
	 * 
	 * @param serachparams
	 */
	private void validateInput(MultiValueMap<String, String> serachparams) {
		
//		List<Integer> result = new ArrayList(serachparams.keySet());
//		System.out.println(result.get(result.size()-1));
//		if(result.get(result.size()-1).equals("logicalOp")) {
//			throw new IllegalArgumentException("check parameters, last parameter can not be a logical operator.");
//		}
		for (Entry<String, List<String>> entry : serachparams.entrySet()) {
			
			for (int i = 0; i < entry.getValue().size(); i++) {
				String value = entry.getValue().get(i);
				if (value.isEmpty())
					return;
				Pattern p = Pattern.compile("[^a-z0-9.,@_]", Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(value);
				switch (entry.getKey()) {
				case "exclude":
				case "include":
				case "sort.desc":
				case "sort.asc":
					if (m.find())
						throw new IllegalArgumentException("check parameter value for" + entry.getKey());
					break;
				case "page":
				case "size":
					if (m.find())
						throw new IllegalArgumentException("'page/size' can be only integers");
					else
						checkInteger(value);
					break;
				case "searchphrase":
				default:
					break;
				}
			}
		}

	}

	/***
	 * Check whether given value is integer.
	 * 
	 * @param value
	 */
	private void checkInteger(String value) {
		try {
			Integer.parseInt(value);
		} catch (Exception e) {
			logger.info("exception:" + e);
			throw new IllegalArgumentException("'page/size' value should be integer only.");
		}
	}

	/**
	 * This is to create aggregate of all the queries
	 */
	private void createQuerylist(Boolean searchInput) {

////		filtersList.add(Filters.text("Chemistry"));
////		queryList.add(Aggregates.match(Filters.text("Chemistry")));
////		queryList.add(Aggregates.match(bsonObjs.get(0)));
//		if(filter != null && searchphraseFilter != null && logicalOps.size() == 0) {
//			queryList.add(Aggregates.match(filter));
//			queryList.add(Aggregates.match(searchphraseFilter));
//		}
		if (filter != null)
			queryList.add(Aggregates.match(filter));
		if (projections != null) {
			queryList.add(Aggregates.project(projections));
		} else {
			// queryList.add(Aggregates.project(Projections.metaTextScore("score")));
		}

		if (sort != null) {
			queryList.add(Aggregates.sort(sort));
		} else {
			if (searchInput) {
				queryList.add(Aggregates.sort(Sorts.metaTextScore("score")));
			}
		}
		if (pagenumber >= 0)
			queryList.add(Aggregates.skip(pagenumber > 0 ? ((pagenumber - 1) * pagesize) : 0));
		if (pagesize > 0)
			queryList.add(Aggregates.limit(pagesize));

	}

	/**
	 * Depending on the search keyword and value pairs create filters to add to
	 * MongoDB query.
	 * 
	 * @param key
	 * @param value
	 */
	private void treatSearch(String key, List<String> value) {
		for (int i = 0; i < value.size(); i++) {
			if (value.get(i).isEmpty())
				return;

			switch (key) {

			case "searchphrase":
				parseFilter(Filters.text(value.get(i)));
//				parseFilter(value.get(i));
				break;
			case "exclude":
				exclude = value.get(i);
				// parseProjection(Projections.exclude(value.split(","))) ;
				break;
			case "include":
				include = value.get(i);
				// parseProjection(Projections.include(value.split(","))) ;
				break;
			case "page":
				pagenumber = Integer.parseInt(value.get(i));
				break;
			case "size":
				pagesize = Integer.parseInt(value.get(i));
				break;
			case "sort.desc":
				parseSorting(Sorts.descending(value.get(i).split(",")));
				break;
			case "sort.asc":
				parseSorting(Sorts.ascending(value.get(i).split(",")));
				break;
			default:
				updateMap(key, value.get(i));
				break;
			}
		}
	}

	private void updateMap(String key, String value) {
		List<String> valSet = new ArrayList<String>();
		boolean existingkey = false;
		for (Map.Entry<String, List<String>> entry : advMap.entrySet()) {
			String k = entry.getKey();
			List<String> val = entry.getValue();
			if (k.equalsIgnoreCase(key)) {
				val.add(value);
				valSet.addAll(val);
				existingkey = true;
			}

		}
		if (!existingkey)
			valSet.add(value);
		advMap.put(key, valSet);
	}

	/**
	 * Projections are MongoDB specific tools to help create complex queries .
	 */
	private void validateProjections() {
		if (!include.isEmpty() && !exclude.isEmpty()) {
			if ("_id".equalsIgnoreCase(exclude)) {
				projections = Projections.fields(Projections.include(include.split(",")), Projections.excludeId());
			} else if (!"_id".equalsIgnoreCase(exclude) && !exclude.isEmpty()) {
				throw new IllegalArgumentException(
						"if you exclude fields, you cannot also specify the inclusion of fields");
			}
		} else if (!include.isEmpty() && exclude.isEmpty()) {
			projections = Projections.include(include.split(","));
		} else if (include.isEmpty() && !exclude.isEmpty()) {
			projections = Projections.exclude(exclude.split(","));
		}
	}

//	/**
//	 * ExcludeId removes the MongoDB generated _id  field from result JSON document
//	 */
//	 private void excludeId(){
//		 if(projections != null)
//				projections = Projections.fields(projections,Projections.excludeId());
//		 else
//			 projections = Projections.fields(Projections.excludeId());
//	 }

//	/**
//	 * Add projections according to the requested parameters
//	 * @param projectionRequest
//	 */
//	private void parseProjection(Bson projectionRequest){
//		if(projections != null)
//			projections = Projections.fields(projections,projectionRequest);
//		else
//			projections = projectionRequest;
//	} 
//	
	/**
	 * Sort input request
	 * 
	 * @param sortingRequest
	 */
	private void parseSorting(Bson sortingRequest) {
		if (sort != null)
			sort = Sorts.orderBy(sort, sortingRequest);
		else
			sort = sortingRequest;
	}

	/**
	 * Parse Filters based on input parameters
	 * 
	 * @param filterRequest
	 */
	private void parseFilter(Bson filterRequest) {
		if (filter == null)
			filter = filterRequest;
		else
			filter = Filters.or(filter, filterRequest);
	}
	
	/**
	 * Parse Filters based on input parameters
	 * 
	 * @param filterRequest
	 */
	private void parseFilter(String searchphrase) {
		
		searchphraseFilter = Filters.text(searchphrase);
	}

	/**
	 * Parse advancedquery key value paramters
	 * 
	 * @param map
	 * @return
	 */
	private void advFilter(Map<String, List<String>> map) {
		if (!map.entrySet().isEmpty()) {
			sortLogic(map);
		}
		if (logicalOps.isEmpty()) {
			noLogicalOps();
		} else {
			withLogicalOps();
		}
	}

	/**
	 * check logical operators in advanced query and separate them from key,value
	 * pair
	 * 
	 * @param map
	 */
	private void sortLogic(Map<String, List<String>> map) {
		for (Entry<String, List<String>> entry : map.entrySet()) {
			List<String> values = entry.getValue();
			if ("logicalOp".equalsIgnoreCase(entry.getKey())) {
				for (int j = 0; j < values.size(); j++) {
				logicalOps.add(entry.getValue().get(j));
				}
			}
			else {
				
				for (int j = 0; j < values.size(); j++) {
//					String[] searchString = entry.getValue().split(",");
					String[] searchString = entry.getValue().get(j).split(",");

					List<Pattern> patternList = new ArrayList<Pattern>();

					for (int i = 0; i < searchString.length; i++) {
						patternList.add(Pattern.compile(searchString[i], Pattern.CASE_INSENSITIVE));
					}
					bsonObjs.add(Filters.in(entry.getKey(), patternList));
				}
			}
		}

	}

	/**
	 * sort the filters without logical operators
	 */
	private void noLogicalOps() {

		int i = 0;
		while (i < bsonObjs.size()) {
//			filtersList.add(filter);
//			filtersList.add(bsonObjs.get(i));
			if (filter == null && bsonObjs.size() == 1) {
				filter = bsonObjs.get(0);
				
				i++;
			} else if (filter == null && bsonObjs.size() > 1) {
				filter = Filters.or(bsonObjs.get(i), bsonObjs.get(i + 1));
				i = i + 2;
			} else {
				filter = Filters.or(filter, bsonObjs.get(i));
				i++;
			}
		}
//		if(this.searchphraseFilter != null)
//			filtersList.add(searchphraseFilter);
//		filtersList.add(filter);

	}

	/**
	 * Create filters with logical options
	 */
	private void withLogicalOps() {
		
		int j = 0;
		for (int i = 0; i < logicalOps.size(); i++) {
			switch (logicalOps.get(i).toLowerCase()) {

			case "and":
				if (filter == null)
					filter = Filters.and(bsonObjs.get(j), bsonObjs.get(j + 1));
				else
					filter = Filters.and(filter, bsonObjs.get(j));
				break;
			case "or":
				if (filter == null)
					filter = Filters.or(bsonObjs.get(j), bsonObjs.get(j + 1));
				else
					filter = Filters.or(filter, bsonObjs.get(j));
				break;
			case "not":
				if (filter == null)
					filter = Filters.not(bsonObjs.get(j));
				else
					filter = Filters.and(filter, Filters.not(bsonObjs.get(j)));
				break;
			default:
				break;

			}
			j++;
		}
	}

	/**
	 * Parse request to taxonomy resource and create mongodb query
	 * 
	 * @param serachparams
	 * @return
	 */
	public Bson parseTaxonomy(Map<String, String> serachparams) {

		Bson taxFilter = null;
		ArrayList<Bson> bsonTax = new ArrayList<Bson>();
		if (!serachparams.entrySet().isEmpty()) {
			for (Entry<String, String> entry : serachparams.entrySet()) {

				Pattern p = Pattern.compile("[^a-z0-9.,]", Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(entry.getValue());
				if (m.find())
					throw new IllegalArgumentException();
				int paramValue = -1;
				try {
					paramValue = Integer.parseInt(entry.getValue());
					bsonTax.add(Filters.eq(entry.getKey(), paramValue));
				} catch (Exception ex) {
					logger.info("parameter Value is not integer" + ex);

					bsonTax.add(
							Filters.regex(entry.getKey(), Pattern.compile(entry.getValue(), Pattern.CASE_INSENSITIVE)));
				}

			}
		}
		int i = 0;
		while (i < bsonTax.size()) {
			if (taxFilter == null && bsonTax.size() == 1) {
				taxFilter = bsonTax.get(i);
				i++;
			} else if (taxFilter == null) {
				taxFilter = Filters.and(bsonTax.get(i), bsonTax.get(i++));
				i = i + 2;
			} else {
				taxFilter = Filters.and(taxFilter, bsonTax.get(i));
				i++;
			}
		}

		return taxFilter;
	}
}