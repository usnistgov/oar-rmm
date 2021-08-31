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
package gov.nist.oar.rmm.unit.repositories.impl;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.util.FongoJSON;

import gov.nist.oar.rmm.exceptions.ResourceNotFoundException;
import gov.nist.oar.rmm.repositories.VersionReleasesetsRepository;
import gov.nist.oar.rmm.utilities.ProcessRequest;

@RunWith(SpringJUnit4ClassRunner.class)
public class VersionsReleasesetsRepositoryImplTest implements VersionReleasesetsRepository {

    private Logger logger = LoggerFactory.getLogger(CustomRepositoryImplTest.class);
    @Rule
    public FongoRule fongoRule = new FongoRule();
    DBCollection versionsCollection, releasesetsCollection;

    @Before
    public void initIt() throws Exception {

	versionsCollection = fongoRule.getDB("TestDBtemp").getCollection("versionstest");
	JSONParser parser = new JSONParser();
	JSONArray a;
	File file = new File(this.getClass().getClassLoader().getResource("versions.json").getFile());
	try {
	    a = (JSONArray) parser.parse(new FileReader(file));
	    for (Object o : a) {
		DBObject dbObject = (DBObject) com.mongodb.util.JSON.parse(o.toString());
		versionsCollection.save(dbObject);
	    }
	} catch (IOException | ParseException e) {
	    e.printStackTrace();
	}

	/// Taxonomy collection;
	releasesetsCollection = fongoRule.getDB("TestDBtemp").getCollection("releasesets");
	parser = new JSONParser();

	file = new File(this.getClass().getClassLoader().getResource("releaseSets.json").getFile());
	try {
	    a = (JSONArray) parser.parse(new FileReader(file));
	    for (Object o : a) {
		DBObject dbObject = (DBObject) com.mongodb.util.JSON.parse(o.toString());
		releasesetsCollection.save(dbObject);
	    }
	} catch (IOException | ParseException e) {
	   e.printStackTrace();
	}
    }

    //// Functions to help test
    private DBObject dbObject(Bson bson) {
	if (bson == null) {
	    return null;
	}

	// TODO Performance killer
	return (DBObject) FongoJSON
		.parse(bson.toBsonDocument(Document.class, MongoClient.getDefaultCodecRegistry()).toString());
    }

    @Override
    public Document findVersion(MultiValueMap<String, String> params, Pageable p) {

	ProcessRequest request = new ProcessRequest();
	request.parseSearch(params);
	long count = 0;
	if (request.getFilter() == null)
	    count = versionsCollection.count();
	else {
	    Bson b = request.getFilter();
	    count = versionsCollection.count((BasicDBObject) dbObject(b));
	}

	logger.info("Count :" + count);
	Document resultDoc = new Document();
	resultDoc.put("Versions Count", count);
	resultDoc.put("PageSize", request.getPageSize());
	List<Bson> dList = request.getQueryList();
	List<BasicDBObject> dobList = new ArrayList<BasicDBObject>();
	int i = 0;
	while (dList.size() > i) {
	    dobList.add((BasicDBObject) dbObject(dList.get(i)));
	    i++;
	}
	AggregationOutput ag = versionsCollection.aggregate(dobList);
	List<DBObject> dlist = new ArrayList<DBObject>();
	for (DBObject dbObject : ag.results()) {
	    dlist.add(dbObject);
	}
	resultDoc.put("Versions data", dlist);
	return resultDoc;
    }

    @Test
    public void testFindVersions() {

	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

	Document r = findVersion(params, null);
	long resCnt = 15;
	List<DBObject> rdata = (List<DBObject>) r.get("Versions data");
	for (DBObject rd : rdata) {
	    System.out.println(rd.get("title"));
	}
	assertEquals(r.get("Versions Count"), resCnt);
    }

