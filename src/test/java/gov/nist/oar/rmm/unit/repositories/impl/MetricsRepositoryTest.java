package gov.nist.oar.rmm.unit.repositories.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.github.fakemongo.junit.FongoRule;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.util.FongoJSON;
import com.mongodb.util.JSON;

import gov.nist.oar.rmm.config.MongoConfig;
import gov.nist.oar.rmm.repositories.MetricsRepository;
//import gov.nist.oar.rmm.repositories.MetricsRepository;
import gov.nist.oar.rmm.repositories.impl.MetricsRepositoryImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@RunWith(SpringJUnit4ClassRunner.class)
public class MetricsRepositoryTest {
    private Logger logger = LoggerFactory.getLogger(MetricsRepositoryTest.class);
    @Rule
    public FongoRule fongoRule = new FongoRule();

//    @Mock
//    MongoConfig mconfig;

    DBCollection fileMetrics, recordMetrics, repoMetrics, uniqueUsers;
//    MongoCollection<Document> recordMetrics;
//    @Autowired
//    MetricsRepository mockRepo;
    @MockBean
    private MetricsRepository mockRepository;

    @Before
    public void initIt() throws Exception {
	try {

	    String recordTest = "{\"DataSetMetricsCount\":1,\"PageSize\":0,"
		    + "\"DataSetMetrics\":[{\"ediid\":\"691DDF3315711C14E0532457068146BE1907\",\"first_time_logged\":\"2021-02-01T18:13:17.000+0000\",\"last_time_logged\":\"2021-02-04T14:56:21.000+0000\",\"total_size\":428555,\"success_get\":20,\"number_users\":5}]}";
	    Document record = Document.parse(recordTest);
	    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	    when(mockRepository.findRecord("691DDF3315711C14E0532457068146BE1907", params)).thenReturn(record);
	    
	    String fileTest = "{\"FilesMetricsCount\":1,\"PageSize\":0,\"FilesMetrics\":{\"ediid\":\"686BF41FF1D773D3E0532457068166DD1903\",\"filepath\":\"686BF41FF1D773D3E0532457068166DD1903/200m.Indy.v3.2.totals.local.csv\",\"success_head\":0,\"failure_head\":0,\"success_get\":3,\"failure_get\":5,\"request_id\":0}}"; 
	    Document file = Document.parse(fileTest);
	    when(mockRepository.findFile("686BF41FF1D773D3E0532457068166DD1903", "686BF41FF1D773D3E0532457068166DD1903/200m.Indy.v3.2.totals.local.csv", params)).thenReturn(file);
	    
	    String repoTest =  "{\"RepoMetricsCount\":1,\"PageSize\":0,\"RepoMetrics\":[{\"timestamp\":\"Feb 2021\",\"total_size\":49378173294,\"number_users\":116}]}";
	    Document repo = Document.parse(repoTest);
	    when(mockRepository.findRepo(params)).thenReturn(repo);
	    
	    String usersMetrics = "{\"TotalUsresCount\":116,\"PageSize\":0,\"TotalUsres\":[{},{}]}";
	    Document users = Document.parse(usersMetrics);
	    when(mockRepository.totalUsers(params)).thenReturn(users);
	    
	} catch (Exception e) {
	    System.out.print("Exception " + e.getMessage());
	}
//	recordMetrics = fongoRule.getDB("TestDBtemp").getCollection("recordMetrics");
//	JSONParser parser = new JSONParser();
//	JSONArray a;
//	// RecordMetrics collection
//	File file = new File(this.getClass().getClassLoader().getResource("recordMetrics.json").getFile());
//	try {
//	    a = (JSONArray) parser.parse(new FileReader(file));
//	    for (Object o : a) {
//
//		DBObject dbObject = (DBObject) com.mongodb.util.JSON.parse(o.toString());
//		recordMetrics.save(dbObject);
//	    }
//	} catch (IOException | ParseException e) {
//	    e.printStackTrace();
//	}

//	when(mconfig.getRecordMetricsCollection()).thenReturn(recordMetrics);
//	
//	/// FileMetrics collection;
//	fileMetrics = fongoRule.getDB("TestDBtemp").getCollection("fileMetrics");
//	parser = new JSONParser();
//
//	file = new File(this.getClass().getClassLoader().getResource("fileMetrics.json").getFile());
//	try {
//	    a = (JSONArray) parser.parse(new FileReader(file));
//	    for (Object o : a) {
//
//		DBObject dbObject = (DBObject) com.mongodb.util.JSON.parse(o.toString());
//		fileMetrics.save(dbObject);
//	    }
//	} catch (IOException | ParseException e) {
//
//	    e.printStackTrace();
//	}
////	when(mconfig.getfileMetricsCollection()).thenReturn((MongoCollection<Document>) fileMetrics);
//	
//	/// repoMetrics collection;
//	repoMetrics = fongoRule.getDB("TestDBtemp").getCollection("repoMetrics");
//	parser = new JSONParser();
//
//	file = new File(this.getClass().getClassLoader().getResource("repoMetrics.json").getFile());
//	try {
//	    a = (JSONArray) parser.parse(new FileReader(file));
//	    for (Object o : a) {
//
//		DBObject dbObject = (DBObject) com.mongodb.util.JSON.parse(o.toString());
//		repoMetrics.save(dbObject);
//	    }
//	} catch (IOException | ParseException e) {
//
//	    e.printStackTrace();
//	}
//	
////	when(mconfig.getRepoMetricsCollection()).thenReturn((MongoCollection<Document>) repoMetrics);
//	
//	/// uniqueUsers collection;
//	uniqueUsers = fongoRule.getDB("TestDBtemp").getCollection("uniqueUsers");
//	parser = new JSONParser();
//
//	file = new File(this.getClass().getClassLoader().getResource("uniqueUsers.json").getFile());
//	try {
//	    a = (JSONArray) parser.parse(new FileReader(file));
//	    for (Object o : a) {
//
//		DBObject dbObject = (DBObject) com.mongodb.util.JSON.parse(o.toString());
//		uniqueUsers.save(dbObject);
//	    }
//	} catch (IOException | ParseException e) {
//
//	    e.printStackTrace();
//	}
//	

//	mockRepository.init();
//	MockitoAnnotations.initMocks(this);
    }

////Functions to help test
    private DBObject dbObject(Bson bson) {
	if (bson == null) {
	    return null;
	}

	// TODO Performance killer
	return (DBObject) FongoJSON
		.parse(bson.toBsonDocument(Document.class, MongoClient.getDefaultCodecRegistry()).toString());
    }
//    @Test
//    public void findRecord() {
//
//	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//	Document results = mockRepository.findRecord("691DDF3315711C14E0532457068146BE1907", params);
//	System.out.println("Results:"+results);
//
//    }

