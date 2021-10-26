package io.thundra.demo.localstack.integration;

import io.thundra.demo.localstack.LocalStackTest;
import io.thundra.demo.localstack.model.User;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAppTest extends LocalStackTest {
    @Test
    public void testCreateNewRequest() throws IOException {
        ResponseEntity<User> getUser = get(getUserLambdaUrl+"/1", User.class);
        assertThat(getUser.getStatusCode()).isEqualTo(HttpStatus.SC_OK); 

        // HttpPost request = new HttpPost(postUserLambdaUrl);
        // request.setHeader("content-type", "application/json");
        // User defaultUser = new User(1, "Mansi", "abc", "xyz");
        // StringEntity userEntity = new StringEntity(defaultUser.toString());
        // request.setEntity(userEntity);
        // ResponseEntity<User> postUser = post(request, User.class);
        // assertThat(postUser.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
    }
}
