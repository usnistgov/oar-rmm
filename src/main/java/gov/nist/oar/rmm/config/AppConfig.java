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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UrlPathHelper;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication
@RefreshScope
@ComponentScan(basePackages = { "gov.nist.oar.rmm" })
/**
 * Application configuration. Spring Boot initialization.
 * 
 * @author Deoyani Nandrekar-Heinis
 * 
 *
 */
public class AppConfig {

    private static Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Value("${oar.id.ark_naan.default}")
    private String defnaan;

    /** return the default NAAN associated with ARK identifiers in the repository */
    public String getDefaultNAAN() {
	return defnaan;
    }

    /**
     * Main runner of the spring-boot class
     * 
     * @param args
     */

    public static void main(String... args) {
	log.info("########## Starting oar rmm service ########");
	SpringApplication.run(AppConfig.class, args);
    }

    /**
     * Add CORS
     * 
     * @return
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
	return new WebMvcConfigurerAdapter() {
	    @Override
	    public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	    }
	    

	    @Override
            public void configurePathMatch(PathMatchConfigurer configurer) {
                UrlPathHelper uhlpr = configurer.getUrlPathHelper();
                if (uhlpr == null) {
                    uhlpr = new UrlPathHelper();
                    configurer.setUrlPathHelper(uhlpr);
                }
                uhlpr.setRemoveSemicolonContent(false);
	        configurer.setUseRegisteredSuffixPatternMatch(true);
	        configurer.setUseSuffixPatternMatch(false);
            }
	    
	};
    }
    
    @Bean
    public OpenAPI customOpenAPI(@Value("1.1.0") String appVersion) {
	appVersion = VERSION;
       return new OpenAPI()
//        .components(new Components().addSecuritySchemes("basicScheme",
//                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
	       .components(new Components())
        .info(new Info().title("Resource Metadata Management API").version(appVersion)
                .license(new License().name("NIST Software").url("https://www.nist.gov/open/copyright-fair-use-and-licensing-statements-srd-data-software-and-technical-series-publications")));
    }
    
    
    /**
     * The service name
     */
    public final static String NAME;

    /**
     * The version of the service
     */
    public final static String VERSION;

    static {
        String name = null;
        String version = null;
        try (InputStream verf =  AppConfig.class.getClassLoader().getResourceAsStream("VERSION")) {
            if (verf == null) {
                name = "oar-rmm";
                version = "not set";
            }
            else {
                BufferedReader vrdr = new BufferedReader(new InputStreamReader(verf));
                String line = vrdr.readLine();
                String[] parts = line.split("\\s+");
                name = parts[0];
                version = (parts.length > 1) ? parts[1] : "missing";
            }
        } catch (Exception ex) {
            name = "oar-rmm";
            version = "unknown";
        }
        NAME = name;
        VERSION = version;
    }
    

}
