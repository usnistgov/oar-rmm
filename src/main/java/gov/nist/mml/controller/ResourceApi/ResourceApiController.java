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
package gov.nist.mml.controller.ResourceApi;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.mml.controller.SearchController;
import gov.nist.mml.domain.ResourceApi;
import gov.nist.mml.domain.catalog;
import gov.nist.mml.repositories.ResourceApiRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dsn1
 *
 */
@RestController
@Api(value = "Get Resource api", tags = "Search ResourceApis")
public class ResourceApiController {

	private Logger logger = LoggerFactory.getLogger(ResourceApiController.class);
	
	@Autowired
    private ResourceApiRepository resourceapiRepository;
	
	@ApiOperation(value = "Get NIST open data api list.",nickname = "ResourceApi",
			  notes = "Returns the list of NIST API available publicly.")
	@RequestMapping(value = {"/ResourceApi"}, method = RequestMethod.GET, produces="application/json")
	public List<ResourceApi> Search ( )throws IOException {
		List<ResourceApi> apiEntries = resourceapiRepository.findAll();
		return apiEntries;
	}
}
