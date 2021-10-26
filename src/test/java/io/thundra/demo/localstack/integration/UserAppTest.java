package io.thundra.demo.localstack.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.thundra.demo.localstack.LocalStackTest;
import io.thundra.demo.localstack.model.User;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAppTest extends LocalStackTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPostUserRequest() throws IOException {
        HttpPost postUserRequest = new HttpPost(lambdaUrl);
        User defaultUser = new User(1, "test", "test", "test");
        StringEntity userEntity = new StringEntity(mapper.writeValueAsString(defaultUser), ContentType.APPLICATION_JSON);
        postUserRequest.setEntity(userEntity);
        ResponseEntity<User> postUserResponse = post(postUserRequest, User.class);
        assertThat(postUserResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
    }

    @Test
    public void testGetUserRequest() throws IOException {
        HttpPost postUserRequest = new HttpPost(lambdaUrl);
        User defaultUser = new User(1, "test", "test", "test");
        StringEntity userEntity = new StringEntity(mapper.writeValueAsString(defaultUser), ContentType.APPLICATION_JSON);
        postUserRequest.setEntity(userEntity);
        post(postUserRequest, User.class);
        ResponseEntity<User> getUserResponse = get(lambdaUrl + "/1", User.class);
        assertThat(getUserResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
    }
}
