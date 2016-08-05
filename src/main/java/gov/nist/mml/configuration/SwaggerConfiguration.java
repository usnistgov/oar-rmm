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
package gov.nist.mml.configuration;



import io.swagger.annotations.Api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.ResponseMessage;

@Configuration
@EnableSwagger2
@ComponentScan({"gov.nist.mml"})

public class SwaggerConfiguration {
	
	private static List<ResponseMessage> responseMessageList = new ArrayList<>();

	static {
		responseMessageList.add(new ResponseMessageBuilder().code(500)
				.message("500 - Internal Server Error")
				.responseModel(new ModelRef("Error")).build());
		responseMessageList.add(new ResponseMessageBuilder().code(403)
				.message("403 - Forbidden").build());
	}
  @Bean
  public Docket api() {
	
	 return new Docket(DocumentationType.SWAGGER_2).select()
    .apis(RequestHandlerSelectors.basePackage("gov.nist.mml"))
    //.paths(PathSelectors.ant("/records/*"))        
    .paths(PathSelectors.any())
    .build().apiInfo(apiInfo());//.useDefaultResponseMessages(false).globalResponseMessage(RequestMethod.GET, responseMessageList);
  }

  private ApiInfo apiInfo() {

    ApiInfo apiInfo =
        new ApiInfo("Resource api", 
        		"This REST api exposes data listing from NIST POD schema. There are various search options along with Insert, Delete and Update.",
        		"Sprint-1.0",
        		"This is internal service used by NIST PDL curators.", 
        		"deoyani@nist.gov",
        		"NIST Public licence", 
        		"Licence URL");
    return apiInfo;
  }


}