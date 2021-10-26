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
    public void testGetUserRequest() throws IOException {
        ResponseEntity<User> getUserResponse = get(lambdaUrl + "/1", User.class);
        System.out.println("MB - Testing getUserRequest " + getUserResponse);
        assertThat(getUserResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
    }

    @Test
    public void testPostUserRequest() throws IOException {
        HttpPost postUserRequest = new HttpPost(lambdaUrl);
        User defaultUser = new User(2, "test", "test", "test");
        StringEntity userEntity = new StringEntity(mapper.writeValueAsString(defaultUser), ContentType.APPLICATION_JSON);
        postUserRequest.setEntity(userEntity);
        System.out.println("MB - Testing postUserRequest " + postUserRequest);
        ResponseEntity<User> postUserResponse = post(postUserRequest, User.class);
        assertThat(postUserResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
    }
}
