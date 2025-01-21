// /**
//  * This software was developed at the National Institute of Standards and Technology by employees of
//  * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
//  * of the United States Code this software is not subject to copyright protection and is in the
//  * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
//  * use by other parties, and makes no guarantees, expressed or implied, about its quality,
//  * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
//  * used. This software can be redistributed and/or modified freely provided that any derivative
//  * works bear some notice that they are derived from it, and any modified versions bear some notice
//  * that they have been modified.
//  * @author: Deoyani Nandrekar-Heinis
//  */
// package gov.nist.oar.rmm.repositories.impl;

// import static org.junit.Assert.assertEquals;

// import java.io.File;
// import java.io.FileReader;
// import java.io.IOException;
// import java.util.Iterator;
// import java.util.List;

// import org.bson.Document;
// import org.bson.conversions.Bson;
// import org.json.simple.JSONArray;
// import org.json.simple.parser.JSONParser;
// import org.json.simple.parser.ParseException;
// import org.junit.jupiter.api.AfterAll;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;
// import org.junit.jupiter.api.TestInstance.Lifecycle;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.mongodb.core.MongoTemplate;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.util.LinkedMultiValueMap;
// import org.springframework.util.MultiValueMap;

// import com.mongodb.BasicDBObject;
// import com.mongodb.client.AggregateIterable;
// import com.mongodb.client.FindIterable;
// import com.mongodb.client.MongoClients;
// import com.mongodb.client.MongoCollection;

// import de.flapdoodle.embed.mongo.MongodExecutable;
// import de.flapdoodle.embed.mongo.MongodStarter;
// import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
// import de.flapdoodle.embed.mongo.config.MongodConfig;
// import de.flapdoodle.embed.mongo.config.Net;
// import de.flapdoodle.embed.mongo.distribution.Version;
// import de.flapdoodle.embed.process.runtime.Network;
// import gov.nist.oar.rmm.repositories.VersionReleasesetsRepository;
// import gov.nist.oar.rmm.utilities.ProcessRequest;
// @ActiveProfiles("test")
// @TestInstance(Lifecycle.PER_CLASS)
// @TestPropertySource("classpath:bootstrap-test.yml")
// public class VersionsReleasesetsRepositoryImplTest implements VersionReleasesetsRepository {

	
// 	private static final String CONNECTION_STRING = "mongodb://%s:%d";

// 	private Logger logger = LoggerFactory
// 			.getLogger(VersionsReleasesetsRepositoryImplTest.class);
// 	private MongodExecutable mongodExecutable;
// 	private MongoTemplate mongoTemplate;

// 	@AfterAll
// 	void clean() {
// 		mongodExecutable.stop();
// 	}

// 	@BeforeAll
// 	void setup() throws Exception {
// 		String ip = "localhost";
// 		int port = 21432;

// 		ImmutableMongodConfig mongodConfig = MongodConfig.builder()
// 				.version(Version.Main.PRODUCTION)
// 				.net(new Net(ip, port, Network.localhostIsIPv6())).build();

// 		MongodStarter starter = MongodStarter.getDefaultInstance();
// 		mongodExecutable = starter.prepare(mongodConfig);
// 		mongodExecutable.start();
// 		mongoTemplate = new MongoTemplate(
// 				MongoClients.create(String.format(CONNECTION_STRING, ip, port)),
// 				"test");

// 		JSONParser parser = new JSONParser();
// 		JSONArray a;
// 		File file = new File(this.getClass().getClassLoader()
// 				.getResource("versions.json").getFile());

// 		try {
// 			a = (JSONArray) parser.parse(new FileReader(file));
// 			for (Object o : a) {
// 				Document doc = Document.parse(o.toString());
// 				mongoTemplate.save(doc, "versions");
// 			}
// 		} catch (IOException | ParseException e) {


// 			e.printStackTrace();
// 		}

// 		file = new File(this.getClass().getClassLoader()
// 				.getResource("releaseSets.json").getFile());
// 		try {
// 			a = (JSONArray) parser.parse(new FileReader(file));
// 			for (Object o : a) {
// 				Document doc = Document.parse(o.toString());
// 				mongoTemplate.save(doc, "releaseSet");

// 			}
// 		} catch (IOException | ParseException e) {

// 			e.printStackTrace();
// 		}
// 	}


    
//     public Document findVersion(MultiValueMap<String, String> params, Pageable p) {

