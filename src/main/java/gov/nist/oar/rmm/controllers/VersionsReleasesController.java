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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.oar.rmm.repositories.VersionReleasesetsRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Versions and Releases API", description = " These endpoints provide search functionality for public data repository metadata")
/***
 * Main search controller. Searches data in Mongodb database.
 * 
 * @author Deoyani Nandrekar-Heinis
 */
@Validated
@RequestMapping("/")
public class VersionsReleasesController {

    private Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private VersionReleasesetsRepository repo;

    @Parameters({ 
        @Parameter(name = "page", description = "Results page you want to retrieve (0..N)"),
        @Parameter(name = "size", description = "Number of records per page."),
        @Parameter(name = "sort.desc", description = "sort on the fields seperated by comma (one or more)."),
        @Parameter(name = "sort.asc", description = "Sort in ascending order seperated by comma (one or more).") })
    @Operation(summary = "Get/Search Versions dataset.", description = "Return all the versions available based on query criteria."
            + "\n following are some search query examples" + "\n 1. /versions?searchphrase=<phrase or words>"
            + "\n 2. /versions?key1=value1&logicalOp=AND&key2=value2..."
            + "\n 3. /versions?searchphrase=<phrase or words>&key=value..." + "\n 4. /records?page=1&size=2"
            + "\n 5. /versions?sort.desc=<field or comma seperated list of fields>")
    @RequestMapping(value = { "/versions" }, method = RequestMethod.GET, produces = "application/json")
    /**
     * Search versions dataset for given record or dataset identifier.
     * 
     * @param params number of query parameters
     * @param p      pagination
     * @return Returns document containing record/document for specific version
     * @throws IOException
     */
    public Document searchVersions(@Parameter(hidden = true) @Valid @RequestParam MultiValueMap<String, String> params,
            @Parameter(hidden = true) @PageableDefault(size = 150) Pageable p) throws IOException {

        /**
         * This particular snippet has been added because if there are same name keys
         * are used the input parameters will be grouped in array In that case the
         * sequence from left to right was not kept as it is. In that case logicalOp was
         * always after all the key,value pairs and position of logical operator was
         * difficult to determine. Hence the original query string is used for initial
         * validation of the request.
         */
        if (request.getQueryString() != null) {
            String[] rParams = request.getQueryString().split("&");
            String prevParam = "";
            for (int i = 0; i < rParams.length; i++) {
                String paramName = rParams[i].split("=")[0];
                if (prevParam.equalsIgnoreCase("logicalOp")
                        && (paramName.equals("include") || paramName.equalsIgnoreCase("exclude"))) {
                    throw new IllegalArgumentException(
                            "check parameters, There should be key=value parameter after logicalOp.");
                }
                prevParam = paramName;
            }
        }

        logger.info(
                "Search request sent to" + request.getRequestURI() + " with query string:" + request.getQueryString());
        return repo.findVersion(params, p);
    }

    @RequestMapping(value = { "/versions/{id}" }, method = RequestMethod.GET, produces = "application/json")
    @Operation(summary = "Get NERDm record of given id.", description = "Resource returns a NERDm Record by given ediid.")
    /**
     * Get versions data for given identifier
     * 
     * @param id
     * @return Returns Document
     * @throws IOException
     */
    public Document getRecordVersion(@PathVariable @Valid String id) throws IOException {
        logger.info("Get record by id: " + id);
        return repo.findVersion(id);
    }

    @RequestMapping(value = {
            "/versions/ark:/{naan:\\d+}/{id:.+}" }, method = RequestMethod.GET, produces = "application/json")
    @Operation(summary = "Get NERDm record of given id.", description = "Resource returns a NERDm Record by given ark identifier.")
    /**
     * Get versions data for given identifier
     * 
     * @param id   the local portion of an ARK identifier to match
     * @param naan the ARK identifier's naming authority number (NAAN)
     * @return Returns Document
     * @throws IOException
     */
    public Document getVersion(@PathVariable @Valid String id, @PathVariable String naan) throws IOException {
        String ediid = "ark:/" + naan + "/" + id;
        logger.info("Get record by full ARK id: " + ediid);
        return repo.findVersion(ediid);
    }

