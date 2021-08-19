package com.example.FinalProject_.domain;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.google.gson.annotations.SerializedName;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Data {
    static AmazonDynamoDB client =  AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-west-2"))
            .build();
    static DynamoDB dynamoDB = new DynamoDB(client);
static String tableName = "record";
    @SerializedName("message")
    private String message;
    @SerializedName("minutes_from_now")
    private int  minutes_from_now;
    @SerializedName("slack_handle")
    private String slack_handle;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMinutes_from_now() {
        return minutes_from_now;
    }

    public void setMinutes_from_now(int minutes_from_now) {
        this.minutes_from_now =minutes_from_now ;
    }
    // System.currentTimeMillis()+ TimeUnit.MINUTES.toMillis(minutes_from_now)

    public String getSlack_handle() {
        return slack_handle;
    }

    public void setSlack_handle(String slack_handle) {
        this.slack_handle = slack_handle;
    }



    public void createTable(){

        try {

            List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
            attributeDefinitions.add(new AttributeDefinition().withAttributeName("Id").withAttributeType("N"));
            List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
            keySchema.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH));

            CreateTableRequest request = new CreateTableRequest().withTableName(tableName).withKeySchema(keySchema)
                    .withAttributeDefinitions(attributeDefinitions).withProvisionedThroughput(
                            new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(6L));
            System.out.println("Issuing CreateTable request for " + tableName);
            Table table = dynamoDB.createTable(request);
            System.out.println("Waiting for " + tableName + " to be created...this may take a while...");
            table.waitForActive();
           insertion();

        }
        catch (Exception e) {
            System.err.println("CreateTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }    }

public void insertion(){
    Table table = dynamoDB.getTable(tableName);
    DateTime t=  new DateTime(getMinutes_from_now());
    String message = getMessage();
    String handle=getSlack_handle();
    String status="";
    final HashMap<String,String> infoMap= new HashMap<String,String>();

    try {
        System.out.println("Adding a new item...");
        PutItemOutcome outcome = table
                .putItem(new Item().withPrimaryKey("message", message, "status",status).withMap("info", infoMap));

        System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());

    }
    catch (Exception e) {
        System.err.println("Unable to add item: " + message + " " + status);
        System.err.println(e.getMessage());
    }
        }

    public static void updateExampleTable() {

        Table table = dynamoDB.getTable(tableName);
        System.out.println("Modifying provisioned throughput for " + tableName);

        try {
            table.updateTable(new ProvisionedThroughput().withReadCapacityUnits(6L).withWriteCapacityUnits(7L));

            table.waitForActive();
        }
        catch (Exception e) {
            System.err.println("UpdateTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }

    public static void deleteExampleTable() {

        Table table = dynamoDB.getTable(tableName);
        try {
            System.out.println("Issuing DeleteTable request for " + tableName);
            table.delete();

            System.out.println("Waiting for " + tableName + " to be deleted...this may take a while...");

            table.waitForDelete();
        }
        catch (Exception e) {
            System.err.println("DeleteTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }
}