package gov.nist.oar.rmm.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import gov.nist.oar.rmm.repositories.MetricsRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(value = "API endpoints to search and access Metrics.", tags = "Metrics API")

@Validated
@RequestMapping("/usagemetrics")
public class MetricsController {
    private Logger logger = LoggerFactory.getLogger(MetricsController.class);

    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private MetricsRepository logRepo;

    
    @ApiImplicitParams({
	    @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N)"),
	    @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page."),
	    @ApiImplicitParam(name = "sort.desc", allowMultiple = true, dataType = "string", paramType = "query", value = "sort on the fields seperated by comma (one or more)."),
	    @ApiImplicitParam(name = "sort.asc", allowMultiple = true, dataType = "string", paramType = "query", value = "Sort in ascending order seperated by comma (one or more).") })
@ApiOperation(value = "Get the metrics for given dataset or list of datasets", nickname = "DataSetMetrics", notes = "Returns all the metrics information related to datasets."
	    + "\n following are some search query examples" + "\n 1. /records"
	    + "\n /records?page=1&size=2"
	    + "\n /recorsd?sort.desc=<field or comma seperated list of fields>")

    /**
     * Get list of Metrics of all the datasets/records 
     * @param recordid
     * @return
     */
    @RequestMapping(value = {"/records"  }, method = RequestMethod.GET, produces = "application/json")
     public Document searchRecord(@PathVariable(required = false) String recordid,
	    @ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params) {
	logger.info("Dataset level metrics");
//	if (recordid != null) recordid="";
	return logRepo.findRecord(recordid, params);
    }
    
    
    
    
    /**
     * Get record/dataset specific  metrics information
     * @param recordid
     * @return
     */
    @RequestMapping(value = {"/records/**"  }, method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get the metrics for given dataset or list of datasets", nickname = "DataSetMetrics", notes = "Returns all the metrics information related to given datasets.")
    
    public Document getRecord(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params) {
	logger.info("Dataset level metrics");
	
	String requestPath=(String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
	String recordid = "";
	String[] filepaths = requestPath.split("/");
	if(requestPath.contains("ark:")) {
	    recordid = filepaths[3]+"/"+filepaths[4]+"/"+filepaths[5];
	} else 
	    recordid = filepaths[3];

	return logRepo.findRecord(recordid, params);
    }
    
    /**
     * Get all the files in the repo and their usage metrics
     * @param recordid
     * @param params
     * @return
     */
    @RequestMapping(value = { "/files" }, method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "List all files and respective metrics information", nickname = "FileMetrics All", notes = "Returns all the metrics information related to files.")
    
    public Document listFile(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params) {
	logger.info("List of all files in this record/dataset and its usage analytics");
	String recordid = "", fileid ="";	
	return logRepo.findFile(recordid, fileid, params);
	
    }
    
    /**
     * Get the files from particular dataset or individual file related metrics with additional parameters 
     * @param recordid
     * @param params
     * @return
     */
    @RequestMapping(value = { "/files/**" }, method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get metrics for particular file or list of files in particualr dataset", nickname = "FileMetrics Selected", notes = "Returns all the metrics information related to particualr file or list of files from particular dataset.")
    
    public Document getFile(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params) {
	logger.info("List of all files in this record/dataset and its usage analytics");
	String filepath=(String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
	String recordid = "", fileid ="";
	String[] filepaths = filepath.split("/");
	if(filepath.contains("ark:")) {
	    recordid = filepaths[3]+"/"+filepaths[4]+"/"+filepaths[5];
	} else 
	    recordid = filepaths[3];

	if(filepath.contains("."))
	    fileid = filepath.substring(filepath.indexOf("/files")+7,filepath.length());
	return logRepo.findFile(recordid, fileid, params);
	
    }
    /**
     * Get the monthly repo level metrics, size and number of users
     * @param recordid
     * @param params
     * @return
     */
    @RequestMapping(value = { "/repo" }, method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Repository level metrics summary", nickname = "RepoMetrics", notes = "Returns all the metrics information related to complete repository.")
    
    public Document repoMetrics(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params) {
	logger.info("Repository level metrics summmary.");
	return logRepo.findRepo(params);
    }
    
    /**
     * Get the total users
     * @param recordid
     * @param params
     * @return
     */
    @RequestMapping(value = { "/totalusers" }, method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get the metrics for given dataset or list of datasets", nickname = "DataSetMetrics", notes = "Returns all the metrics information related to datasets.")
    
    public Document totalUsers(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params) {
	logger.info("Total unique users of the repository");
	return logRepo.totalUsers(params);
	
    }
    
//  /**
//   * Get all the files list or request with specific id or filepath or any other field,value
//   * @param params
//   * @param p
//   * @return
//   * @throws IOException
//   */
//  @RequestMapping(value = { "/files" }, method = RequestMethod.GET, produces = "application/json")
//  public Document search(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params,
//  @ApiIgnore @PageableDefault(size = 150) Pageable p) throws IOException {
//	logger.info("List all the files and provide sorting requirement: " + params);
//	return logRepo.listfiles(params);
//  }
  
//  /**
//   * Get all the files list or request with specific id or filepath or any other field,value
//   * or specific to record
//   * @param params
//   * @param p
//   * @return
//   * @throws IOException
//   */
//  @RequestMapping(value = { "/files/<recordid>/**" }, method = RequestMethod.GET, produces = "application/json")
//  public Document searchFiles( @PathVariable @Valid Optional<String> recordid, @PathVariable @Valid Optional<String> fileid,
//	    @ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params,
//  @ApiIgnore @PageableDefault(size = 150) Pageable p) throws IOException {
//	String recordId = "", fileId= request.getRequestURI();
//	if(recordid.isPresent()) recordId = recordid.get();
//	if(fileid.isPresent()) fileId = fileid.get();
//	logger.info("List all the files and provide sorting requirement: " + params);
//	return logRepo.listfiles(recordId,fileId, params);
//  }
//  
//  /**
//   * Get all the files list or request with specific id or filepath or any other field,value
//   * or specific to record
//   * @param params
//   * @param p
//   * @return
//   * @throws IOException
//   */
//  @RequestMapping(value = { "/files/ark:/{naan:\\\\d+}/{recordid}/<fileid>" }, method = RequestMethod.GET, produces = "application/json")
//  public Document searchFilesArkId( @PathVariable @Valid Optional<String> recordid, @PathVariable @Valid Optional<String> fileid,
//	    @ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params,
//  @ApiIgnore @PageableDefault(size = 150) Pageable p) throws IOException {
//	String recordId = "", fileId= "";
//	if(recordid.isPresent()) recordId = recordid.get();
//	if(fileid.isPresent()) fileId = fileid.get();
//	logger.info("List all the files and provide sorting requirement: " + params);
//	return logRepo.listfiles(recordId,fileId, params);
//  }
    
//    /**
//     * 
//     * @param id
//     * @return
//     * @throws IOException
//     */
//    @RequestMapping(value = { "/downloadsPerRecord/{id}" }, method = RequestMethod.GET, produces = "application/json")
//    public Document downloadsPerRecords(@PathVariable @Valid String id) throws IOException {
//	logger.info("Get number of downloads per record "+id );
//	return logRepo.getRecordDownloads(id);
//
//    }
    
    
//    /**
//     * Get information about bundles requests
//     * Return list of unique bundles, sizes, dates 
//     * 
//     * @param id
//     * @return Returns Document
//     * @throws IOException
//     */
//    @RequestMapping(value = { "/bundles" }, method = RequestMethod.GET, produces = "application/json")
//    public Document listBundles(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params,
//	    @ApiIgnore @PageableDefault(size = 150) Pageable p) throws IOException {
//	logger.info("Get list of bundles requests with sizes and dates " );
//	return logRepo.listBundles(params);
//
//    }
//    
//    /**
//     * get the list of bundleplan requests
//     * @param params
//     * @param p
//     * @return
//     * @throws IOException
//     */
//    @RequestMapping(value = { "/bundleplans" }, method = RequestMethod.GET, produces = "application/json")
//    public Document listBundlePlans(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params,
//	    @ApiIgnore @PageableDefault(size = 150) Pageable p) throws IOException {
//	logger.info("Get list of bundle planss requests with sizes and dates " );
//	return logRepo.listBundlePlan(params);
//
//    }
//   
//    /**
//     * get the bundle plan summary
//     * @param params
//     * @param p
//     * @return
//     * @throws IOException
//     */
//    @RequestMapping(value = { "/bundlePlansSummary" }, method = RequestMethod.GET, produces = "application/json")
//    public Document listBundlePlanSummary(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params,
//	    @ApiIgnore @PageableDefault(size = 150) Pageable p) throws IOException {
//	logger.info("Get list of bundle planss requests with sizes and dates " );
//	return logRepo.findBundlePlanSummary(params);
//
//    }
//    


}
