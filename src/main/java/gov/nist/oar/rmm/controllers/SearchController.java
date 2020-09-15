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
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
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
@Api(value = "API endpoints to search EDI/PDL data", tags = "Search API")
/***
 * Main search controller. Searches data in Mongodb database.
 * 
 * @author Deoyani Nandrekar-Heinis
 */
@Validated
@RequestMapping("/")
public class SearchController {

	private Logger logger = LoggerFactory.getLogger(SearchController.class);

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private CustomRepository repo;

	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N)"),
			@ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page."),
			@ApiImplicitParam(name = "sort.desc", allowMultiple = true, dataType = "string", paramType = "query", value = "sort on the fields seperated by comma (one or more)."),
			@ApiImplicitParam(name = "sort.asc", allowMultiple = true, dataType = "string", paramType = "query", value = "Sort in ascending order seperated by comma (one or more).") })
	@ApiOperation(value = "Get/Search NERDm records.", nickname = "NERDm", notes = "Resource returns all the data if no request parameter mentioned."
			+ "\n following are some search query examples" + "\n 1. /records?searchphrase=<phrase or words>"
			+ "\n 2. /records?key1=value1&logicalOp=AND&key2=value2..."
			+ "\n 3. /records?searchphrase=<phrase or words>&key=value..." + "\n 4. /records?page=1&size=2"
			+ "\n 5. /recorsd?sort.desc=<field or comma seperated list of fields>")
	@RequestMapping(value = { "/records" }, method = RequestMethod.GET, produces = "application/json")
	/**
	 * Search the records repository, if no parameters given returns whole
	 * collection
	 * 
	 * @param params number of query parameters
	 * @param p      pagination
	 * @return Returns document containing result count and results array.
	 * @throws IOException
	 */
	public Document search(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params,
			@ApiIgnore @PageableDefault(size = 150) Pageable p) throws IOException {
		
		/**
		 * This particular snippet has been added because if there are same name keys are used the input parameters will be grouped in array 
		 * In that case the sequence from left to right was not kept as it is. In that case logicalOp was always after all the key,value pairs and position
		 * of logical operator was difficult to determine. 
		 * Hence the original query string is used for initial validation of the request.
		 */
		System.out.println("Request :"+request.getQueryString());
		String[] rParams = request.getQueryString().split("&");
		String prevParam = "";
		for(int i=0; i< rParams.length; i++) {
			String paramName = rParams[i].split("=")[0];
			if(prevParam.equalsIgnoreCase("logicalOp") && (paramName.equals("include") || paramName.equalsIgnoreCase("exclude"))) {
				 throw new IllegalArgumentException("check parameters, There should be key=value parameter after logicalOp.");
			 }
			 prevParam = paramName;			
		}


		logger.info(
				"Search request sent to" + request.getRequestURI() + " with query string:" + request.getQueryString());
		return repo.find(params);
	}

	@RequestMapping(value = { "/records/{id}" }, method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "Get NERDm record of given id.", nickname = "recordbyId", notes = "Resource returns a NERDm Record by given ediid.")
	/**
	 * Get record for given id
	 * 
	 * @param id
	 * @return Returns Document
	 * @throws IOException
	 */
	public Document record(@PathVariable @Valid String id) throws IOException {
		logger.info("Get record by id: " + id);
		return repo.findRecord(id);
	}

	@RequestMapping(value = {
			"/records/ark:/{naan:\\d+}/{id}" }, method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "Get NERDm record of given id.", nickname = "recordbyId", notes = "Resource returns a NERDm Record by given ark identifier.")
	/**
	 * Get record for given id
	 * 
	 * @param id   the local portion of an ARK identifier to match
	 * @param naan the ARK identifier's naming authority number (NAAN)
	 * @return Returns Document
	 * @throws IOException
	 */
	public Document record(@PathVariable @Valid String id, @PathVariable String naan) throws IOException {
		String ediid = "ark:/" + naan + "/" + id;
		logger.info("Get record by full ARK id: " + ediid);
		return repo.findRecord(ediid);
	}

	@RequestMapping(value = { "/records/fields" }, method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "Get all fields in the NERDm records.", nickname = "fieldnames", notes = "This resource returns NERDm fields. ** will be changed soon")
	/**
	 * Returns NERDm fields
	 * 
	 * @return Returns a set of fields
	 * @throws IOException
	 */
	public List<Document> recordFields() throws IOException {
		logger.info(
				" Fields request sent to" + request.getRequestURI() + " with query string:" + request.getQueryString());
		return repo.findFieldnames();
	}

	@RequestMapping(value = { "/taxonomy" }, method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "Get all taxonomy data.", nickname = "taxonomy", notes = "This resource returns all the taxonomy used in RMM service.")
	/**
	 * Search taxonomy collection
	 * 
	 * @param params
	 * @return returns List taxonomy
	 * @throws IOException
	 */
	public List<Document> serachTaxonomy(@ApiIgnore @RequestParam Map<String, String> params) throws IOException {
		logger.info("Taxonomy request sent to" + request.getRequestURI() + " with query string:"
				+ request.getQueryString());
		return repo.findtaxonomy(params);
	}

	@RequestMapping(value = { "/resourceApi" }, method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "Get all Resource apis.", nickname = "resourceApi", notes = "This will return other Resource apis available at NIST.")
	/**
	 * Search apis
	 * 
	 * @param params
	 * @return Returns list of searchApi
	 * @throws IOException
	 */
	public List<Document> searchApis() throws IOException {
		logger.info("Resource APIs request sent to" + request.getRequestURI() + " with query string:"
				+ request.getQueryString());
		return repo.findResourceApis();
	}

	/**
	 * Extra function for testing and other purposes.
	 * 
	 */
	@ApiIgnore
	@RequestMapping(value = { "/orig/records" }, method = RequestMethod.GET, produces = "application/json")
	/***
	 * Extra function for Testing etc
	 * 
	 * @param params
	 * @param p
	 * @return returns List Document
	 * @throws IOException
	 ***/
	public List<Document> extrasearch(@ApiIgnore @RequestParam Map<String, String> params,
			@ApiIgnore @PageableDefault(size = 1000) Pageable p) throws IOException {
		logger.debug("This is advanced search request:" + request);
		return repo.find(params, p);

	}

	/**
	 * TEST
	 * 
	 * @param ediid
	 * @param params
	 * @return
	 * @throws CustomizationException
	 * @throws InvalidInputException
	 */

	@RequestMapping(value = {
			"/test/{ediid}" }, method = RequestMethod.POST, headers = "accept=application/json", produces = "application/json")
	@ApiOperation(value = ".", nickname = "Cache Record Changes", notes = "Resource returns a record if it is editable and user is authenticated.")
	public Document updateRecord(@PathVariable @Valid String ediid, @Valid @RequestBody String params)
			throws Exception {

		logger.info("Update the given record: " + ediid);
		return repo.findRecord("4765EE7CC5D3A396E0531A57068160031688");

	}
}
