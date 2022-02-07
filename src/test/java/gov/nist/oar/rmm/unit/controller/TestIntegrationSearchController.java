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
package gov.nist.oar.rmm.unit.controller;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import gov.nist.oar.rmm.config.AppConfig;
import gov.nist.oar.rmm.config.MongoConfig;
import gov.nist.oar.rmm.controllers.SearchController;
import gov.nist.oar.rmm.repositories.CustomRepository;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SearchController.class)
@ContextConfiguration(classes = AppConfig.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@TestPropertySource("classpath:bootstrap-test.yml")
public class TestIntegrationSearchController {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
			MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	 @Autowired
	  private MockMvc mockMvc;

	 @MockBean
	    MongoConfig mongoConfig;
	 
	 @MockBean
	 private CustomRepository customRepo;
	 
	 @Autowired
	 private SearchController searchController;

	@Test
	public void shouldFindRecords() throws Exception {
		mockMvc.perform(get("/records"))
		.andExpect(status().is(200));
//		.andExpect(jsonPath("$.ResultData.[0].title", is("NIST Bibliographic Database on Atomic Energy Levels and Spectra - SRD 169")))
//		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//		.andExpect(jsonPath("$.ResultCount",is(134)));
	}
	
	@Test
	public void shouldFindTaxonomyBylevel() throws Exception {
		mockMvc.perform(get("/taxonomy?level=1"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void shouldFindByTitle() throws Exception {
		mockMvc.perform(get("/records?title=Enterprise"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void shouldFindRecordById() throws Exception {
		mockMvc.perform(get("/records?@id=ark:/88434/pdr02d58"))
		.andExpect(status().is(200));
//		.andDo(print())
//		.andExpect(jsonPath("$.ResultData.[0].title", is("NIST-JANAF Thermochemical Tables - SRD 13")));
	}
	@Test
	public void shouldFindTaxonomy() throws Exception {
		mockMvc.perform(get("/taxonomy"))
		.andExpect(status().is(200))
		.andDo(print());
//		.andExpect(jsonPath("$[0].term", is("Advanced Communications")));
	}

	@Test
	public void shouldFindRecordFields() throws Exception {
		mockMvc.perform(get("/records/fields"))
		.andExpect(status().is(200))
		.andDo(print());
//		.andExpect(jsonPath("$[0].name", is("@type")));
	}
	
	@Test
	public void shouldFindResourceApis() throws Exception {
		mockMvc.perform(get("/resourceApi"))
		.andExpect(status().is(200))
		.andDo(print());
//		.andExpect(jsonPath("$[0].name", is("NIST Enterprise Data Inventory (EDI)")));
	}
}
