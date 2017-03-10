///**
// * This software was developed at the National Institute of Standards and Technology by employees of
// * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
// * of the United States Code this software is not subject to copyright protection and is in the
// * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
// * use by other parties, and makes no guarantees, expressed or implied, about its quality,
// * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
// * used. This software can be redistributed and/or modified freely provided that any derivative
// * works bear some notice that they are derived from it, and any modified versions bear some notice
// * that they have been modified.
// * @author: Deoyani Nandrekar-Heinis
// */
//package gov.nist.mml.controller;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import gov.nist.mml.configuration.TestWebappConfiguration;
//
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//
///**
// * @author dsn1
// * @DeoyaniNandrekar-Heinis
// */
//@WebAppConfiguration
//@ContextConfiguration(classes=TestWebappConfiguration.class)
////@ComponentScan(basePackages = "gov.nist.mml")
//@RunWith(SpringJUnit4ClassRunner.class)
//
//public class TestSearchController {
//
//	@Autowired
//    private WebApplicationContext wac;
//
//	@Autowired MockHttpServletRequest request;
//	
//    private MockMvc mockMvc;
//
//    @Before
//    public void setup() {
//       // this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();
//    }
//
//    @Test
//    public void getTitle() throws Exception {
//        this.mockMvc.perform(
//        		get("/records/searchByTitle?title='Enterprise'")
//        		.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
//          .andExpect(status().isOk())
//          .andExpect(content().contentType("application/json"))
//          .andExpect(jsonPath("$.title").value("Enterprise Data Inventory"));
//    }
//}
