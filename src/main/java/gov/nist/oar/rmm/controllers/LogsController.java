package gov.nist.oar.rmm.controllers;

import java.io.IOException;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.oar.rmm.repositories.LogRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(value = "API endpoints to search and update Logs.", tags = "Logs API")

@Validated
@RequestMapping("/logs")
public class LogsController {
    private Logger logger = LoggerFactory.getLogger(LogsController.class);

    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private LogRepository logRepo;

//    @RequestMapping(value = { "/{id}" }, method = RequestMethod.GET, produces = "application/json")
//    @ApiOperation(value = "Get Logs for NERDm record of given id.", nickname = "recordbyId", notes = "Resource returns a NERDm Record by given ediid.")
//    /**
//     * Get data for given record if and unique bundleid to get total number of bundle requests for this record.
//     * 
//     * @param id
//     * @return Returns Document
//     * @throws IOException
//     */
//    public Document record(@PathVariable @Valid String id) throws IOException {
//	logger.info("Get record by id: " + id);
//	return logRepo.findRecord(id);
//
//    }
//
//    /**
//     * Get record for given file.
//     * 
//     * @param id
//     * @return Returns Document
//     * @throws IOException
//     */
//    @RequestMapping(value = { "/fileinfo" }, method = RequestMethod.GET, produces = "application/json")
//    public Document fileInfo(@RequestParam  @Valid String filePath) throws IOException {
//	logger.info("Get record by filePath: " + filePath);
//	return logRepo.findFileInfo(filePath);
//
//    }
    
    /**
     * Get all the files list or request with specific id or filepath or any other field,value
     * @param params
     * @param p
     * @return
     * @throws IOException
     */
    @RequestMapping(value = { "/files" }, method = RequestMethod.GET, produces = "application/json")
    public Document search(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params,
    @ApiIgnore @PageableDefault(size = 150) Pageable p) throws IOException {
	logger.info("List all the files and provide sorting requirement: " + params);
	return logRepo.listfiles(params);
    }
    
    
    /**
     * Get information about bundles requests
     * Return list of unique bundles, sizes, dates 
     * 
     * @param id
     * @return Returns Document
     * @throws IOException
     */
    @RequestMapping(value = { "/bundles" }, method = RequestMethod.GET, produces = "application/json")
    public Document listBundles(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params,
	    @ApiIgnore @PageableDefault(size = 150) Pageable p) throws IOException {
	logger.info("Get list of bundles requests with sizes and dates " );
	return logRepo.listBundles(params);

    }
    
    /**
     * get the list of bundleplan requests
     * @param params
     * @param p
     * @return
     * @throws IOException
     */
    @RequestMapping(value = { "/bundleplans" }, method = RequestMethod.GET, produces = "application/json")
    public Document listBundlePlans(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params,
	    @ApiIgnore @PageableDefault(size = 150) Pageable p) throws IOException {
	logger.info("Get list of bundle planss requests with sizes and dates " );
	return logRepo.listBundlePlan(params);

    }
   
    /**
     * get the bundle plan summary
     * @param params
     * @param p
     * @return
     * @throws IOException
     */
    @RequestMapping(value = { "/bundlePlansSummary" }, method = RequestMethod.GET, produces = "application/json")
    public Document listBundlePlanSummary(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params,
	    @ApiIgnore @PageableDefault(size = 150) Pageable p) throws IOException {
	logger.info("Get list of bundle planss requests with sizes and dates " );
	return logRepo.findBundlePlanSummary(params);

    }
    
    /**
     * 
     * @param id
     * @return
     * @throws IOException
     */
    @RequestMapping(value = { "/downloadsPerRecord/{id}" }, method = RequestMethod.GET, produces = "application/json")
    public Document downloadsPerRecords(@PathVariable @Valid String id) throws IOException {
	logger.info("Get number of downloads per record "+id );
	return logRepo.getRecordDownloads(id);

    }
    

}
