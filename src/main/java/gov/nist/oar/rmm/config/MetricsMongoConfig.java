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

import  jakarta.annotation.PostConstruct;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.System;
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
public class MetricsMongoConfig {

	private static Logger log = LoggerFactory.getLogger(MongoConfig.class);

	// @Autowired
	MongoClient mongoClient;

	private MongoDatabase  mongoDb;

	//these are collections to collect and serve logs from parsing distribution service
	private MongoCollection<Document> recordMetricsCollection;
	private MongoCollection<Document> fileMetricsCollection;
	private MongoCollection<Document> uniqueUsersCollection;
	private MongoCollection<Document> repoMetricsCollection;

	List<ServerAddress> servers = new ArrayList<ServerAddress>();
	List<MongoCredential> credentials = new ArrayList<MongoCredential>();


	@Value("${dbcollections.recordMetrics}")
	private String recordMetrics;
	
	@Value("${dbcollections.fileMetrics}")
	private String fileMetrics;
	
	@Value("${dbcollections.uniqueUsers}")
	private String uniqueusers;
	
	@Value("${dbcollections.repoMetrics}")
	private String repoMetrics;

	@Value("${oar.mongodb.read.user}")
	private String user;
	@Value("${oar.mongodb.read.password}")
	private String password;

	@Value("${oar.metrics.mongodb.port}")
	private int metricsport;
	@Value("${oar.metrics.mongodb.host}")
	private String metricshost;
	@Value("${oar.metrics.mongodb.database.name}")
	private String metricsdbname;


	@PostConstruct
	public void initIt() throws Exception {

		mongoClient = (MongoClient) this.mongo();
		log.info("########## Metrics databasenaem:" + metricsdbname + " ########");

		this.setMongodb(this.metricsdbname);

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
	
	

	/**
	 * MongoClient : Initialize mongoclient for db operations
	 * 
	 * @return
	 * @throws Exception
	 */
	public MongoClient mongo() throws Exception {
            String dburl = "mongodb://"+metricshost+":"+metricsport;
			// System.out.println("dburl ::"+ dburl);
            MongoCredential credential = MongoCredential.createCredential(user, metricsdbname,
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