//         ProcessRequest request = new ProcessRequest();
//         request.parseSearch(params);
//         long count = 0;
//         if (request.getFilter() == null)
//             count = mongoTemplate.getCollection("versions").countDocuments();
//         else {
//             Bson b = request.getFilter();
//             count =  mongoTemplate.getCollection("versions").countDocuments(b);
//         }

//         logger.info("Count :" + count);
    
//         List<Bson> dList = request.getQueryList();

//         AggregateIterable<Document> ag =  mongoTemplate.getCollection("versions").aggregate(dList);        
//         Document resultDoc = new Document();
//         resultDoc.put("Version" + "Count", count);
//         resultDoc.put("PageSize", request.getPageSize());
//         resultDoc.put("Version" + "Data", ag);
//         return resultDoc;
//     }

//     @Test
//     public void testFindVersions() {

//         MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//         Document r = findVersion(params, null);
//         long resCnt = 15;
//         assertEquals(r.get("VersionCount"), resCnt);
//     }

//     @Test
//     public void testFindVersionDocument() {
//         //// Test with parameters
//         MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//         params.add("@id", "ark:/88434/mds2-2106.rel");
//         Document r1 = findVersion(params, null);
//         AggregateIterable<Document> aggregateIterable =  (AggregateIterable<Document>) r1.get("VersionData");
//         Iterator iterator = aggregateIterable.iterator();
//         String title = "";
//         while (iterator.hasNext()) {
//             Document d =(Document) iterator.next();
//             title = d.getString("title");
//             break;
//         }

//         assertEquals(
//                 "Data from: Collaborative Guarded-Hot-Plate Tests between the National Institute of Standards and Technology and the National Physical Laboratory",
//                 title);

//     }

//     @Test
//     public void testReleasesets() {
//         MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//         Document r = findReleaseset(params, null);
//         long resCnt = 4;   
//         assertEquals(r.get("ReleaseSetsCount"), resCnt);
//     }

//     @Override
//     public Document findReleaseset(MultiValueMap<String, String> param, Pageable p) {
//         ProcessRequest request = new ProcessRequest();
//         request.parseSearch(param);

//         long count = 0;
//         if (request.getFilter() == null)
//             count = mongoTemplate.getCollection("releaseSet").countDocuments();
//         else {
//             Bson b = request.getFilter();

//             count = mongoTemplate.getCollection("releaseSet").countDocuments(b);
//         }

//         logger.info("ReleaseSetsCount :" + count);
//         Document resultDoc = new Document();
//         resultDoc.put("ReleaseSetsCount", count);
//         resultDoc.put("PageSize", request.getPageSize());
//         List<Bson> dList = request.getQueryList();
//         AggregateIterable<Document> ag = mongoTemplate.getCollection("releaseSet").aggregate(dList);
//         resultDoc.put("ReleaseSetsData", ag);
//         return resultDoc;
//     }

//     @Override
//     public Document findVersion(String id) {
//         return queryResult("ark:/88434/mds2-2107.rel", "versions");
//     }

//     @Test
//     public void testfindReleaseset() {
//     	 MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//          params.add("@id", "ark:/88434/mds00sxbvh.rel");
//          Document r = findReleaseset(params, null);
        
//         AggregateIterable<Document> aggregateIterable =  (AggregateIterable<Document>) r.get("ReleaseSetsData");
//         Iterator iterator = aggregateIterable.iterator();
//         String title = "";
//         while (iterator.hasNext()) {
//             Document d =(Document) iterator.next();
//             title = d.getString("title");
//             break;
//         }
//         assertEquals("NIST Mugshot Identification Database (MID) - NIST Special Database 18", title);
//     }

//     @Override
//     public Document findReleaseset(String id) {
//         return queryResult("ark:/88434/mds00sxbvh.rel", "releaseSet");

//     }

//     private Document queryResult(String id, String collectionName) {

//         MongoCollection<Document> mcollection = null;
//         if ("version".equalsIgnoreCase(collectionName))
//             mcollection = mongoTemplate.getCollection("version");

//         else if ("releaseSet".equalsIgnoreCase(collectionName))
//             mcollection = mongoTemplate.getCollection("releaseSet");

//         Document resultDoc = new Document();
//         resultDoc.put("ReleaseSets count", 1);
//         resultDoc.put("PageSize", 1);
//         mcollection.find().limit(1);
//         FindIterable<Document> dm = mcollection.find(new BasicDBObject("@id", "ark:/88434/mds00sxbvh.rel"));
    
//         resultDoc.put("ReleaseSetData", dm);
//         return resultDoc;
//     }


// }