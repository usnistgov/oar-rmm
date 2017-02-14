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
package gov.nist.oar.rmm.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.oar.rmm.repositories.CustomRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@Api(value = "Api endpoints to search EDI/PDL data", tags = "Search API")
public class SearchController {
	
	private Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private CustomRepository repo;

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
				  notes = "This will all the data if no request parameter mentioned.")
	@RequestMapping(value = {"/records"}, method = RequestMethod.GET, produces="application/json")
	public Document search(@ApiIgnore @RequestParam Map<String, String> params, @ApiIgnore @PageableDefault(size=150) Pageable p) throws IOException{
		logger.info("This is advanced search request:"+request);
	   
		return repo.find(params);
    	
	}
	
	@RequestMapping(value = {"/records/{id}"}, method = RequestMethod.GET, produces="application/json")
	@ApiOperation(value = "Get record of given id.",nickname = "recordbyId",
	  notes = "This will return a Record by given ID.")
	public Document record(@PathVariable String id) throws IOException{
		logger.info("Get record by id:"+request);
		return repo.findRecord(id);
	}
	
	
	@RequestMapping(value = {"/records/fields"}, method = RequestMethod.GET, produces="application/json")
	@ApiOperation(value = "Get all fields in the document.",nickname = "fieldnames",
	  notes = "This will return other Resource apis available at NIST.")
	public Set<String> recordFields() throws IOException{
		logger.info("Record fields names:"+request);
		return repo.findFieldnames();
	}

	@RequestMapping(value = {"/taxonomy"}, method = RequestMethod.GET, produces="application/json")
	@ApiOperation(value = "Get all taxonomy data.",nickname = "taxonomy",
	  notes = "This will return all the taxonomy used in RMM service.")
	public List<Document> serachTaxonoy(Map<String,String> params) throws IOException{
		logger.info("This is taxonomy:"+request);
		return repo.findtaxonomy(params);
	}
	
	@RequestMapping(value = {"/resourceApi"}, method = RequestMethod.GET, produces="application/json")
	@ApiOperation(value = "Get all Resource apis.",nickname = "resourceApi",
	  notes = "This will return other Resource apis available at NIST.")
	public List<Document> searchApis(Map<String,String> params) throws IOException{
		logger.info("This is resourceApi:"+request);
		return repo.findResourceApis();
	}
	
	
	
	/**
	 * Extra function for testing and other purposes
	 * 
	 */
	
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
				  notes = "This will all the data if no request parameter mentioned.")
	@RequestMapping(value = {"/orig/records"}, method = RequestMethod.GET, produces="application/json")
	public List<Document> extrasearch(@ApiIgnore @RequestParam Map<String, String> params, @ApiIgnore @PageableDefault(size=1000) Pageable p) throws IOException{
		logger.info("This is advanced search request:"+request);
	   
		return repo.find(params,p);
    	
	}
	

}