    @Test
    public void findRecord() {

	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	Document results = mockRepository.findRecord("691DDF3315711C14E0532457068146BE1907", params);
	System.out.println("Results:" + results.toJson());
	assertEquals(results.getInteger("DataSetMetricsCount"), Integer.valueOf(1));

    }

    @Test
    public void findFile() {
	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	String filepath = "686BF41FF1D773D3E0532457068166DD1903/200m.Indy.v3.2.totals.local.csv";
	Document results = mockRepository.findFile("686BF41FF1D773D3E0532457068166DD1903",filepath, params);
	assertEquals(results.getInteger("FilesMetricsCount"), Integer.valueOf(1));
    }

    @Test
    public void repoMetrics() {
	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	Document results = mockRepository.findRepo(params);
	Object attributeValue = results.get("RepoMetrics");
	    if (attributeValue instanceof Document) {
	      HashMap<Object, Object> attributAsAMap = new HashMap<>();
	      ((Document) attributeValue).forEach(attributAsAMap::put);
	      System.out.println("Results1:" + attributAsAMap);
	    } else {
		
		System.out.println("Results2:" +attributeValue);
	    }
	
	assertEquals(results.getInteger("RepoMetricsCount"), Integer.valueOf(1));
	
    }

    @Test
    public void totalUsers() {
	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	Document results = mockRepository.totalUsers(params);
	assertEquals(results.getInteger("TotalUsresCount"), Integer.valueOf(116));

    }
}
