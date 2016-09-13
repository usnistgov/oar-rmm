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
package gov.nist.mml.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
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

import gov.nist.mml.domain.Record;
import gov.nist.mml.domain.nestedpod.ContactPoint;
import gov.nist.mml.domain.nestedpod.Distribution;
import gov.nist.mml.domain.nestedpod.Publisher;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TestRecordsRepository {

	@Autowired
    private ApplicationContext applicationContext;

	@Rule
	public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("test-db");

	@Autowired
	private RecordRepository recordRepository;
	
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
	@UsingDataSet(locations = {"/testdata1.json"})
	public void shouldFindByTitle(){
		final List <Record> l = recordRepository.findByTitleContainingIgnoreCase("SRD");
		assertNotNull(l);
		assertTrue(l.size() > 0);
		assertEquals("NIST Chemistry WebBook - SRD 69", l.get(0).getTitle());
	}
	
	@Test
	@UsingDataSet(locations = {"/testdata1.json"})
	public void shouldFindByKeyword(){
		final List <Record> l = recordRepository.findByKeywordContainingIgnoreCase("IR spectrum");
		assertNotNull(l);
		assertTrue(l.size() > 0);
		assertEquals(new String[]{"IR spectrum","InChI" }, l.get(0).getKeyword());
	}

	private Record createRecord(){
	final Record newRecord = new Record();
	newRecord.setType("dcat:Dataset");
	String[] keywords = new String[]{"IR spectrum","InChI" };//,"InChIKey","UV Vis spectrum","boiling point","chemical data","chemical structure","enthalpy","entropy","heat capacity","heat of formation","ionization potential","mass spectrum","retention index","thermochemical data","thermochemistry","thermodynamic data","vapor pressure"};
	newRecord.setKeyword(keywords);
	newRecord.setProgramCode(new String[]{"006:052"});
	newRecord.setLandingPage("http://webbook.nist.gov/chemistry");
	newRecord.setAccessLevel("public");
	 Distribution db = new Distribution();
	 db.setDescription("Landing page for the NIST Chemistry WebBook.");
	 db.setAccessURL("http://webbook.nist.gov/chemistry/");
	 db.setMediaType("text/html");
	newRecord.setDistribution(new Distribution[]{db});
	newRecord.setModified("2013-04-01");
		Publisher pub = new Publisher();
		pub.setType("org:Organization");
		pub.setName("National Institute of Standards and Technology");
	newRecord.setPublisher(pub);
	newRecord.setReferences(new String[]{"http://dx.doi.org/10.1021/je000236i"});
	newRecord.setBureauCode(new String[]{"006:55"});
	
	newRecord.setTitle("NIST Chemistry WebBook - SRD 69");
	newRecord.setDescription("The NIST Chemistry WebBook provides users with easy access to chemical and physical property data for chemical species through the internet. The data provided in the site are from collections maintained by the NIST Standard Reference Data Program and outside contributors. Data in the WebBook system are organized by chemical species. The WebBook system allows users to search for chemical species by various means. Once the desired species has been identified, the system will display data for the species.");
		ContactPoint cp = new ContactPoint();
		cp.setFn("Peter Linstrom");
		cp.sethasmail("mailto:peter.linstrom@nist.gov");
		cp.setType("vcard:Contact");
	newRecord.setContactPoint(cp);
	newRecord.setLanguage(new String[]{"en"});
	newRecord.setLicense("http://www.nist.gov/data/license.cfm");
	newRecord.setIdentifier("EBC9DB05EDEE5B0EE043065706812DF85");
	return newRecord;
}
	
	
	@Configuration
	@EnableMongoRepositories
	@ComponentScan(basePackageClasses = { RecordRepository.class })
	@PropertySource(value = "classpath:restapi.properties")
	static class MongoConfiguration extends AbstractMongoConfiguration {

		@Override
		protected String getDatabaseName() {
			return "test-db";
		}
   
		@Bean(name="recordTemplate")
		@Override
		public Mongo mongo() {
			return new Fongo("test-db").getMongo();
		}

		@Override
		protected String getMappingBasePackage() {
			return "gov.nist.mml";
		}
	}
}
