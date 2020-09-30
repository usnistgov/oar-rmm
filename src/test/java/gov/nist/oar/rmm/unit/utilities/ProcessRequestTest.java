package gov.nist.oar.rmm.unit.utilities;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.conversions.Bson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import gov.nist.oar.rmm.utilities.ProcessRequest;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProcessRequestTest {
	

	@Test
	public void parseSearchTest() {
		ProcessRequest pr = new ProcessRequest();
		  MultiValueMap<String, String>params = new LinkedMultiValueMap<String, String>();
		  params.add("title", "Enterprise Data Inventory");
		  pr.parseSearch(params);
		  
		  List<Pattern> patternList = new ArrayList<Pattern>();
		  patternList.add(Pattern.compile(params.get("title").get(0), Pattern.CASE_INSENSITIVE));
		  Bson f1 = Filters.in("title", patternList);
		 	
		 Bson b1 = Aggregates.match(f1);
		 Bson b2 =  Aggregates.sort(Sorts.metaTextScore("score"));
		 List<Bson> queryList= pr.getQueryList();

		 assertEquals(b1.toString(), queryList.get(0).toString());

	}
	
	@Test
	public void parseSearchTest2() {
		ProcessRequest pr = new ProcessRequest();
		  MultiValueMap<String, String>params = new LinkedMultiValueMap<String, String>();
		  params.add("searchphrase","statistics");
		  params.add("topic.tag", "physics"); 
		  pr.parseSearch(params);
		  
		  List<Pattern> patternList = new ArrayList<Pattern>();
		  patternList.add(Pattern.compile(params.get("topic.tag").get(0), Pattern.CASE_INSENSITIVE));
		  Bson f1 = Filters.in("topic.tag", patternList);

		 Bson b1 = Aggregates.match(Filters.and(Filters.text("statistics"), f1));
		 Bson b3 =  Aggregates.sort(Sorts.metaTextScore("score"));
		 List<Bson> queryList= pr.getQueryList();

		 assertEquals(b1.toString(), queryList.get(0).toString());
		 assertEquals(b3.toString(), queryList.get(1).toString());
	}
	
	@Test
	public void validateInput() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("keyword", "Chemistry");
		map.add("topic.tag", "Chemistry");
		
		List<Pattern> patternList = new ArrayList<Pattern>();
		patternList.add(Pattern.compile(map.get("keyword").get(0), Pattern.CASE_INSENSITIVE));
		Bson f1 = Filters.in("keyword", patternList);
		
		patternList = new ArrayList<Pattern>();
		patternList.add(Pattern.compile(map.get("topic.tag").get(0), Pattern.CASE_INSENSITIVE));
		Bson f2 = Filters.in("topic.tag", patternList);
		
		Bson f3 = Filters.and(f1, f2);
		
		ProcessRequest pr = new ProcessRequest();
		pr.parseSearch(map);
		
		List<Bson> queryList = pr.getQueryList();
		
		assertEquals(f3.toString(), pr.getFilter().toString());

	}
	
	@Test(expected = IllegalArgumentException.class)
	public void IllegalArgumentTest() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("searchphrase", "Chemistry");
		map.add("searchphrase", "Chemistry");
		
		ProcessRequest pr = new ProcessRequest();
		pr.parseSearch(map);
		
		List<Bson> queryList = pr.getQueryList();
		

	}
	
	@Test(expected = IllegalArgumentException.class)
	public void IllegalArgumentTest2() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("keyword", "Chemistry");
		map.add("searchphrase", "Chemistry");
		
		ProcessRequest pr = new ProcessRequest();
		pr.parseSearch(map);
		
		List<Bson> queryList = pr.getQueryList();
		

	}
	
	@Test(expected = IllegalArgumentException.class)
	public void IllegalArgumentTest3() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		
		map.add("searchphrase", "Chemistry");
		map.add("logicalOp", "Or");
		map.add("keyword", "Chemistry");
		
		ProcessRequest pr = new ProcessRequest();
		pr.parseSearch(map);
		
		List<Bson> queryList = pr.getQueryList();
		

	}

}
