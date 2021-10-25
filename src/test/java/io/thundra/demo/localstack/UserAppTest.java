package io.thundra.demo.localstack;

import io.thundra.agent.lambda.localstack.LambdaServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class UserAppTest extends LocalStackTest {
    @BeforeAll
    static void beforeAll() throws Exception {
        LambdaServer.start();
    }

    @AfterAll
    static void afterAll() throws Exception {
        LambdaServer.stop();
    }

    @Test
    public void testCreateNewRequest() {
        assertThat(true);
    }
}