    @Test
    public void testFindVersionDocument() {
	//// Test with parameters
	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	params.add("@id", "ark:/88434/mds2-2106.rel");
	Document r1 = findVersion(params, null);
	List<DBObject> rdata1 = (List<DBObject>) r1.get("Versions data");
	String title = "";

	for (DBObject rd : rdata1) {
	    title = rd.get("title").toString();
	}
	assertEquals("Data from: Collaborative Guarded-Hot-Plate Tests between the National Institute of Standards and Technology and the National Physical Laboratory", title);

    }

    @Test
    public void testReleasesets() {
	MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

	Document r = findReleaseset(params, null);
	long resCnt = 4;
	long resCntdb =0;
	String titleReleaseSet = "";
	List<DBObject> rdata = (List<DBObject>) r.get("ReleaseSets data");
	for (DBObject rd : rdata) {
	    System.out.println(rd.get("title")+ " ::  "+r.get("ReleaseSets count"));
	    titleReleaseSet = rd.get("title").toString();
	    
	}
	assertEquals(r.get("ReleaseSets count"), resCnt);
	
    }

    @Override
    public Document findReleaseset(MultiValueMap<String, String> param, Pageable p) {
	ProcessRequest request = new ProcessRequest();
	request.parseSearch(param);

	long count = 0;
	if (request.getFilter() == null)
	    count = releasesetsCollection.count();
	else {
	    Bson b = request.getFilter();

	    count = releasesetsCollection.count((BasicDBObject) dbObject(b));
	}

	logger.info("ReleaseSets Count :" + count);
	Document resultDoc = new Document();
	resultDoc.put("ReleaseSets count", count);
	resultDoc.put("PageSize", request.getPageSize());
	List<Bson> dList = request.getQueryList();
	List<BasicDBObject> dobList = new ArrayList<BasicDBObject>();
	int i = 0;
	while (dList.size() > i) {
	    dobList.add((BasicDBObject) dbObject(dList.get(i)));
	    i++;
	}
	AggregationOutput ag = releasesetsCollection.aggregate(dobList);
	List<DBObject> dlist = new ArrayList<DBObject>();
	for (DBObject dbObject : ag.results()) {
	    dlist.add(dbObject);
	}
	resultDoc.put("ReleaseSets data", dlist);
	return resultDoc;
    }

    @Override
    public Document findVersion(String id) {
	return queryResult("ark:/88434/mds2-2107.rel", "versions");
    }
    
    @Test
    public void testfindReleaseset() {
	Document r = findReleaseset("ark:/88434/mds00sxbvh.rel");
	List<DBObject> rdata = (List<DBObject>) r.get("ReleaseSets data");
	for (DBObject rd : rdata) {
	    System.out.println(rd.get("title"));
	}
	
    }

    @Override
    public Document findReleaseset(String id) {
	return queryResult("ark:/88434/mds00sxbvh.rel", "releaseSets");
	
    }

    
    private Document queryResult(String id, String collectionName) {
		
//	
//	Pattern legal = Pattern.compile("[^a-z0-9:/-]", Pattern.CASE_INSENSITIVE);
//	Matcher m = legal.matcher(id);
//	if (m.find())
//	    throw new IllegalArgumentException("Illegal identifier");

	DBCollection mcollection = null;
	if("versions".equalsIgnoreCase(collectionName))
	    mcollection = versionsCollection;
	else if("releaseSets".equalsIgnoreCase(collectionName)) 
	    mcollection = releasesetsCollection;
	String useid = id;
	Document resultDoc = new Document();
	resultDoc.put("ReleaseSets count", 1);
	resultDoc.put("PageSize", 1);
	mcollection.find().limit(1);
	DBCursor dm = mcollection.find(new BasicDBObject("@id","ark:/88434/mds00sxbvh.rel"));
	DBObject resultObject = dm.next();
	
	List<DBObject> dlist = new ArrayList<DBObject>();
	dlist.add(resultObject);
	resultDoc.put("ReleaseSets data",dlist );
	return resultDoc;
    }
}
