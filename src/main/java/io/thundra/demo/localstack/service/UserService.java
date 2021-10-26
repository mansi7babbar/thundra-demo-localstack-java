package io.thundra.demo.localstack.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import io.thundra.demo.localstack.model.User;

import static io.thundra.demo.localstack.service.ClientBuilder.buildDynamoDB;

public class UserService {

    public static final String USERS_TABLE_NAME = System.getenv("USERS_TABLE_NAME");

    private final AmazonDynamoDB dynamoDB;
    private final DynamoDBMapper dynamoDBMapper;

    public UserService() {
        this.dynamoDB = buildDynamoDB();
        this.dynamoDBMapper = buildDynamoDBMapper(dynamoDB);
    }

    private DynamoDBMapper buildDynamoDBMapper(AmazonDynamoDB dynamoDB) {
        return new DynamoDBMapper(dynamoDB, DynamoDBMapperConfig.builder()
                .withTableNameOverride(
                        DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(USERS_TABLE_NAME))
                .build());
    }

    public Boolean insertUser(User user) {
        try {
            dynamoDBMapper.save(user);
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred while inserting user " + e.getMessage());
            return false;
        }
    }

    public User getUser(int id) {
        return dynamoDBMapper.load(User.class, id);
    }
}
