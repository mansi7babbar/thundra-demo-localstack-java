package io.thundra.demo.localstack.integration;

import io.thundra.demo.localstack.LocalStackTest;
import io.thundra.demo.localstack.model.User;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAppTest extends LocalStackTest {
    @Test
    public void testCreateNewRequest() throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
//        ResponseEntity<User> responseEntity = get(lambdaUrl, User.class);
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        HttpPost postRequest = new HttpPost(lambdaUrl);
        postRequest.addHeader("content-type", "application/json");
        User defaultUser = new User(1, "Mansi", "abc", "xyz");
        StringEntity userEntity = new StringEntity(defaultUser.toString());
        postRequest.setEntity(userEntity);
        HttpResponse response = httpClient.execute(postRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode).isEqualTo(HttpStatus.SC_OK);
    }
}
