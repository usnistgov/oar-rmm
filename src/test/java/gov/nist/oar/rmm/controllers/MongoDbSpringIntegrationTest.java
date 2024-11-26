package gov.nist.oar.rmm.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.context.SpringBootTest;

import gov.nist.oar.rmm.config.AppConfig;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

@SpringBootTest(properties = "spring.cloud.config.enabled=false")
@TestPropertySource("/bootstrap-test.yml")
@ContextConfiguration(classes = AppConfig.class)
// @DataMongoTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
public class MongoDbSpringIntegrationTest {
    @DisplayName("Given object When save object using MongoDB template Then object can be found")
    @Test
    public void test(@Autowired MongoTemplate mongoTemplate) {
        // given
        DBObject objectToSave = BasicDBObjectBuilder.start()
                .add("key", "value")
                .get();

        // when
        mongoTemplate.save(objectToSave, "collection");

        // then
        assertThat(mongoTemplate.findAll(DBObject.class, "collection")).extracting("key")
                .containsOnly("value");
    }
}
