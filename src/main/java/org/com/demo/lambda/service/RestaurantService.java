package org.com.demo.lambda.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.com.demo.lambda.model.Restaurant;

import static org.com.demo.lambda.service.ClientBuilder.buildDynamoDB;

public class RestaurantService {

    public static final String RESTAURANTS_TABLE_NAME = System.getenv("RESTAURANTS_TABLE_NAME");

    private final AmazonDynamoDB dynamoDB;
    private final DynamoDBMapper dynamoDBMapper;

    public RestaurantService() {
        this.dynamoDB = buildDynamoDB();
        this.dynamoDBMapper = buildDynamoDBMapper(dynamoDB);
    }

    private DynamoDBMapper buildDynamoDBMapper(AmazonDynamoDB dynamoDB) {
        return new DynamoDBMapper(dynamoDB, DynamoDBMapperConfig.builder()
                .withTableNameOverride(
                        DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(RESTAURANTS_TABLE_NAME))
                .build());
    }

    public Boolean insertRestaurant(Restaurant restaurant) {
        try {
            dynamoDBMapper.save(restaurant);
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred while inserting restaurant " + e.getMessage());
            return false;
        }
    }

    public Restaurant getRestaurant(int id) {
        return dynamoDBMapper.load(Restaurant.class, id);
    }
}
