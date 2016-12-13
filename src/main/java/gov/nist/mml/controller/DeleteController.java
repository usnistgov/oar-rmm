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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Test api to Delete data from database", tags = "Delete API")
public class DeleteController {
	
	private Logger logger = LoggerFactory.getLogger(SearchController.class);
/**
 * Following code can be enabled once we decide to enable deleting records feature
 * @Autowired
    private RecordRepository recordRepository;

	@Autowired
    public DeleteController(RecordRepository repo) { 
        recordRepository = repo;
    }
 */
	
	
	@ApiOperation(value = "Delete an entry from POD list",nickname = "deleteOne")
	@RequestMapping(value = "/catalog/records/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public String deleteRecord(@PathVariable String id) {
		logger.info("Delete record"+id);
	     /**
	      * Only authenticated users should be able to do this.
	      * Enable this when authentication is implemented
	      *recordRepository.delete(id);
	      */
	     return "{\"Messgae\":\" Operation not allowed.\"}";
	}
	
	@ApiOperation(value = "Delete all entries from POD list",nickname = "deleteAll")
	@RequestMapping(value = "/catalog/records/deleteall", method = RequestMethod.DELETE, produces = "application/json")
	public String deleteAll() {
	      logger.info("Delete All");
	      
	      /**
		      * Only authenticated users should be able to do this.
		      * Enable this when authentication is implemented
		      * recordRepository.deleteAll();
		      */

	      return "{\"Messgae\":\" Operation not allowed.\"}";
	}
}