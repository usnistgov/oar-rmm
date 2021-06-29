package gov.nist.oar.rmm.unit.repositories.impl;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.github.fakemongo.junit.FongoRule;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.util.FongoJSON;

import gov.nist.oar.rmm.unit.repositories.MetricsRepositoryTest;
import gov.nist.oar.rmm.utilities.ProcessRequest;

//@RunWith(SpringRunner.class)
//@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class MetricsRepositoryImplTest implements MetricsRepositoryTest {
    private Logger logger = LoggerFactory.getLogger(MetricsRepositoryImplTest.class);
    @Rule
    public FongoRule fongoRule = new FongoRule();
    DBCollection fileMetrics, recordMetrics, repoMetrics, uniqueUsers;

    @Before
    public void initIt() throws Exception {

	recordMetrics = fongoRule.getDB("TestDBtemp").getCollection("recordMetrics");
	JSONParser parser = new JSONParser();
	JSONArray a;
	// RecordMetrics collection
	File file = new File(this.getClass().getClassLoader().getResource("recordMetrics.json").getFile());
	try {
	    a = (JSONArray) parser.parse(new FileReader(file));
	    for (Object o : a) {

		DBObject dbObject = (DBObject) com.mongodb.util.JSON.parse(o.toString());
		recordMetrics.save(dbObject);
	    }
	} catch (IOException | ParseException e) {
	    e.printStackTrace();
	}

	/// FileMetrics collection;
	fileMetrics = fongoRule.getDB("TestDBtemp").getCollection("fileMetrics");
	parser = new JSONParser();
	JSONArray b;
	file = new File(this.getClass().getClassLoader().getResource("fileMetrics.json").getFile());
	try {
	    b = (JSONArray) parser.parse(new FileReader(file));
	    for (Object o : b) {

		DBObject dbObject = (DBObject) com.mongodb.util.JSON.parse(o.toString());
		fileMetrics.save(dbObject);
	    }
	} catch (IOException | ParseException e) {

	    e.printStackTrace();
	}

	/// repoMetrics collection;
	repoMetrics = fongoRule.getDB("TestDBtemp").getCollection("repoMetrics");
	parser = new JSONParser();

	file = new File(this.getClass().getClassLoader().getResource("repoMetrics.json").getFile());
	try {
	    a = (JSONArray) parser.parse(new FileReader(file));
	    for (Object o : a) {

		DBObject dbObject = (DBObject) com.mongodb.util.JSON.parse(o.toString());
		repoMetrics.save(dbObject);
	    }
	} catch (IOException | ParseException e) {

	    e.printStackTrace();
	}

	/// uniqueUsers collection;
	uniqueUsers = fongoRule.getDB("TestDBtemp").getCollection("uniqueUsers");
	parser = new JSONParser();

	file = new File(this.getClass().getClassLoader().getResource("uniqueUsers.json").getFile());
	try {
	    a = (JSONArray) parser.parse(new FileReader(file));
	    for (Object o : a) {

		DBObject dbObject = (DBObject) com.mongodb.util.JSON.parse(o.toString());
		uniqueUsers.save(dbObject);
	    }
	} catch (IOException | ParseException e) {

	    e.printStackTrace();
	}
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

    @Override
    public Document findRecord(String id, MultiValueMap<String, String> params) {

	if (id != null)
	    params.add("ediid", id);
	return this.processInputAndData(recordMetrics, params, "DataSetMetrics");

    }

    @Test
    public void testFindRecords() {

	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	Document r = findRecord("", params);
	long resCnt = 20;
	assertEquals(r.get("DataSetMetricsCount"), resCnt);
    }

    @Override
    public Document findFile(String recordid, String filepath, MultiValueMap<String, String> params) {
	if (!recordid.isEmpty() && filepath.isEmpty()) {
	    params.add("ediid", recordid);
	    return this.processInputAndData(fileMetrics, params, "FilesMetrics");
	} else if (!filepath.isEmpty()) {
	    params.add("filepath", filepath);
	    return this.processInputAndData(fileMetrics, params, "FilesMetrics");
	}
	return this.processInputAndData(fileMetrics, params, "FilesMetrics");

    }

    @Test
    public void testFindFiles() {
	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	Document r = findFile("", "", params);
	long rcount = 10;
	assertEquals(r.get("FilesMetricsCount"), rcount);

	params = new LinkedMultiValueMap<String, String>();
	r = findFile("691DDF3315711C14E0532457068146BE1907", "", params);
	rcount = 3;
	assertEquals(r.get("FilesMetricsCount"), rcount);

	params = new LinkedMultiValueMap<String, String>();
	r = findFile("", "686BF41FF1D773D3E0532457068166DD1903/200m.Indy.v3.2.totals.local.csv", params);
	rcount = 1;
	assertEquals(r.get("FilesMetricsCount"), rcount);

//	 List<DBObject> rdata1 = (List<DBObject>)r.get("FilesMetrics");
//		int succes = 0;
//		
//		for (DBObject rd : rdata1) {
//			succes = Integer.parseInt(rd.get("sucess_get").toString());
//		}
//	 assertEquals(r.get("FilesMetrics"), 3);
//	 

    }

    @Override
    public Document findRepo(MultiValueMap<String, String> params) {

	return processInputAndData(repoMetrics, params, "RepoMetrics");
    }

    @Test
    public void TestFindRepo() {
	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	Document repo = findRepo(params);
	long count = 1;
	assertEquals(repo.get("RepoMetricsCount"), count);
    }

    @Override
    public Document totalUsers(MultiValueMap<String, String> params) {
	return processInputAndData(uniqueUsers, params, "TotalUsers");

    }

    @Test
    public void TestTotalUsers() {
	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	Document users = totalUsers(params);
	long count = 3;
	assertEquals(users.get("TotalUsersCount"), count);

    }

    /**
     * Get the input requested parameters and process to create Mongodb query,
     * process results and return in desired format
     */

    private Document processInputAndData(DBCollection mdCollection, MultiValueMap<String, String> params,
	    String collectionName) {

	ProcessRequest request = new ProcessRequest();
	request.parseSearch(params);

	long count = 0;
	if (request.getFilter() == null)
	    count = mdCollection.count();
	else {
	    Bson b = request.getFilter();

	    count = mdCollection.count(dbObject(b));
	}
	logger.info("Count :" + count);

	List<Bson> dList = request.getQueryList();
	List<BasicDBObject> dobList = new ArrayList<BasicDBObject>();
	int i = 0;
	while (dList.size() > i) {
	    dobList.add((BasicDBObject) dbObject(dList.get(i)));
	    i++;
	}
	AggregationOutput ag = mdCollection.aggregate(dobList);
	List<DBObject> dlist = new ArrayList<DBObject>();
	for (DBObject dbObject : ag.results()) {
	    dlist.add(dbObject);
	}

	Document resultDoc = new Document();
	resultDoc.put(collectionName + "Count", count);
	resultDoc.put("PageSize", request.getPageSize());
	resultDoc.put(collectionName, ag);
	return resultDoc;
    }

}
