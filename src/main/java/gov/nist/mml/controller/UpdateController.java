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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gov.nist.mml.domain.Record;
import gov.nist.mml.repositories.RecordRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(value = "Test api for updating existing data entries", tags = "update API")
public class UpdateController {
	private Logger logger = LoggerFactory.getLogger(SearchController.class);

	@Autowired
    private RecordRepository recordRepository;

	@Autowired
    public UpdateController(RecordRepository repo) { 
        recordRepository = repo;
    }
	
	@ApiOperation(value = "Delete an entry from POD list",nickname = "deleteOne")
	@RequestMapping(value = "/catalog/records/{id}", method = RequestMethod.PUT, produces = "application/json")
	public String updateRecord(@PathVariable String id,@RequestBody Record updateRecord) {
		logger.info("Update Record.");
		try{
		Record recordToChange = recordRepository.findOne(id);
			   recordToChange.setAccessLevel(updateRecord.getAccessLevel());
			   recordToChange.setContactPoint(updateRecord.getContactPoint());
			   
	   	recordRepository.save(recordToChange);	   
		}catch(Exception ex){
			return "{\"Exception\":\" "+ex.getMessage()+"\"}";
		}
		finally{
			return "{\"Messgae\":\" Operation not allowed.\"}";
		}
	}
}
