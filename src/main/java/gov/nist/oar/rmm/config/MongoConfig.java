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
import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Configuration
@ConfigurationProperties	
/**
 * MongoDB configuration, reading all the conf details from 
 * application.yml
 * @author dsn1
 *
 */
public class MongoConfig {

	 private static Logger log = LoggerFactory.getLogger(MongoConfig.class);
	 
	 //@Autowired
	 MongoClient mongoClient;
	 
	 private MongoDatabase mongoDb;
	 private MongoCollection<Document> recordsCollection;
	 private MongoCollection<Document> taxonomyCollection;
	 private MongoCollection<Document> resourceApiCollection;
	 private MongoCollection<Document> recordFieldsCollection;
	 List servers=new ArrayList();
	 List credentials=new ArrayList();
	 
//	 @Value("${spring.data.mongodb.database}")
//	    private String dbname;
	 
	 @Value("${dbcollections.records}")
	    private String record;
	 
	 @Value("${dbcollections.taxonomy}")
	    private String taxonomy;
	 
	 @Value("${dbcollections.resources}")
	    private String resourceApi;
	 
	 @Value("${dbcollections.recordfields}")
	    private String rfields;
	 
	
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
		
		 mongoClient= (MongoClient) this.mongo();
		 log.info("########## "+ dbname+" ########");
	
		 this.setMongodb(this.dbname);
		 this.setRecordCollection(this.record);
		 this.setTaxonomyCollection(this.taxonomy);
		 this.setResourceApiCollection(this.resourceApi);
		 this.setRecordFieldsCollection(this.rfields);
		 
	 }
	 
	 /**
	  * Get mongodb database name
	  * @return
	  */
	
	 public MongoDatabase getMongoDb(){
		 return mongoDb;
	 }
	 /**
	  * Set mongodb database name
	  * @param dbname
	  */
	 private void setMongodb(String dbname){
		mongoDb =  mongoClient.getDatabase(dbname);
	 }

	 /***
	  * Get records collection from Mongodb
	  * @return
	  */
	 public MongoCollection<Document> getRecordCollection(){
		 return   recordsCollection;
	 }
	 /**
	  * Set records collection 
	  */
	 private void setRecordCollection(String record){
		 recordsCollection = mongoDb.getCollection(record);
	 }
	 /***
	  * Get taxonomy collection
	  * @return
	  */
	 public MongoCollection<Document> getTaxonomyCollection(){
		 return taxonomyCollection;
	 }
	 /**
	  * Set taxonomy collection
	  * @param taxonomy
	  */
	 private void setTaxonomyCollection(String taxonomy){
		 taxonomyCollection = mongoDb.getCollection(taxonomy);
	 }
	 /***
	  * get ResourceApi collection
	  * @return
	  */
	 public MongoCollection<Document> getResourceApiCollection(){
		 return resourceApiCollection;
	 }
	 /**
	  * Set resourceApi collection
	  * @param resourceApi
	  */
	 private void setResourceApiCollection(String resourceApi){
		 resourceApiCollection = mongoDb.getCollection(resourceApi);
	 }
	 /***
	  * Get record collections fields collection
	  * @return
	  */
	 public MongoCollection<Document> getRecordFieldsCollection(){
		 return recordFieldsCollection;
	 }
	 /***
	  * Set record collections fields collection
	  * @param recordFields
	  */
	 private void setRecordFieldsCollection(String recordFields){
		 recordFieldsCollection = mongoDb.getCollection(recordFields);
	 }
	 
	
	 
	 public Mongo mongo() throws Exception {
		 servers.add(new ServerAddress(host, port));
		 credentials.add(MongoCredential.createCredential(user,dbname, password.toCharArray()));
	     return new MongoClient(servers, credentials);
	 }
}
