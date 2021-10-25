package io.thundra.demo.localstack.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
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
    private final UserService userService = new UserService();
    JSONParser parser = new JSONParser();
    Gson gson = new Gson();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            logger.info("Request --> " + request);
            if ("/user".equals(request.getPath()) && "POST".equals(request.getHttpMethod())) {
                JSONObject requestBody = (JSONObject) parser.parse(request.getBody());
                User user = gson.fromJson(requestBody.toString(), User.class);
                Boolean insertUserResponse = userService.insertUser(user);
                if (insertUserResponse) {
                    return new APIGatewayProxyResponseEvent().withStatusCode(200)
                            .withHeaders(headers)
                            .withBody("Successfully inserted User");
                } else {
                    return new APIGatewayProxyResponseEvent().withStatusCode(400)
                            .withHeaders(headers)
                            .withBody("Failed to insert User");
                }
            } else if ("/user".equals(request.getPath()) && "GET".equals(request.getHttpMethod())) {
                Map<String, String> pathParameters = request.getPathParameters();
                int userID = Integer.parseInt(pathParameters.get("userid"));
                User getUserResponse = userService.getUser(userID);
                User defaultUser = new User();
                if (getUserResponse != null) {
                    return new APIGatewayProxyResponseEvent().withStatusCode(200)
                            .withHeaders(headers)
                            .withBody(getUserResponse.toString());
                } else {
                    return new APIGatewayProxyResponseEvent().withStatusCode(200)
                            .withHeaders(headers)
                            .withBody(defaultUser.toString());
                }
            } else {
                return new APIGatewayProxyResponseEvent().
                        withStatusCode(400);
            }
        } catch (ParseException e) {
            logger.error("Error occurred handling message. Exception is ", e);
            return new APIGatewayProxyResponseEvent().
                    withStatusCode(500).
                    withBody(e.getMessage());
        }
    }
}
