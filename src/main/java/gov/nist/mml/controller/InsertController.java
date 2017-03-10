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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.mml.domain.ResourceApi;
import gov.nist.mml.domain.Record;
import gov.nist.mml.repositories.ResourceApiRepository;
import gov.nist.mml.repositories.RecordRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Test api for inserting data in the POD", tags = "Save API")
public class InsertController {
	
	private Logger logger = LoggerFactory.getLogger(SearchController.class);

	@Autowired
    private RecordRepository recordRepository;

	@Autowired
    private ResourceApiRepository resourceApiRepository;
	
	@Autowired
    public InsertController(RecordRepository repo) { 
        recordRepository = repo;
    }
	
	@ApiOperation(value = "Insert new entery in the database.",nickname = "save one")
	@RequestMapping(value = "/catalog/records/save", method = RequestMethod.POST, produces = "application/json")
	public Record savePod(@RequestBody Record record) {
	      //do something fancy
		 logger.info("adding new entry in Records"+record.toString());
	      return recordRepository.save(record);
	}
	
	@RequestMapping(value = "/ResourceApi/save", method = RequestMethod.POST, produces = "application/json")
	public ResourceApi saveApi(@RequestBody ResourceApi keydata) {
	      //do something fancy
		  logger.info("adding new entry in ResourceApi"+keydata.toString());
	      return resourceApiRepository.save(keydata);
	}
}
