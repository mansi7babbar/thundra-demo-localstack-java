package org.com.demo.lambda;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.thundra.agent.lambda.localstack.LambdaServer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class LocalStackTest {

    protected String restaurantLambdaUrl;

    @BeforeAll
    static void beforeAll() throws Exception {
        LambdaServer.start();
    }

    @AfterAll
    static void afterAll() throws Exception {
        LambdaServer.stop();
    }

    public static <T> T retrieveResourceFromResponse(HttpResponse response, Class<T> clazz) throws IOException {
        String jsonFromResponse = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonFromResponse, clazz);
    }

    @BeforeEach
    void setup() throws Exception {
        LambdaServer.reset();

        executeCommand("make start-embedded");
        String result = executeCommand("awslocal apigateway get-rest-apis");
        JSONObject object = new JSONObject(result);
        JSONArray array = object.getJSONArray("items");
        String restApiId = array.getJSONObject(0).getString("id");
        restaurantLambdaUrl = "http://localhost:4566/restapis/" + restApiId + "/local/_restaurant_request_/restaurant";
    }

    @AfterEach
    void teardown() throws IOException, InterruptedException {
        executeCommand("docker stop $(docker ps -a -q --filter ancestor=localstack/localstack --format=\"{{.ID}}\")");
    }

    private String executeCommand(String command) throws IOException, InterruptedException {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("cmd.exe", "/c", command);
        } else {
            builder.command("sh", "-c", command);
        }
        Process process = builder.start();
        process.waitFor();

        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        return output.toString();
    }

    protected <R> ResponseEntity<R> get(String path, Class<R> responseType) throws IOException {
        HttpUriRequest request = new HttpGet(path);
        return doRequest(request, responseType);
    }

    protected <R> ResponseEntity<R> post(String path, Class<R> responseType) throws IOException {
        HttpUriRequest request = new HttpPost(path);
        return doRequest(request, responseType);
    }

    protected <R> ResponseEntity<R> post(HttpUriRequest request, Class<R> responseType) throws IOException {
        return doRequest(request, responseType);
    }

    private <R> ResponseEntity<R> doRequest(HttpUriRequest request, Class<R> responseType) throws IOException {
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        R response = retrieveResourceFromResponse(httpResponse, responseType);
        return new ResponseEntity<>(httpResponse.getStatusLine().getStatusCode(), response);
    }

    public class ResponseEntity<R> {

        private int statusCode;
        private R body;

        public ResponseEntity(int statusCode, R body) {
            this.statusCode = statusCode;
            this.body = body;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public R getBody() {
            return body;
        }

        public void setBody(R body) {
            this.body = body;
        }

    }
}
