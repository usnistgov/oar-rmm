//package gov.nist.oar.rmm.unit.controller;
//
//import static org.hamcrest.core.Is.is;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.nio.charset.Charset;
//
//import javax.inject.Inject;
//
//import org.bson.Document;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//
//import gov.nist.oar.rmm.config.AppConfig;
//import gov.nist.oar.rmm.controllers.MetricsController;
////import gov.nist.oar.rmm.controllers.SearchController;
////import gov.nist.oar.rmm.repositories.CustomRepository;
//import gov.nist.oar.rmm.repositories.MetricsRepository;
//import org.springframework.test.context.junit4.SpringRunner;
//
////@RunWith(SpringRunner.class)
////@WebMvcTest(MetricsController.class)
////@AutoConfigureMockMvc
////@ContextConfiguration
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.platform.runner.JUnitPlatform;
//
////
////@ExtendWith(MockitoExtension.class)
////@RunWith(JUnitPlatform.class)
//public class TestMetricsCollector {
//    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
//		MediaType.APPLICATION_JSON.getType(),
//		MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
//
//private  MockMvc mockMvc;
//
////@TestConfiguration
////static class MongConfigTest {
////    @Bean
////}
//
//@Mock
//private MetricsRepository logRepo;
//
//@InjectMocks
////@Autowired
//private MetricsController metricsController;
//
////@Inject
////private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
////
////@Before
////public void setup() {
////	this.mockMvc = MockMvcBuilders.standaloneSetup(metricsController)
////			.setCustomArgumentResolvers(pageableArgumentResolver).build();
////}
//
//@Before
//public void initIt() throws Exception {
//	try {
//
//	    String recordTest = "{\"DataSetMetricsCount\":1,\"PageSize\":0,"
//		    + "\"DataSetMetrics\":[{\"ediid\":\"691DDF3315711C14E0532457068146BE1907\",\"first_time_logged\":\"2021-02-01T18:13:17.000+0000\",\"last_time_logged\":\"2021-02-04T14:56:21.000+0000\",\"total_size\":428555,\"success_get\":20,\"number_users\":5}]}";
//	    Document record = Document.parse(recordTest);
//	    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//	    when(logRepo.findRecord("691DDF3315711C14E0532457068146BE1907", params)).thenReturn(record);
//	    
//	    String fileTest = "{\"FilesMetricsCount\":1,\"PageSize\":0,\"FilesMetrics\":{\"ediid\":\"686BF41FF1D773D3E0532457068166DD1903\",\"filepath\":\"686BF41FF1D773D3E0532457068166DD1903/200m.Indy.v3.2.totals.local.csv\",\"success_head\":0,\"failure_head\":0,\"success_get\":3,\"failure_get\":5,\"request_id\":0}}"; 
//	    Document file = Document.parse(fileTest);
//	    when(logRepo.findFile("686BF41FF1D773D3E0532457068166DD1903", "686BF41FF1D773D3E0532457068166DD1903/200m.Indy.v3.2.totals.local.csv", params)).thenReturn(file);
//	    
//	    String repoTest =  "{\"RepoMetricsCount\":1,\"PageSize\":0,\"RepoMetrics\":[{\"timestamp\":\"Feb 2021\",\"total_size\":49378173294,\"number_users\":116}]}";
//	    Document repo = Document.parse(repoTest);
//	    when(logRepo.findRepo(params)).thenReturn(repo);
//	    
//	    String usersMetrics = "{\"TotalUsresCount\":116,\"PageSize\":0,\"TotalUsres\":[{},{}]}";
//	    Document users = Document.parse(usersMetrics);
//	    when(logRepo.totalUsers(params)).thenReturn(users);
//	    
//	} catch (Exception e) {
//	    System.out.print("Exception " + e.getMessage());
//	}
//}
//
//@Test
//public void shouldFindRecords() throws Exception {
//	mockMvc.perform(get("/usagemetrics/records/691DDF3315711C14E0532457068146BE1907"))
//	.andExpect(status().is(200))
//	.andExpect(jsonPath("$.DataSetMetrics.[0].total_size", is(428555)))
//	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//	.andExpect(jsonPath("$.DataSetMetricsCount",is(1)));
//}
//
////@Test
////public void shouldFindTaxonomyBylevel() throws Exception {
////	mockMvc.perform(get("/taxonomy?level=1"))
////	.andExpect(status().isOk());
////}
////
////@Test
////public void findRecord() throws Exception {
////	mockMvc.perform(get("/records?title=Enterprise"))
////	.andExpect(jsonPath("$.ResultData.[0].title", is("Enterprise Data Inventory")))
////	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
////	.andExpect(status().isOk());
////}
//
////@Test
////public void shouldFindRecordById() throws Exception {
////	mockMvc.perform(get("/records?@id=ark:/88434/pdr02d58"))
////	.andExpect(status().is(200))
////	.andDo(print())
////	.andExpect(jsonPath("$.ResultData.[0].title", is("NIST-JANAF Thermochemical Tables - SRD 13")));
////}
////@Test
////public void shouldFindTaxonomy() throws Exception {
////	mockMvc.perform(get("/taxonomy"))
////	.andExpect(status().is(200))
////	.andDo(print())
////	.andExpect(jsonPath("$[0].term", is("Advanced Communications")));
////}
////
////@Test
////public void shouldFindRecordFields() throws Exception {
////	mockMvc.perform(get("/records/fields"))
////	.andExpect(status().is(200))
////	.andDo(print())
////	.andExpect(jsonPath("$[0].name", is("@type")));
////}
////
////@Test
////public void shouldFindResourceApis() throws Exception {
////	mockMvc.perform(get("/resourceApi"))
////	.andExpect(status().is(200))
////	.andDo(print())
////	.andExpect(jsonPath("$[0].name", is("NIST Enterprise Data Inventory (EDI)")));
////}
//}
