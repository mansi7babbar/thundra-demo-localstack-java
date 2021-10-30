package io.thundra.demo.localstack.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.thundra.demo.localstack.LocalStackTest;
import io.thundra.demo.localstack.model.Restaurant;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantAppTest extends LocalStackTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPostRestaurantRequest() throws IOException {
        HttpPost postUserRequest = new HttpPost(restaurantLambdaUrl);
        List<String> cuisines = new ArrayList<>();
        cuisines.add("Chinese");
        Restaurant defaultRestaurant = new Restaurant(1, "test", "test", cuisines);
        StringEntity userEntity = new StringEntity(mapper.writeValueAsString(defaultRestaurant), ContentType.APPLICATION_JSON);
        postUserRequest.setEntity(userEntity);
        ResponseEntity<Restaurant> postUserResponse = post(postUserRequest, Restaurant.class);
        assertThat(postUserResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
    }

    @Test
    public void testGetRestaurantRequest() throws IOException {
        HttpPost postUserRequest = new HttpPost(restaurantLambdaUrl);
        List<String> cuisines = new ArrayList<>();
        cuisines.add("Chinese");
        Restaurant defaultRestaurant = new Restaurant(1, "test", "test", cuisines);
        StringEntity userEntity = new StringEntity(mapper.writeValueAsString(defaultRestaurant), ContentType.APPLICATION_JSON);
        postUserRequest.setEntity(userEntity);
        post(postUserRequest, Restaurant.class);
        ResponseEntity<Restaurant> getRestaurantResponse = get(lambdaUrl + "/1", Restaurant.class);
        assertThat(getRestaurantResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
    }
}
