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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
import org.springframework.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import gov.nist.mml.repositories.RecordRepository;
import gov.nist.mml.utilities.ProcessRequest;
import gov.nist.mml.domain.catalog;
import gov.nist.mml.domain.Record;
import gov.nist.mml.domain.nestedpod.ContactPoint;
import gov.nist.mml.domain.nestedpod.Distribution;
import gov.nist.mml.domain.nestedpod.Publisher;
import gov.nist.mml.exception.ResourceNotFoundException;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.query.Query.query;
import gov.nist.mml.repositories.CatalogRepository;;
@RestController
@Api(value = "Api for advanced search options", tags = "AdvancedSearch")
public class AdvancedSearchController {
	
	private Logger logger = LoggerFactory.getLogger(SearchController.class);

	@Autowired
    private RecordRepository RecordRepository;
	
	@Autowired
    private CatalogRepository catRepository;

	@Autowired
    public AdvancedSearchController(RecordRepository repo) { 
        RecordRepository = repo;
    }
	
	@Autowired
	MongoOperations mongoOps ;

	@Autowired
	private HttpServletRequest request;
	
	//*** This is the main /search method which accepts various kinds of request parameters including 
    // advanced search with logical operations on the columns/fields
    // This search api can take any key=value pair.
    // for logical operations "logicalOp" will accept and/or op
    // for searching any text use "searchPhrase" 
    /// This was added for SwaggerUI to show the options for pageable
    @ApiOperation(value = "Give combination of keywords  or key value pair to search with pagination and sorting.",
    		      nickname = "advancedsearch", notes="/advancedsearch api endpoint accepts any number of key=value pairs seperated by '&' operator."
    		      		+ " <br> <br> keys should represent field names in data.json"
    		      		+ " <br> <br> Logical operations can be performed between any number key=value pairs using "
    		      		+ "\"logicalOp\" operator."
    		      		+ "<br> <br> For Example:  <a href=\"/RMMApi/apidoc.html\" >Refer document</a>"
    		      		+ "<br> <br> Note: Please ignore 'p' field below, input your pagination values in page,size and sort fields."
    		      		+ "<br> <br> Note: Use tools like curl/postman to test this resource as swagger does not allow dynamic, variable number of key=value pairs as input.  ")
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
    @RequestMapping(value = {"/records/advancedsearch"}, method = RequestMethod.GET, produces="application/json")
	public List<Record> Search (@ApiIgnore Pageable p)//,@RequestParam @ApiIgnore  Map<String,String> params )
		   throws IOException {
    	/**
    	 * Following code is not needed but added for swagger-ui to work.
    	 * handlerequest can take directly params from method @requestparam option
    	 **/
    	Map<String,String[]> params = request.getParameterMap();
    	Map<String,String> requstParams = new HashMap<String,String>();
    	for (Map.Entry<String, String[]> entry : params.entrySet()) 
    	      requstParams.put(entry.getKey(), entry.getValue()[0]);
    
    	/***/

        ProcessRequest processRequest = new ProcessRequest();
        return mongoOps.find(processRequest.handleRequest(requstParams).with(p), Record.class);
    }
}
