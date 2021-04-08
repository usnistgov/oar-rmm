package gov.nist.oar.rmm.unit.repositories.impl;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.MultiValueMap;

import com.github.fakemongo.junit.FongoRule;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.FongoJSON;

import gov.nist.oar.rmm.unit.repositories.MetricsRepositoryTest;

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

	file = new File(this.getClass().getClassLoader().getResource("fileMetrics.json").getFile());
	try {
	    a = (JSONArray) parser.parse(new FileReader(file));
	    for (Object o : a) {

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
	    return (DBObject) FongoJSON.parse(bson.toBsonDocument(Document.class, MongoClient.getDefaultCodecRegistry()).toString());
	  }
    @Override
    public Document findRecord(String id, MultiValueMap<String, String> params) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Document findFile(String id, String fileid, MultiValueMap<String, String> params) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Document totalSize(MultiValueMap<String, String> params) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Document totalUsers(MultiValueMap<String, String> params) {
	// TODO Auto-generated method stub
	return null;
    }
}
