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

import java.util.Map;

import javax.annotation.PostConstruct;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Configuration
@ConfigurationProperties	
public class MongoConfig {

	 private static Logger log = LoggerFactory.getLogger(MongoConfig.class);
	 
	 @Autowired
	 MongoClient mongoClient;
	 
	 private MongoDatabase mongoDb;
	 private MongoCollection<Document> recordsCollection;
	 private MongoCollection<Document> taxonomyCollection;
	 
	 
	 
	 @Value("${spring.data.mongodb.database}")
	    private String dbname;
	 
	 @Value("${dbcollections.records}")
	    private String record;
	 
	 @Value("${dbcollections.taxonomy}")
	    private String taxonomy;
	 
	 @PostConstruct
	 public void initIt() throws Exception {
		 log.info("##########  ########"+ dbname);
		 System.out.println("##########  ########"+dbname +"##########  ########");
		  
		 this.setMongodb(this.dbname);
		 this.setRecordCollection(this.record);
	 }
	 
	
	 public MongoDatabase getMongoDb(){
		 return mongoDb;
	 }
	 private void setMongodb(String dbname){
		mongoDb =  mongoClient.getDatabase(dbname);
	 }

	 public MongoCollection<Document> getRecordCollection(){
		 return recordsCollection;
	 }
	 private void setRecordCollection(String record){
		 recordsCollection = mongoDb.getCollection(record);
	 }
	 
	 public MongoCollection<Document> getTaxonomyCollection(){
		 return taxonomyCollection;
	 }
	 private void setTaxonomyCollection(String taxonomy){
		 recordsCollection = mongoDb.getCollection(taxonomy);
	 }
	 
}
