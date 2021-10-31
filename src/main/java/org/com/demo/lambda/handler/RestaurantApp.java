package org.com.demo.lambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.com.demo.lambda.model.Restaurant;
import org.com.demo.lambda.service.RestaurantService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RestaurantApp implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger logger = LogManager.getLogger(RestaurantApp.class);
    private static final Map<String, String> headers = new HashMap<String, String>() {{
        put("content-type", "application/json");
    }};
    private final RestaurantService restaurantService = new RestaurantService();
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            logger.info("Request --> " + request);
            if ("/restaurant".equals(request.getPath()) && "POST".equals(request.getHttpMethod())) {
                Restaurant restaurant = mapper.readValue(request.getBody(), Restaurant.class);
                Boolean insertRestaurantResponse = restaurantService.insertRestaurant(restaurant);
                if (insertRestaurantResponse) {
                    return new APIGatewayProxyResponseEvent()
                            .withStatusCode(200)
                            .withHeaders(headers)
                            .withBody(mapper.writeValueAsString(restaurant));
                } else {
                    return new APIGatewayProxyResponseEvent()
                            .withStatusCode(400)
                            .withHeaders(headers)
                            .withBody(mapper.writeValueAsString(restaurant));
                }
            } else if (Pattern.matches("/restaurant/\\d*", request.getPath()) && "GET".equals(request.getHttpMethod())) {
                Map<String, String> pathParameters = request.getPathParameters();
                int restaurantId = Integer.parseInt(pathParameters.get("restaurantid"));
                Restaurant getRestaurantResponse = restaurantService.getRestaurant(restaurantId);
                Restaurant defaultRestaurant = new Restaurant(1, "test", "test_address", new ArrayList<>());
                if (getRestaurantResponse != null) {
                    return new APIGatewayProxyResponseEvent()
                            .withStatusCode(200)
                            .withHeaders(headers)
                            .withBody(mapper.writeValueAsString(getRestaurantResponse));
                } else {
                    return new APIGatewayProxyResponseEvent()
                            .withStatusCode(400)
                            .withHeaders(headers)
                            .withBody(mapper.writeValueAsString(defaultRestaurant));
                }
            } else {
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(404);
            }
        } catch (Exception exception) {
            logger.error("Error occurred while handling message. Exception is ", exception);
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody(exception.getMessage());
        }
    }
}
