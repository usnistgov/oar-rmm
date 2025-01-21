package gov.nist.oar.rmm.controllers;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import gov.nist.oar.rmm.config.AppConfig;
import gov.nist.oar.rmm.config.MongoConfig;
import gov.nist.oar.rmm.controllers.MetricsController;
import gov.nist.oar.rmm.repositories.MetricsRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.test.annotation.DirtiesContext;

@WebMvcTest(controllers = MetricsController.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@TestPropertySource("/bootstrap-test.yml")
@ContextConfiguration(classes = AppConfig.class)
@ExtendWith(SpringExtension.class)
@DirtiesContext
public class MetricsControllerTest {

        public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        MongoConfig mongoConfig;

        @MockBean
        private MetricsRepository logRepo;

        @Test
        public void findRecordMetrics() throws Exception {

                String recordTest = "{\"DataSetMetricsCount\":1,\"PageSize\":0,"
                                + "\"DataSetMetrics\":[{\"ediid\":\"691DDF3315711C14E0532457068146BE1907\",\"first_time_logged\":\"2021-02-01T18:13:17.000+0000\",\"last_time_logged\":\"2021-02-04T14:56:21.000+0000\",\"total_size\":428555,\"success_get\":20,\"number_users\":5}]}";
                Document record = Document.parse(recordTest);
                MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
                when(logRepo.findRecord("691DDF3315711C14E0532457068146BE1907", params)).thenReturn(record);

                mockMvc.perform(get("/usagemetrics/records/691DDF3315711C14E0532457068146BE1907"))
                                .andExpect(status().is(200))
                                .andExpect(jsonPath("$.DataSetMetrics.[0].total_size", is(428555)))
                                .andExpect(content().contentType("application/json"))
                                .andExpect(jsonPath("$.DataSetMetricsCount", is(1)));
        }

        @Test
        public void findFileMetrics() throws Exception {
                MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
                String fileTest = "{\"FilesMetricsCount\":1,\"PageSize\":0,\"FilesMetrics\":{\"ediid\":\"686BF41FF1D773D3E0532457068166DD1903\",\"filepath\":\"686BF41FF1D773D3E0532457068166DD1903/200m.Indy.v3.2.totals.local.csv\",\"success_head\":0,\"failure_head\":0,\"success_get\":3,\"failure_get\":5,\"request_id\":0}}";
                Document file = Document.parse(fileTest);
                when(logRepo.findFile("686BF41FF1D773D3E0532457068166DD1903",
                                "686BF41FF1D773D3E0532457068166DD1903/200m.Indy.v3.2.totals.local.csv", params))
                                .thenReturn(file);

                mockMvc.perform(get(
                                "/usagemetrics/files/686BF41FF1D773D3E0532457068166DD1903/200m.Indy.v3.2.totals.local.csv"))
                                .andExpect(status().is(200))
                                .andExpect(jsonPath("$.FilesMetrics.success_get", is(3)))
                                .andExpect(content().contentType("application/json"))
                                .andExpect(jsonPath("$.FilesMetricsCount", is(1)));

        }

        @Test
        public void findRepoMetrics() throws Exception {
                MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
                String repoTest = "{\"RepoMetricsCount\":1,\"PageSize\":0,\"RepoMetrics\":[{\"timestamp\":\"Feb 2021\",\"total_size\":49378173294,\"number_users\":116}]}";
                Document repo = Document.parse(repoTest);
                when(logRepo.findRepo(params)).thenReturn(repo);

                mockMvc.perform(get("/usagemetrics/repo")).andExpect(status().is(200))
                                .andExpect(jsonPath("$.RepoMetrics.[0].number_users", is(116)))
                                .andExpect(content().contentType("application/json"))
                                .andExpect(jsonPath("$.RepoMetrics.[0].timestamp", is("Feb 2021")));
        }

        @Test
        public void usersMetrics() throws Exception {
                MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
                String usersMetrics = "{\"TotalUsresCount\":116,\"PageSize\":0,\"TotalUsres\":[{},{}]}";
                Document users = Document.parse(usersMetrics);
                when(logRepo.totalUsers(params)).thenReturn(users);

                mockMvc.perform(get("/usagemetrics/totalusers")).andExpect(status().is(200))
                                .andExpect(content().contentType("application/json"))
                                .andExpect(jsonPath("$.TotalUsresCount", is(116)));
        }

}
