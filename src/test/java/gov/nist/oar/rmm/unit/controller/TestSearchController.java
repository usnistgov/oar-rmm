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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.nist.oar.rmm.config.AppConfig;
import gov.nist.oar.rmm.config.MongoConfig;
import gov.nist.oar.rmm.controllers.SearchController;
import gov.nist.oar.rmm.repositories.CustomRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SearchController.class)
@ContextConfiguration(classes = AppConfig.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@TestPropertySource("classpath:application-test.yml")
public class TestSearchController {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
			MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	 @Autowired
	  private MockMvc mockMvc;
	 
	 @Autowired
	  private ObjectMapper objectMapper;
	 
	 @MockBean
	    MongoConfig mongoConfig;
	 
	 @MockBean
	 private CustomRepository customRepo;

	@Test
	public void shouldFindRecords() throws Exception {
		
		String recordTest = "";
		try {
			recordTest = new String ( Files.readAllBytes( Paths.get(this.getClass().getClassLoader().getResource("recordTest.json").getFile()) ) );
		}catch(Exception exp) {
			System.out.print(exp.getMessage());
		}
		Document record = Document.parse(recordTest);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		when(customRepo.find(params)).thenReturn(record);
		mockMvc.perform(get("/records"))
		.andExpect(status().is(200))
		.andExpect(jsonPath("$.ResultData.[0].title", is("NIST Bibliographic Database on Atomic Energy Levels and Spectra - SRD 169")))
		.andExpect(content().contentType("application/json"))
		.andExpect(jsonPath("$.ResultCount",is(5)));
	}
	
	@Test
	public void shouldFindTaxonomyBylevel() throws Exception {
		
		JSONParser parser = new JSONParser();
		List<Document> listTaxonomy = new LinkedList<Document>();
    	JSONArray a;
    	File file = new File(this.getClass().getClassLoader().getResource("taxonomy.json").getFile());
		
    	
    	try {
			a = (JSONArray) parser.parse(new FileReader(file));
			for (Object o : a) {
				Document doc = Document.parse(o.toString());
				listTaxonomy.add(doc);
			}
		} catch (IOException | ParseException e) {

			e.printStackTrace();
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("level", "1");
		when(customRepo.findtaxonomy(params)).thenReturn(listTaxonomy);
		mockMvc.perform(get("/taxonomy?level=1"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void shouldFindByTitle() throws Exception {
		
		String recordTest = "";
		try {
			recordTest = new String ( Files.readAllBytes( Paths.get(this.getClass().getClassLoader().getResource("recordTest.json").getFile()) ) );
		}catch(Exception exp) {
			System.out.print(exp.getMessage());
		}
		Document record = Document.parse(recordTest);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("title", "NIST Bibliographic");
		when(customRepo.find(params)).thenReturn(record);
		mockMvc.perform(get("/records?title=NIST Bibliographic"))
		.andExpect(jsonPath("$.ResultData.[0].title", is("NIST Bibliographic Database on Atomic Energy Levels and Spectra - SRD 169")))
		.andExpect(content().contentType("application/json"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void shouldFindRecordById() throws Exception {
		
		String recordTest = "";
		try {
			recordTest = new String ( Files.readAllBytes( Paths.get(this.getClass().getClassLoader().getResource("recordTest.json").getFile()) ) );
		}catch(Exception exp) {
			System.out.print(exp.getMessage());
		}
		Document record = Document.parse(recordTest);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("@id", "ark:/88434/pdr02j5x");
		when(customRepo.find(params)).thenReturn(record);
		
		mockMvc.perform(get("/records?@id=ark:/88434/pdr02j5x"))
		.andExpect(status().is(200))
		.andDo(print())
		.andExpect(jsonPath("$.ResultData.[0].title", is("NIST Bibliographic Database on Atomic Energy Levels and Spectra - SRD 169")));
	}
	@Test
	public void shouldFindTaxonomy() throws Exception {
		JSONParser parser = new JSONParser();
		List<Document> listTaxonomy = new LinkedList<Document>();
    	JSONArray a;
    	File file = new File(this.getClass().getClassLoader().getResource("taxonomy.json").getFile());
		
    	
    	try {
			a = (JSONArray) parser.parse(new FileReader(file));
			for (Object o : a) {
				Document doc = Document.parse(o.toString());
				listTaxonomy.add(doc);
			}
		} catch (IOException | ParseException e) {

			e.printStackTrace();
		}
    	Map<String, String> params = new HashMap<String, String>();
		when(customRepo.findtaxonomy(params)).thenReturn(listTaxonomy);
		mockMvc.perform(get("/taxonomy"))
		.andExpect(status().is(200))
		.andDo(print())
		.andExpect(jsonPath("$[0].term", is("Advanced Communications")));
	}

	@Test
	public void shouldFindRecordFields() throws Exception {
		
		JSONParser parser = new JSONParser();
		List<Document> fieldsTest = new LinkedList<Document>();
    	JSONArray a;
    	File file = new File(this.getClass().getClassLoader().getResource("fields.json").getFile());
		
    	
    	try {
			a = (JSONArray) parser.parse(new FileReader(file));
			for (Object o : a) {
				Document doc = Document.parse(o.toString());
				fieldsTest.add(doc);
			}
		} catch (IOException | ParseException e) {

			e.printStackTrace();
		}

		when(customRepo.findFieldnames()).thenReturn(fieldsTest);
		mockMvc.perform(get("/records/fields"))
		.andExpect(status().is(200))
		.andDo(print())
		.andExpect(jsonPath("$[0].name", is("@type")));
	}
	
	@Test
	public void shouldFindResourceApis() throws Exception {
		
		JSONParser parser = new JSONParser();
		List<Document> resourcesTest = new LinkedList<Document>();
    	JSONArray a;
    	File resources = new File(this.getClass().getClassLoader().getResource("resourceApi.json").getFile());
		
    	
    	try {
			a = (JSONArray) parser.parse(new FileReader(resources));
			for (Object o : a) {
				Document doc = Document.parse(o.toString());
				resourcesTest.add(doc);
			}
		} catch (IOException | ParseException e) {

			e.printStackTrace();
		}
    	
    	when(customRepo.findResourceApis()).thenReturn(resourcesTest);
		mockMvc.perform(get("/resourceApi"))
		.andExpect(status().is(200))
		.andDo(print())
		.andExpect(jsonPath("$[0].name", is("NIST Enterprise Data Inventory (EDI)")));
	}
}
