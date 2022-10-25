package gov.nist.oar.rmm.unit.repositories.impl;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import gov.nist.oar.rmm.utilities.ProcessRequest;
@TestPropertySource("classpath:bootstrap-test.yml")
@TestInstance(Lifecycle.PER_CLASS)
public class CustomRepositoryImplTest {
	private static final String CONNECTION_STRING = "mongodb://%s:%d";

	private Logger logger = LoggerFactory
			.getLogger(CustomRepositoryImplTest.class);
	private MongodExecutable mongodExecutable;
	private MongoTemplate mongoTemplate;

	@AfterAll
	void clean() {
		mongodExecutable.stop();
	}

	@BeforeAll
	void setup() throws Exception {
		String ip = "localhost";
		int port = 21432;

		ImmutableMongodConfig mongodConfig = MongodConfig.builder()
				.version(Version.Main.PRODUCTION)
				.net(new Net(ip, port, Network.localhostIsIPv6())).build();

		MongodStarter starter = MongodStarter.getDefaultInstance();
		mongodExecutable = starter.prepare(mongodConfig);
		mongodExecutable.start();
		mongoTemplate = new MongoTemplate(
				MongoClients.create(String.format(CONNECTION_STRING, ip, port)),
				"test");

		JSONParser parser = new JSONParser();
		JSONArray a;
		File file = new File(this.getClass().getClassLoader()
				.getResource("record.json").getFile());

		try {
			a = (JSONArray) parser.parse(new FileReader(file));
			for (Object o : a) {
				Document doc = Document.parse(o.toString());
				mongoTemplate.save(doc, "record");
			}
		} catch (IOException | ParseException e) {


			e.printStackTrace();
		}

		file = new File(this.getClass().getClassLoader()
				.getResource("taxonomy.json").getFile());
		try {
			a = (JSONArray) parser.parse(new FileReader(file));
			for (Object o : a) {
				Document doc = Document.parse(o.toString());
				mongoTemplate.save(doc, "taxonomy");

			}
		} catch (IOException | ParseException e) {

			e.printStackTrace();
		}
	}

	@DisplayName("Test a Test " + " when save object using MongoDB template"
			+ " then object is saved")
	@Test
	public void test() throws Exception {
		// given
		DBObject objectToSave = BasicDBObjectBuilder.start().add("key", "value")
				.get();

		// when
		mongoTemplate.save(objectToSave, "collection");

		// then
		assertThat(mongoTemplate.findAll(DBObject.class, "collection"))
				.extracting("key").containsOnly("value");

		logger.info("count ::"
				+ mongoTemplate.getCollection("collection").countDocuments());

	}

	public Document find(MultiValueMap<String, String> params) {

		ProcessRequest request = new ProcessRequest();
		request.parseSearch(params);
		long count = 0;
		if (request.getFilter() == null)
			count = mongoTemplate.getCollection("record").countDocuments();
		else {
			Bson b = request.getFilter();
			count = mongoTemplate.getCollection("record").countDocuments(b);
		}

		List<Bson> dList = request.getQueryList();

		AggregateIterable<Document> ag = mongoTemplate.getCollection("record")
				.aggregate(dList);
		logger.info("Count :" + count);
		Document resultDoc = new Document();
		resultDoc.put("ResultCount", count);
		resultDoc.put("PageSize", request.getPageSize());
		resultDoc.put("ResultData", ag);
		return resultDoc;
	}

	@Test
	public void testFindRecords() {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

		Document r = find(params);
		long resCnt = 134;
		assertEquals(r.get("ResultCount"), resCnt);
	}

	@Test
	public void testFindRecordKeyValue() {
		//// Test with parameters
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("title", "Enterprise Data Inventory");
		Document r1 = find(params);
		Document rData = (Document) r1.get("ResultData");
		String title = rData.getString("title");

		assertEquals("Enterprise Data Inventory", title);

	}

	public FindIterable<Document> testfindtaxonomy(Map<String, String> param) {
		ProcessRequest request = new ProcessRequest();

		Bson b = request.parseTaxonomy(param);
		FindIterable<Document> results = mongoTemplate.getCollection("taxonomy")
				.find();

		return results;

	}

	@Test
	public void testTaxonomy() {
		Map<String, String> params = new HashMap<String, String>();
		FindIterable<Document> docs = testfindtaxonomy(params);
		int count = 0;
		for (Document doc : docs) {
			count++;
		}
		assertEquals(249, count);

	}

}
