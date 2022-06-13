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
package gov.nist.oar.rmm.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
//import com.mongodb.Mongo;
//import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Configuration
@ConfigurationProperties
@EnableAutoConfiguration
/**
 * MongoDB configuration, reading all the conf details from application.yml
 * 
 * @author Deoyani Nandrekar-Heinis
 *
 */
public class MongoConfig {

	private static Logger log = LoggerFactory.getLogger(MongoConfig.class);

	// @Autowired
	MongoClient mongoClient;

	private MongoDatabase mongoDb;
	private MongoCollection<Document> recordsCollection;
	private MongoCollection<Document> taxonomyCollection;
	private MongoCollection<Document> resourceApiCollection;
	private MongoCollection<Document> recordFieldsCollection;
	//these are collections to collect and serve logs from parsing distribution service
	private MongoCollection<Document> recordMetricsCollection;
	private MongoCollection<Document> fileMetricsCollection;
	private MongoCollection<Document> uniqueUsersCollection;
	private MongoCollection<Document> repoMetricsCollection;
	List<ServerAddress> servers = new ArrayList<ServerAddress>();
	List<MongoCredential> credentials = new ArrayList<MongoCredential>();

	@Value("${dbcollections.records}")
	private String record;

	@Value("${dbcollections.taxonomy}")
	private String taxonomy;

	@Value("${dbcollections.resources}")
	private String resourceApi;

	@Value("${dbcollections.recordfields}")
	private String rfields;

	@Value("${dbcollections.recordMetrics}")
	private String recordMetrics;
	
	@Value("${dbcollections.fileMetrics}")
	private String fileMetrics;
	
	@Value("${dbcollections.uniqueUsers}")
	private String uniqueusers;
	
	@Value("${dbcollections.repoMetrics}")
	private String repoMetrics;
	
	@Value("${oar.mongodb.port}")
	private int port;
	@Value("${oar.mongodb.host}")
	private String host;
	@Value("${oar.mongodb.database.name}")
	private String dbname;
	@Value("${oar.mongodb.read.user}")
	private String user;
	@Value("${oar.mongodb.read.password}")
	private String password;

	@PostConstruct
	public void initIt() throws Exception {

		mongoClient = (MongoClient) this.mongo();
		log.info("########## " + dbname + " ########");

		this.setMongodb(this.dbname);
		this.setRecordCollection(this.record);
		this.setTaxonomyCollection(this.taxonomy);
		this.setResourceApiCollection(this.resourceApi);
		this.setRecordFieldsCollection(this.rfields);
		this.setRecordMetricsCollection(recordMetrics);
		this.setfileMetricsCollection(fileMetrics);
		this.setUniqueUsersMetricsCollection(uniqueusers);
		this.setRepoMetricsCollection(repoMetrics);

	}

	/**
	 * Get mongodb database name
	 * 
	 * @return
	 */

	public MongoDatabase getMongoDb() {
		return mongoDb;
	}

	/**
	 * Set mongodb database name
	 * 
	 * @param dbname
	 */
	private void setMongodb(String dbname) {
		mongoDb = mongoClient.getDatabase(dbname);
	}

	/***
	 * Get records collection from Mongodb
	 * 
	 * @return
	 */
	public MongoCollection<Document> getRepoMetricsCollection() {
		return this.repoMetricsCollection;
	}

	/**
	 * Set records collection
	 */
	private void setRepoMetricsCollection(String repoMetrics) {
	    this.repoMetricsCollection = mongoDb.getCollection(repoMetrics);
	}
	
	/***
	 * Get records collection from Mongodb
	 * 
	 * @return
	 */
	public MongoCollection<Document> getUniqueUsersMetricsCollection() {
		return this.uniqueUsersCollection;
	}

	/**
	 * Set records collection
	 */
	private void setUniqueUsersMetricsCollection(String uniqueusers) {
	    this.uniqueUsersCollection = mongoDb.getCollection(uniqueusers);
	}
	
	/***
	 * Get records collection from Mongodb
	 * 
	 * @return
	 */
	public MongoCollection<Document> getfileMetricsCollection() {
		return fileMetricsCollection;
	}

	/**
	 * Set records collection
	 */
	private void setfileMetricsCollection(String fileMetrics) {
	    fileMetricsCollection = mongoDb.getCollection(fileMetrics);
	}
	
	
	/***
	 * Get records collection from Mongodb
	 * 
	 * @return
	 */
	public MongoCollection<Document> getRecordMetricsCollection() {
		return recordMetricsCollection;
	}

	/**
	 * Set records collection
	 */
	private void setRecordMetricsCollection(String recordMetrics) {
	    recordMetricsCollection = mongoDb.getCollection(recordMetrics);
	}
	
	/***
	 * Get records collection from Mongodb
	 * 
	 * @return
	 */
	public MongoCollection<Document> getRecordCollection() {
		return recordsCollection;
	}

	/**
	 * Set records collection
	 */
	private void setRecordCollection(String record) {
		recordsCollection = mongoDb.getCollection(record);
	}

	/***
	 * Get taxonomy collection
	 * 
	 * @return
	 */
	public MongoCollection<Document> getTaxonomyCollection() {
		return taxonomyCollection;
	}

	/**
	 * Set taxonomy collection
	 * 
	 * @param taxonomy
	 */
	private void setTaxonomyCollection(String taxonomy) {
		taxonomyCollection = mongoDb.getCollection(taxonomy);
	}

	/***
	 * get ResourceApi collection
	 * 
	 * @return
	 */
	public MongoCollection<Document> getResourceApiCollection() {
		return resourceApiCollection;
	}

	/**
	 * Set resourceApi collection
	 * 
	 * @param resourceApi
	 */
	private void setResourceApiCollection(String resourceApi) {
		resourceApiCollection = mongoDb.getCollection(resourceApi);
	}

	/***
	 * Get record collections fields collection
	 * 
	 * @return
	 */
	public MongoCollection<Document> getRecordFieldsCollection() {
		return recordFieldsCollection;
	}

	/***
	 * Set record collections fields collection
	 * 
	 * @param recordFields
	 */
	private void setRecordFieldsCollection(String recordFields) {
		recordFieldsCollection = mongoDb.getCollection(recordFields);
	}

	/**
	 * MongoClient : Initialize mongoclient for db operations
	 * 
	 * @return
	 * @throws Exception
	 */
	public MongoClient mongo() throws Exception {
            String dburl = "mongodb://"+host+":"+port;
            MongoCredential credential = MongoCredential.createCredential(user, dbname,
                                                                          password.toCharArray());
            MongoClientSettings settings = MongoClientSettings.builder() 
                    .credential(credential)
                    .applyConnectionString(new ConnectionString(dburl))
                    .applyToConnectionPoolSettings(builder -> builder.maxWaitTime(10, TimeUnit.SECONDS)
                                                                     .maxSize(200).minSize(5))
                    .applyToSocketSettings(builder -> builder.connectTimeout(10, TimeUnit.SECONDS)
                                                             .readTimeout(15, TimeUnit.SECONDS))
                    .build();

            MongoClient mongoClient = MongoClients.create(settings);
                
                
            return mongoClient;
        }
}
