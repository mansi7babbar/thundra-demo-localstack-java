package io.thundra.demo.localstack.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.thundra.demo.localstack.LocalStackTest;
import io.thundra.demo.localstack.model.User;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAppTest extends LocalStackTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetUserRequest() throws IOException {
        ResponseEntity<User> getUser = get(lambdaUrl + "/1", User.class);
        System.out.println("MB - Testing getUserRequest " + getUser);
        assertThat(getUser.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
    }

    @Test
    public void testPostUserRequest() throws IOException {
        HttpPost request = new HttpPost(lambdaUrl);
        User defaultUser = new User(1, "Mansi", "abc", "xyz");
        StringEntity userEntity = new StringEntity(mapper.writeValueAsString(defaultUser));
        request.setEntity(userEntity);
        System.out.println("MB - Testing postUserRequest " + request);
        ResponseEntity<User> postUser = post(request, User.class);
        assertThat(postUser.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
    }
}
