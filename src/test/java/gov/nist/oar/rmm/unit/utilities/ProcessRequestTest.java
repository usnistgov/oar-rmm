package gov.nist.oar.rmm.unit.utilities;

import java.util.List;

import org.bson.conversions.Bson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import gov.nist.oar.rmm.utilities.ProcessRequest;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProcessRequestTest {
	
	
	
	@Test
	public void parseSearchTest() {
		ProcessRequest pr = new ProcessRequest();
		  MultiValueMap<String, String>params = new LinkedMultiValueMap<String, String>();
		  params.add("title", "Enterprise Data Inventory");
		  pr.parseSearch(params);
		  
		 List<Bson> queryList= pr.getQueryList();
		 System.out.println("queryList :"+queryList.get(0));
		
	}

}
