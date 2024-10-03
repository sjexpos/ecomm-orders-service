package io.oigres.ecomm.service.orders;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties"
)
@SetEnvironmentVariable(key = "AWS_ENDPOINT", value = "http://localhost:4566")
@SetEnvironmentVariable(key = "AWS_REGION", value = "us-east-1")
@SetEnvironmentVariable(key = "AWS_DEFAULT_REGION", value = "us-east-1")
@SetEnvironmentVariable(key = "AWS_ACCESS_KEY_ID", value = "test")
@SetEnvironmentVariable(key = "AWS_SECRET_ACCESS_KEY", value = "test")
public class BootstrapTest {

    @Test
    void contextLoads() {
    }

}
