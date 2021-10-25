package io.thundra.demo.localstack.integration;

import io.thundra.demo.localstack.LocalStackTest;
import io.thundra.demo.localstack.model.User;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAppTest extends LocalStackTest {
    @Test
    public void testCreateNewRequest() throws IOException {
        assertEventually(() -> {
            try {
                ResponseEntity<User> responseEntity = get(lambdaUrl, User.class);
                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        });
    }
}
