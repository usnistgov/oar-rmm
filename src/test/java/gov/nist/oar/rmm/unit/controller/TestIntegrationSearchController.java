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

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import gov.nist.oar.rmm.config.AppConfig;
import gov.nist.oar.rmm.controllers.SearchController;
import gov.nist.oar.rmm.repositories.CustomRepository;
import static org.hamcrest.core.Is.is;
import java.nio.charset.Charset;

import javax.inject.Inject;


@RunWith(SpringJUnit4ClassRunner.class)
//@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration
@SpringBootTest(classes = AppConfig.class)
//@WebAppConfiguration
public class TestIntegrationSearchController {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
			MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	private  MockMvc mockMvc;

	@Autowired
	private CustomRepository customRepo;

	@Autowired
	private SearchController searchController;
    
    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
    
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(searchController)
				.setCustomArgumentResolvers(pageableArgumentResolver).build();
	}
	
	@Test
	public void shouldFindRecords() throws Exception {
		mockMvc.perform(get("/records"))
		.andExpect(status().is(200))
		.andExpect(jsonPath("$.ResultData.[0].title", is("NIST Bibliographic Database on Atomic Energy Levels and Spectra - SRD 169")))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.ResultCount",is(134)));
	}
	
	@Test
	public void shouldFindTaxonomyBylevel() throws Exception {
		mockMvc.perform(get("/taxonomy?level=1"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void shouldFindByTitle() throws Exception {
		mockMvc.perform(get("/records?title=Enterprise"))
		.andExpect(jsonPath("$.ResultData.[0].title", is("Enterprise Data Inventory")))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk());
	}
	
	@Test
	public void shouldFindRecordById() throws Exception {
		mockMvc.perform(get("/records?@id=ark:/88434/pdr02d58"))
		.andExpect(status().is(200))
		.andDo(print())
		.andExpect(jsonPath("$.ResultData.[0].title", is("NIST-JANAF Thermochemical Tables - SRD 13")));
	}
	@Test
	public void shouldFindTaxonomy() throws Exception {
		mockMvc.perform(get("/taxonomy"))
		.andExpect(status().is(200))
		.andDo(print())
		.andExpect(jsonPath("$[0].term", is("Advanced Communications")));
	}

	@Test
	public void shouldFindRecordFields() throws Exception {
		mockMvc.perform(get("/records/fields"))
		.andExpect(status().is(200))
		.andDo(print())
		.andExpect(jsonPath("$[0].name", is("@type")));
	}
	
	@Test
	public void shouldFindResourceApis() throws Exception {
		mockMvc.perform(get("/resourceApi"))
		.andExpect(status().is(200))
		.andDo(print())
		.andExpect(jsonPath("$[0].name", is("NIST Enterprise Data Inventory (EDI)")));
	}
}
