package gov.nist.oar.rmm.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.oar.rmm.repositories.LogRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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

    @RequestMapping(value = { "/{id}" }, method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get Logs for NERDm record of given id.", nickname = "recordbyId", notes = "Resource returns a NERDm Record by given ediid.")
    /**
     * Get record for given id
     * 
     * @param id
     * @return Returns Document
     * @throws IOException
     */
    public Document record(@PathVariable @Valid String id) throws IOException {
	logger.info("Get record by id: " + id);
	return logRepo.findRecord(id);

    }

    /**
     * Get record for given id
     * 
     * @param id
     * @return Returns Document
     * @throws IOException
     */
    public Document fileInfo(@PathVariable @Valid String filePath) throws IOException {
	logger.info("Get record by filePath: " + filePath);
	return logRepo.findFileInfo(filePath);

    }

}
