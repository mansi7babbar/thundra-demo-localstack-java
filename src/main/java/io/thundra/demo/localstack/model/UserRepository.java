package io.thundra.demo.localstack.model;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;
import java.util.List;

import static io.thundra.demo.localstack.service.ClientBuilder.buildDynamoDB;

public class UserRepository {
    private final DynamoDB dynamoDB;
    private final String tableName;
    private Table table;

    public UserRepository() {
        AmazonDynamoDB dynamoDBClient = buildDynamoDB();
        this.dynamoDB = new DynamoDB(dynamoDBClient);
        this.tableName = "Users_Table";
    }

    public void createTable() {
        try {
            List<KeySchemaElement> keySchema = new ArrayList<>();
            keySchema.add(new KeySchemaElement().withAttributeName("id").withKeyType(KeyType.HASH));

            List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
            attributeDefinitions.add(new AttributeDefinition().withAttributeName("id").withAttributeType("N"));

            CreateTableRequest createTableRequest = new CreateTableRequest()
                    .withTableName(tableName)
                    .withKeySchema(keySchema)
                    .withAttributeDefinitions(attributeDefinitions)
                    .withProvisionedThroughput(new ProvisionedThroughput()
                            .withReadCapacityUnits(5L)
                            .withWriteCapacityUnits(6L));

            this.table = dynamoDB.createTable(createTableRequest);


            table.waitForActive();
        } catch (Exception e) {
            System.err.println("Create table failed.");
            System.err.println(e.getMessage());
        }
    }

    public Boolean insertItem(User user) {
        try {
            Item item = new Item().withPrimaryKey("id", user.getId())
                    .withString("name", user.getName())
                    .withString("email", user.getEmail())
                    .withString("address", user.getAddress());
            table.putItem(item);
            return true;
        } catch (Exception e) {
            System.err.println("Create items failed.");
            System.err.println(e.getMessage());
            return false;
        }
    }

    public Item getItem(int id) {
        try {
            return table.getItem("id", id);
        } catch (Exception e) {
            System.err.println("Get item failed.");
            System.err.println(e.getMessage());
            return null;
        }
    }
}
