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
package gov.nist.oar.rmm.unit.configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.fakemongo.junit.FongoRule;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;


@Configuration	
@ConfigurationProperties //("classpath:/application-test.yml")
//@RunWith( SpringJUnit4ClassRunner.class )
public class TestMongoConfiguration {

	 private static Logger log = LoggerFactory.getLogger(TestMongoConfiguration.class);
	 
	 @Rule 
	 public FongoRule fongoRule = new FongoRule(); 
	 
	 
	 private DB mongoDb;
	 private DBCollection recordsCollection;
	 private DBCollection taxonomyCollection;
	 private DBCollection resourceApiCollection;
	 private DBCollection recordFieldsCollection;
	 
	    private String dbname = "DBTest" ;
	 
	    private String record = "testRecord";
	 
	    private String taxonomy = "testTaxonomy";
	 
	    private String resourceApi = "testResourceApi";
	 
	    private String rfields = "testRecordfields";
	 
	// @PostConstruct
	 @Before
	 public void initIt() throws Exception {
	
//	 public TestMongoConfiguration(){
//		 log.info("##########  ########"+ dbname);
		 System.out.println("##########  ########"+dbname +"##########  ########");
 
//		 this.setMongodb(this.dbname);
//		 this.setRecordCollection(this.record);
//		 this.setTaxonomyCollection(this.taxonomy);
//		 this.setResourceApiCollection(this.resourceApi);
//		 this.setRecordFieldsCollection(this.rfields);
		 //DBCollection collection = fongoRule.getDB("testdb2").getCollection("test");
		 //recordsCollection =  fongoRule.getDB(dbname).getCollection(record);
//		 DB db = fongoRule.getDB("testdb");
//		 DBCollection col = db.getCollection("testcol");
		 
		 recordsCollection = fongoRule.getDB(dbname).getCollection(record);
	    	JSONParser parser = new JSONParser();
	    	JSONArray a;
	    	File file = new File(this.getClass().getClassLoader().getResource("dataset.json").getFile());
			try {
				a = (JSONArray) parser.parse(new FileReader(file));
				for (Object o : a)
	    	  {
	    		  //System.out.println(o.toString());
	    	   DBObject dbObject = (DBObject)  com.mongodb.util.JSON.parse(o.toString());
	    	    
	    	   recordsCollection.save(dbObject); 
	    	  }
			} catch (IOException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }
	 
	public String getMDBname(){
		return dbname+"::"+rfields;
	}
	 public DB getMongoDb(){
		 return mongoDb;
	 }
	 private void setMongodb(String dbname){
		mongoDb =  fongoRule.getDB(dbname);
	 }

	 public DBCollection getRecordCollection(){
		 return   recordsCollection;
	 }
	 private void setRecordCollection(String record){
		 recordsCollection = mongoDb.getCollection(record);
	 }
	 
	 public DBCollection getTaxonomyCollection(){
		 return taxonomyCollection;
	 }
	 private void setTaxonomyCollection(String taxonomy){
		 taxonomyCollection = mongoDb.getCollection(taxonomy);
	 }
	 
	 public DBCollection getResourceApiCollection(){
		 return resourceApiCollection;
	 }
	 
	 private void setResourceApiCollection(String resourceApi){
		 resourceApiCollection = mongoDb.getCollection(resourceApi);
	 }
	 
	 public DBCollection getRecordFieldsCollection(){
		 return recordFieldsCollection;
	 }
	 
	 private void setRecordFieldsCollection(String recordFields){
		 recordFieldsCollection = mongoDb.getCollection(recordFields);
	 }
}