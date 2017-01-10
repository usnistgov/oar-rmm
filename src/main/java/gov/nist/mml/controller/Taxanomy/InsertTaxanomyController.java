
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
package gov.nist.mml.controller.Taxanomy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.mml.domain.Taxanomy;
import gov.nist.mml.repositories.TaxanomyRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@RestController
@Api(value = "Test api for inserting data in the POD", tags = "Save API")
public class InsertTaxanomyController {
	
	private Logger logger = LoggerFactory.getLogger(InsertTaxanomyController.class);

	@Autowired
    private TaxanomyRepository taxanomyRepository;

	@Autowired
    public InsertTaxanomyController(TaxanomyRepository repo) { 
        taxanomyRepository = repo;
    }
	
	@ApiOperation(value = "Insert new entery in the database.",nickname = "save one")
	@RequestMapping(value = "/taxanomy/save", method = RequestMethod.POST, produces = "application/json")
	public Taxanomy savePod(@RequestBody Taxanomy taxanomy) {
		
		  logger.info("Taxanomy repository save data"+taxanomy);	
	      return taxanomyRepository.save(taxanomy);
	}
}
