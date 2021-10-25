package io.thundra.demo.localstack.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.thundra.demo.localstack.model.User;
import io.thundra.demo.localstack.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

public class UserApp implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger logger = LogManager.getLogger(UserApp.class);
    private static final Map<String, String> headers = new HashMap<String, String>() {{
        put("content-type", "application/json");
    }};
    private final ObjectMapper mapper = new ObjectMapper();
    private final UserService userService = new UserService();
    JSONParser parser = new JSONParser();
    Gson gson = new Gson();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            System.out.println("MB handleRequest " + request);
            logger.info("Request --> " + request);
            if ("/user".equals(request.getPath()) && "POST".equals(request.getHttpMethod())) {
                JSONObject requestBody = (JSONObject) parser.parse(request.getBody());
                User user = gson.fromJson(requestBody.toString(), User.class);
                Boolean insertUserResponse = userService.insertUser(user);
                if (insertUserResponse) {
                    return new APIGatewayProxyResponseEvent().withStatusCode(200)
                            .withHeaders(headers)
                            .withBody(mapper.writeValueAsString(user));
                } else {
                    return new APIGatewayProxyResponseEvent().withStatusCode(400)
                            .withHeaders(headers)
                            .withBody(mapper.writeValueAsString(user));
                }
            } else if ("/user".equals(request.getPath()) && "GET".equals(request.getHttpMethod())) {
                System.out.println("MB handleRequest GET");
                Map<String, String> pathParameters = request.getPathParameters();
                int userID = Integer.parseInt(pathParameters.get("userid"));
                User getUserResponse = userService.getUser(userID);
                User defaultUser = new User(1, "Mansi", "abc", "xyz");
                System.out.println("MB getUserResponse " + getUserResponse);
                if (getUserResponse != null) {
                    System.out.println("MB getUserResponse not null");
                    return new APIGatewayProxyResponseEvent().withStatusCode(200)
                            .withHeaders(headers)
                            .withBody(mapper.writeValueAsString(getUserResponse));
                } else {
                    System.out.println("MB getUserResponse null");
                    return new APIGatewayProxyResponseEvent().withStatusCode(200)
                            .withHeaders(headers)
                            .withBody(mapper.writeValueAsString(defaultUser));
                }
            } else {
                return new APIGatewayProxyResponseEvent().
                        withStatusCode(400);
            }
        } catch (ParseException | JsonProcessingException e) {
            logger.error("Error occurred handling message. Exception is ", e);
            return new APIGatewayProxyResponseEvent().
                    withStatusCode(500).
                    withBody(e.getMessage());
        }
    }
}