    @Parameters({ 
        @Parameter(name = "page", description = "Results page you want to retrieve (0..N)"),
        @Parameter(name = "size", description = "Number of records per page."),
        @Parameter(name = "sort.desc", description = "sort on the fields seperated by comma (one or more)."),
        @Parameter(name = "sort.asc", description = "Sort in ascending order seperated by comma (one or more).") })
    @Operation(summary = "Search Releasesets repository.", description = "Returns all the documents in Releaseset collection based on input criteria."
            + "\n following are some search query examples" + "\n 1. /releaseSets?searchphrase=<phrase or words>"
            + "\n 2. /releaseSets?key1=value1&logicalOp=AND&key2=value2..."
            + "\n 3. /releaseSets?searchphrase=<phrase or words>&key=value..." + "\n 4. /records?page=1&size=2"
            + "\n 5. /releaseSets?sort.desc=<field or comma seperated list of fields>")
    @RequestMapping(value = { "/releaseSets", "/releasesets" }, method = RequestMethod.GET, produces = "application/json")
    /**
     * Search the Releassets repository, if no parameters given returns whole
     * collection
     * 
     * @param params number of query parameters
     * @param p      pagination
     * @return Returns document containing result count and results array.
     * @throws IOException
     */
    public Document serachReleasesets(
            @Parameter(hidden = true) @Valid @RequestParam MultiValueMap<String, String> params,
            @Parameter(hidden = true) @PageableDefault(size = 150) Pageable p) throws IOException {

        /**
         * This particular snippet has been added because if there are same name keys
         * are used the input parameters will be grouped in array In that case the
         * sequence from left to right was not kept as it is. In that case logicalOp was
         * always after all the key,value pairs and position of logical operator was
         * difficult to determine. Hence the original query string is used for initial
         * validation of the request.
         */
        if (request.getQueryString() != null) {
            String[] rParams = request.getQueryString().split("&");
            String prevParam = "";
            for (int i = 0; i < rParams.length; i++) {
                String paramName = rParams[i].split("=")[0];
                if (prevParam.equalsIgnoreCase("logicalOp")
                        && (paramName.equals("include") || paramName.equalsIgnoreCase("exclude"))) {
                    throw new IllegalArgumentException(
                            "check parameters, There should be key=value parameter after logicalOp.");
                }
                prevParam = paramName;
            }
        }

        logger.info(
                "Search request sent to" + request.getRequestURI() + " with query string:" + request.getQueryString());
        return repo.findReleaseset(params, p);
    }

    @RequestMapping(value = { "/releaseSets/{id}" }, method = RequestMethod.GET, produces = "application/json")
    @Operation(summary = "Get NERDm record of given id.", description = "Resource returns a NERDm Record by given ediid.")
    /**
     * Get ReleaseSet for given id to get all the versions
     * 
     * @param id
     * @return Returns Document representing ReleaseSets
     * @throws IOException
     */
    public Document getReleaseSets(@PathVariable @Valid String id) throws IOException {
        logger.info("Get record by id: " + id);
        return repo.findReleaseset(id);
    }

    @RequestMapping(value = {
            "/releaseSets/ark:/{naan:\\d+}/{id:.+}" }, method = RequestMethod.GET, produces = "application/json")
    @Operation(summary = "Get NERDm record of given id.", description = "Resource returns a NERDm Record by given ark identifier.")
    /**
     * Get all the ReleaseSet for given record id
     * 
     * @param id   the local portion of an ARK identifier to match
     * @param naan the ARK identifier's naming authority number (NAAN)
     * @return Returns Document representing Releasesets
     * @throws IOException
     */
    public Document getReleaseSets(@PathVariable @Valid String id, @PathVariable String naan) throws IOException {
        String ediid = "ark:/" + naan + "/" + id;
        logger.info("Get record by full ARK id: " + ediid);
        return repo.findReleaseset(ediid);
    }
}
