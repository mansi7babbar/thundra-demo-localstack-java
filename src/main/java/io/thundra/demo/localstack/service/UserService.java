package io.thundra.demo.localstack.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.thundra.demo.localstack.model.User;
import static io.thundra.demo.localstack.service.ClientBuilder.*;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import java.util.Map;

public class UserService {
 
    public static final String USERS_TABLE_NAME = System.getenv("USERS_TABLE_NAME");

    private final ObjectMapper mapper = new ObjectMapper();

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
        try{
            dynamoDBMapper.save(user);
            return true;
        }catch(Exception e){
            System.out.println("Exception occured in insert user"+e.getMessage())
            return false;

        }
        return userRepository.insertItem(user);
    }

    public User getUser(int id) {
        return dynamoDBMapper.load(User.class, requestId);

    }
}
