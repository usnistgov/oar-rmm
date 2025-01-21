package gov.nist.oar.rmm.utilities;

import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProcessRequestTest {

    @Test
    public void mytest() {
        System.out.println("Some test");
        assertThat("World", containsString("World"));
    }
}
