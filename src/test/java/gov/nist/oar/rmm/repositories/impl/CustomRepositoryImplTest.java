// package gov.nist.oar.rmm.repositories.impl;

// import com.fasterxml.jackson.databind.ObjectMapper;

// import de.flapdoodle.embed.mongo.commands.MongoImportArguments;
// import de.flapdoodle.embed.mongo.commands.ServerAddress;
// import de.flapdoodle.embed.mongo.config.Net;
// import de.flapdoodle.embed.mongo.distribution.Version;
// import de.flapdoodle.embed.mongo.transitions.ExecutedMongoImportProcess;
// import de.flapdoodle.embed.mongo.transitions.MongoImport;
// import de.flapdoodle.embed.mongo.transitions.Mongod;
// import de.flapdoodle.embed.mongo.transitions.RunningMongodProcess;
// import de.flapdoodle.reverse.StateID;
// import de.flapdoodle.reverse.TransitionWalker;
// import de.flapdoodle.reverse.Transitions;
// import de.flapdoodle.reverse.transitions.Start;
// import gov.nist.oar.rmm.config.AppConfig;

// import org.junit.jupiter.api.AfterAll;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.System;
// // import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties.Application;
// import org.springframework.boot.autoconfigure.ssl.SslProperties.Bundles.Watch.File;
// import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
// import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.test.context.event.annotation.AfterTestClass;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
// import org.springframework.test.context.junit4.SpringRunner;

// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Objects;

// import static org.assertj.core.api.Assertions.assertThat;

// @AutoConfigureMockMvc
// @AutoConfigureDataMongo
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
// @ActiveProfiles("test")
// @TestPropertySource("classpath:bootstrap-test.yml")


// public class CustomRepositoryImplTest {

// //  private BooksController booksController;

//  @Autowired
//  private CustomRepositoryImpl customRepositoryImpl;

//  private final ObjectMapper objectMapper = new ObjectMapper();

//  private static TransitionWalker.ReachedState<RunningMongodProcess> mongoDProcess;

//  private static TransitionWalker.ReachedState<ExecutedMongoImportProcess> mongoImportProcess;

//  @BeforeAll
//  public static void setUp() {
//   String os = System.getProperty("os.name");
  

//   // String path = getClass().getClassLoader().getResource("fields.json").getPath();
  
// String path = "something";

//   if (os != null && os.toLowerCase().contains("windows")) {
//    path = path.substring(1);
//   }

//   MongoImportArguments arguments = MongoImportArguments.builder()
//     .databaseName("TestDB")
//     .collectionName("fields")
//     .importFile(path)
//     .isJsonArray(true)
//     .upsertDocuments(true)
//     .build();

//   Version.Main version = Version.Main.PRODUCTION;

//   mongoDProcess = Mongod.builder()
//     .net(Start.to(Net.class).initializedWith(Net.defaults().withPort(27124)) )
//     .build()
//     .transitions(version)
//     .walker()
//     .initState(StateID.of(RunningMongodProcess.class));

//   Transitions mongoImportTransitions = MongoImport.instance()
//     .transitions(version)
//     .replace(Start.to(MongoImportArguments.class).initializedWith(arguments))
//     .addAll(Start.to(ServerAddress.class).initializedWith(mongoDProcess.current().getServerAddress()));

//   mongoImportProcess = mongoImportTransitions.walker().initState(StateID.of(ExecutedMongoImportProcess.class));

//   // booksController = new BooksController(booksService);
//  }

//  @AfterAll
//  public static void tearDownAfterAll() {
//   mongoImportProcess.close();
//   mongoDProcess.close();
//  }

//  @AfterTestClass
//  public static void tearDownAfterTestClass() {
//   mongoImportProcess.close();
//   mongoDProcess.close();
//  }

//  @Test
//  public void testSearch() throws IOException {
//   // List<org.bson.Document> filedList = customRepositoryImpl.findFieldnames();

//   String fields = Files.readString(Paths.get("src/test/resources/fields.json"));
 
//   // System.out.println(fields);
//   // assertThat(filedList).usingRecursiveComparison()
//   //   .isEqualTo(Arrays.asList(fields));
//  }
// }
