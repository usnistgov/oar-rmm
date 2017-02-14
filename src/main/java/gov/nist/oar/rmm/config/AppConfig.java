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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
@SpringBootApplication
@ComponentScan(basePackages = {"gov.nist.oar.rmm"})
//@EnableMongoRepositories
public class AppConfig {

	 private static Logger log = LoggerFactory.getLogger(AppConfig.class);

	  /**
	   * Main runner of the spring-boot class
	   * 
	   * @param args
	   */
	  
	  public static void main(String... args) {
	    log.info("########## Starting oar rmm service ########");
	    SpringApplication.run(AppConfig.class, args);
	  }
}

