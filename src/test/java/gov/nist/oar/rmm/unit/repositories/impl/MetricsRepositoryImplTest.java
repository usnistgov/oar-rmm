package gov.nist.oar.rmm.unit.repositories.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import gov.nist.oar.rmm.repositories.MetricsRepository;
import gov.nist.oar.rmm.utilities.ProcessRequest;
import org.junit.jupiter.api.TestInstance.Lifecycle;
@TestInstance(Lifecycle.PER_CLASS)
public class MetricsRepositoryImplTest implements MetricsRepository {
	private static final String CONNECTION_STRING = "mongodb://%s:%d";
	private Logger logger = LoggerFactory.getLogger(MetricsRepositoryImplTest.class);
	private MongodExecutable mongodExecutable;
	private MongoTemplate mongoTemplate;

	@AfterAll
	void clean() {
		mongodExecutable.stop();
	}

	@BeforeAll
	void setup() throws Exception {
		String ip = "localhost";
		int port = 23032;

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
		// RecordMetrics collection
		File file = new File(this.getClass().getClassLoader()
				.getResource("recordMetrics.json").getFile());
		try {
			a = (JSONArray) parser.parse(new FileReader(file));
			for (Object o : a) {
				Document doc = Document.parse(o.toString());
				mongoTemplate.save(doc, "recordMetrics");

			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		// FileMetrics Collection
		parser = new JSONParser();
		JSONArray b;
		file = new File(this.getClass().getClassLoader()
				.getResource("fileMetrics.json").getFile());
		try {
			b = (JSONArray) parser.parse(new FileReader(file));
			for (Object o : b) {
				Document doc = Document.parse(o.toString());
				mongoTemplate.save(doc, "fileMetrics");
			}
		} catch (IOException | ParseException e) {

			e.printStackTrace();
		}

		/// repoMetrics collection;
		parser = new JSONParser();
		file = new File(this.getClass().getClassLoader()
				.getResource("repoMetrics.json").getFile());
		try {
			a = (JSONArray) parser.parse(new FileReader(file));
			for (Object o : a) {
				Document doc = Document.parse(o.toString());
				mongoTemplate.save(doc, "repoMetrics");
			}
		} catch (IOException | ParseException e) {

			e.printStackTrace();
		}

		/// uniqueUsers collection;
		parser = new JSONParser();
		mongoTemplate.createCollection("uniqueUsers");
		file = new File(this.getClass().getClassLoader()
				.getResource("uniqueUsers.json").getFile());
		try {
			a = (JSONArray) parser.parse(new FileReader(file));
			for (Object o : a) {
				Document doc = Document.parse(o.toString());

				mongoTemplate.save(doc, "uniqueUsers");
			}
		} catch (IOException | ParseException e) {

			e.printStackTrace();
		}

	}

	@DisplayName("given object to save"
			+ " when save object using MongoDB template"
			+ " then object is saved")
	@Test
	void test() throws Exception {
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
	@Override
	public Document findRecord(String id,
			MultiValueMap<String, String> params) {

		if (id != null)
			params.add("ediid", id);
		return this.processInputAndData(
				mongoTemplate.getCollection("recordMetrics"), params,
				"DataSetMetrics");

	}

	@Test
	public void testFindRecords() {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		Document r = findRecord("", params);
		long resCnt = 20;
		assertEquals(r.get("DataSetMetricsCount"), resCnt);
	}
	@Override
	public Document findFile(String recordid, String filepath,
			MultiValueMap<String, String> params) {
		if (!recordid.isEmpty() && filepath.isEmpty()) {
			params.add("ediid", recordid);
			return this.processInputAndData(
					mongoTemplate.getCollection("fileMetrics"), params,
					"FilesMetrics");
		} else if (!filepath.isEmpty()) {
			params.add("filepath", filepath);
			return this.processInputAndData(
					mongoTemplate.getCollection("fileMetrics"), params,
					"FilesMetrics");
		}
		return this.processInputAndData(
				mongoTemplate.getCollection("fileMetrics"), params,
				"FilesMetrics");

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
		r = findFile("",
				"686BF41FF1D773D3E0532457068166DD1903/200m.Indy.v3.2.totals.local.csv",
				params);
		rcount = 1;
		assertEquals(r.get("FilesMetricsCount"), rcount);
	}
	@Override
	public Document findRepo(MultiValueMap<String, String> params) {

		return processInputAndData(mongoTemplate.getCollection("repoMetrics"),
				params, "RepoMetrics");
	}

	@Test
	public void TestFindRepo() {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		Document repo = findRepo(params);
		long count = 1;
		assertEquals(repo.get("RepoMetricsCount"), count);
	}

	public Document totalUsers(MultiValueMap<String, String> params) {
		return processInputAndData(mongoTemplate.getCollection("uniqueUsers"),
				params, "TotalUsers");

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

	private Document processInputAndData(MongoCollection<Document> mdCollection,
			MultiValueMap<String, String> params, String collectionName) {

		ProcessRequest request = new ProcessRequest();
		request.parseSearch(params);

		long count = 0;
		if (request.getFilter() == null)
			count = mdCollection.countDocuments();

		else {
			Bson b = request.getFilter();

			count = mdCollection.countDocuments(b);

		}
		logger.info("Count:" + count);

		List<Bson> dList = request.getQueryList();

		AggregateIterable<Document> ag = mdCollection.aggregate(dList);

		Document resultDoc = new Document();
		resultDoc.put(collectionName + "Count", count);
		resultDoc.put("PageSize", request.getPageSize());
		resultDoc.put(collectionName, ag);
		return resultDoc;
	}

}