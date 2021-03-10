package gov.nist.oar.rmm.controllers;

import java.io.IOException;
import java.util.Optional;

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
import org.springframework.web.servlet.HandlerMapping;

import gov.nist.oar.rmm.repositories.MetricsRepository;
import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(value = "API endpoints to search and update Logs.", tags = "Logs API")

@Validated
@RequestMapping("/usagemetrics")
public class MetricsController {
    private Logger logger = LoggerFactory.getLogger(MetricsController.class);

    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private MetricsRepository logRepo;

    
//    /**
//     * Get all the files list or request with specific id or filepath or any other field,value
//     * @param params
//     * @param p
//     * @return
//     * @throws IOException
//     */
//    @RequestMapping(value = { "/files" }, method = RequestMethod.GET, produces = "application/json")
//    public Document search(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params,
//    @ApiIgnore @PageableDefault(size = 150) Pageable p) throws IOException {
//	logger.info("List all the files and provide sorting requirement: " + params);
//	return logRepo.listfiles(params);
//    }
    
//    /**
//     * Get all the files list or request with specific id or filepath or any other field,value
//     * or specific to record
//     * @param params
//     * @param p
//     * @return
//     * @throws IOException
//     */
//    @RequestMapping(value = { "/files/<recordid>/**" }, method = RequestMethod.GET, produces = "application/json")
//    public Document searchFiles( @PathVariable @Valid Optional<String> recordid, @PathVariable @Valid Optional<String> fileid,
//	    @ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params,
//    @ApiIgnore @PageableDefault(size = 150) Pageable p) throws IOException {
//	String recordId = "", fileId= request.getRequestURI();
//	if(recordid.isPresent()) recordId = recordid.get();
//	if(fileid.isPresent()) fileId = fileid.get();
//	logger.info("List all the files and provide sorting requirement: " + params);
//	return logRepo.listfiles(recordId,fileId, params);
//    }
//    
//    /**
//     * Get all the files list or request with specific id or filepath or any other field,value
//     * or specific to record
//     * @param params
//     * @param p
//     * @return
//     * @throws IOException
//     */
//    @RequestMapping(value = { "/files/ark:/{naan:\\\\d+}/{recordid}/<fileid>" }, method = RequestMethod.GET, produces = "application/json")
//    public Document searchFilesArkId( @PathVariable @Valid Optional<String> recordid, @PathVariable @Valid Optional<String> fileid,
//	    @ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params,
//    @ApiIgnore @PageableDefault(size = 150) Pageable p) throws IOException {
//	String recordId = "", fileId= "";
//	if(recordid.isPresent()) recordId = recordid.get();
//	if(fileid.isPresent()) fileId = fileid.get();
//	logger.info("List all the files and provide sorting requirement: " + params);
//	return logRepo.listfiles(recordId,fileId, params);
//    }
    /**
     * Get the files metrics for given recordid/dataset id
     * @param recordid
     * @return
     */
    @RequestMapping(value = { "/records/{recordid}" }, method = RequestMethod.GET, produces = "application/json")
    public Document searchRecord(@PathVariable @Valid String recordid,
	    @ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params) {
	logger.info("Dataset level metrics");
	return logRepo.findRecord(recordid, params);
    }
    
    /**
     * Get the monthly repo level metrics, size and number of users
     * @param recordid
     * @param params
     * @return
     */
//    @RequestMapping(value = { "/filemetrics/{recordid}/{fileid:.+}", "/filemetrics",  "/filemetrics/{recordid}/**" , "/filemetrics/{recordid}/**{}"}, method = RequestMethod.GET, produces = "application/json")
    @RequestMapping(value = { "/filemetrics/**" }, method = RequestMethod.GET, produces = "application/json")
    public Document listFiles(@PathVariable(required = false) String recordid, @PathVariable(required = false) String fileid,@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params) {
	logger.info("List of all files in this record/dataset and its usage analytics");
	String filepath=(String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
//	filepath = filepath.substring("/filemetrics".length() + recordid.length());
	return logRepo.findFile(recordid, fileid, params);
	
    }
    /**
     * Get the monthly repo level metrics, size and number of users
     * @param recordid
     * @param params
     * @return
     */
    @RequestMapping(value = { "/repometrics" }, method = RequestMethod.GET, produces = "application/json")
    public Document totalSize(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params) {
	logger.info("List of all files in this record/dataset and its usage analytics");
	return logRepo.totalSize(params);
    }
    
    /**
     * Get the total users
     * @param recordid
     * @param params
     * @return
     */
    @RequestMapping(value = { "/totalusers" }, method = RequestMethod.GET, produces = "application/json")
    public Document totalUsers(@ApiIgnore @Valid @RequestParam MultiValueMap<String, String> params) {
	logger.info("List of all files in this record/dataset and its usage analytics");
	return logRepo.totalUsers(params);
	
    }
    
    
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
