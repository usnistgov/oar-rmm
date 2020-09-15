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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import gov.nist.oar.rmm.controllers.SearchController;
import gov.nist.oar.rmm.repositories.CustomRepository;


@RunWith(MockitoJUnitRunner.class)
public class TestSearchController {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
			MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	private  MockMvc mockMvc;
	
	@Mock
	@Autowired
	private CustomRepository customRepo;
	
    @InjectMocks
    @Autowired
	private SearchController searchController;
    
    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
    
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(searchController)
				 //.setControllerAdvice(new GlobalExceptionHandler())
				.setCustomArgumentResolvers(pageableArgumentResolver).build();
	}
	
	@Test
	public void shouldFindAll() throws Exception {
		Document resultDoc = new Document();
		resultDoc.put("ResultCount", 134);
		resultDoc.put("PageSize", 134);
		ArrayList<Document> aList = new ArrayList<Document>();
		JSONParser parser = new JSONParser();
    	JSONArray a;
    	File file = new File(this.getClass().getClassLoader().getResource("record.json").getFile());
		try {
			a = (JSONArray) parser.parse(new FileReader(file));
		  for (Object o : a)
    	  {
			 Document d = Document.parse(o.toString()); 
			 aList.add(d);
    	  }
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resultDoc.put("ResultData", aList);
		Map<String,String> params = new HashMap<String,String>();
//		when(customRepo.find(params)).thenReturn(resultDoc);
		
		//mockMvc.perform(get("/records"))
		//.andExpect(status().isOk());
		//.andExpect(jsonPath("$.ResultCount",is(134)));
		//assertEquals(134, Integer.parseInt(resultDoc.get("ResultCount").toString()));
		
		
	}
	
//	@Test
//	public void shouldFindByTitle() throws Exception {
//		mockMvc.perform(get("/taxonomy?level=1"))
//		.andExpect(status().isOk());
//	}

}
