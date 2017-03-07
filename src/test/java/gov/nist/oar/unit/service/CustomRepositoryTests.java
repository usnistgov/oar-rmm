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
 * 
 * @author:Deoyani Nandrekar-Heinis
 */
package gov.nist.oar.unit.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.fakemongo.Fongo;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.mongodb.Mongo;

import gov.nist.oar.rmm.repositories.CustomRepository;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import org.bson.Document;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
public class CustomRepositoryTests {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private CustomRepository customRepository;

//	@Rule
//	public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("test-db");


	
//	@Test
//	@ShouldMatchDataSet(location = "/testdata1.json")
//	public void test1(){
//		//String test = null;
//		//assertNull(test);
//		ObjectMapper mapper = new ObjectMapper();
//		//Record r =createRecord();
//		Record r = recordRepository.save(createRecord());
//	
////	    List<Record> rm = recordRepository.findByTitleContainingIgnoreCase("SRD");	
//	    try{
//		System.out.println( recordRepository.count()+" ::"+ mapper.writeValueAsString(r));
//	    }catch(Exception e){
//	    	System.out.println(e);
//	    }
//	}
	
	
	@Test
	//@UsingDataSet(locations = {"/testdata1.json"})
	public void shouldFindByTitle(){
		Map<String,String> param = new HashMap<>();
		param.put("title", "SRD 69");
		Document resultDoc = customRepository.find(param);
		//assertNotNull(param);
		//assertTrue(param.size() > 0);
		assertEquals("NIST Chemistry WebBook - SRD 69", resultDoc.getString("title"));
	}
	
//	@Test
//	@UsingDataSet(locations = {"/testdata1.json"})
//	public void shouldFindByKeyword(){
//		final List <Record> l = recordRepository.findByKeywordContainingIgnoreCase("IR spectrum");
//		assertNotNull(l);
//		assertTrue(l.size() > 0);
//		assertEquals(new String[]{"IR spectrum","InChI" }, l.get(0).getKeyword());
//	}

	
	@Configuration
	@EnableMongoRepositories
	static class MongoConfiguration  {

		protected String getDatabaseName() {
			return "test-db";
		}

		public Mongo mongo() {
			return new Fongo("TestDB").getMongo();
		}

		protected String getMappingBasePackage() {
			return "gov.nist.oar";
		}
	}
}
