package com.example.FinalProject_.domain;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.*;
import com.google.gson.annotations.SerializedName;
import org.joda.time.DateTime;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Data {
    static AmazonDynamoDB client =  AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-west-2"))
            .build();
    static DynamoDB dynamoDB = new DynamoDB(client);
static String tableName = "project_record";
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

    public Date getMinutes_from_now() {

        Calendar date = Calendar.getInstance();
       // System.out.println("Current Date and TIme : " + date.getTime());
        long timeInSecs = date.getTimeInMillis();
        Date new_time = new Date(timeInSecs + (minutes_from_now * 60 * 1000));
       // System.out.println("After adding mins to current time : " + new_time);
        return new_time;
    }

    public void setMinutes_from_now(int minutes_from_now) {
        this.minutes_from_now =minutes_from_now ;
    }

    public String getSlack_handle() {
        return slack_handle;
    }

    public void setSlack_handle(String slack_handle) {
        this.slack_handle = slack_handle;
    }

    ////-----------------------------DYNAMO ---------------------
    public void createTable(){

        try {
            System.out.println("Attempting to create table; please wait...");
            Table table = dynamoDB.createTable(tableName,
                    Arrays.asList(new KeySchemaElement("year", KeyType.HASH), // Partition
                            new KeySchemaElement("title", KeyType.RANGE)), // Sort key
                    Arrays.asList(new AttributeDefinition("year", ScalarAttributeType.N),
                            new AttributeDefinition("title", ScalarAttributeType.S)),
                    new ProvisionedThroughput(10L, 10L));
            table.waitForActive();
            System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

           //insertion();

        }
        catch (Exception e) {
            System.err.println("CreateTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }    }

    public void insertion(){
    Table table = dynamoDB.getTable(tableName);
        int year = 2021;
        String title = "Reminder Bot Record";

        final Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put("message", getMessage());
        infoMap.put("stat","pending");
        infoMap.put("timer",getMinutes_from_now().toString());

        try {
            System.out.println("Adding a new item...");
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("year", year, "title", title).withMap("info", infoMap));

            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());

        }
        catch (Exception e) {
            System.err.println("Unable to add item: " + year + " " + title);
            System.err.println(e.getMessage());
        }

        }

        public  void updateRecord() {
        Table table = dynamoDB.getTable(tableName);
            int year = 2021;
            String title = "Reminder Bot Record";

            UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("year", year, "title", title)
                    .withUpdateExpression("set info.stat=:s")
                    .withValueMap(new ValueMap().withString(":s", "failed")).withReturnValues(ReturnValue.UPDATED_NEW);
            try {
                System.out.println("Updating the item...");
                UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
                System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());

            }
            catch (Exception e) {
                System.err.println("Unable to update item: " + year + " " + title);
                System.err.println(e.getMessage());
            }
    }

    public void deleteRecord() {

        Table table = dynamoDB.getTable(tableName);
        int year = 2021;
        String title = "Reminder Bot Record";

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
               .withPrimaryKey(new PrimaryKey("year", year, "title", title)).withConditionExpression("info.stat <= :s").withValueMap(new ValueMap().withString(":s", "pending"));


        try {
            System.out.println("Attempting a conditional delete...");
            table.deleteItem(deleteItemSpec);
            System.out.println("DeleteItem succeeded");
        }
        catch (Exception e) {
            System.err.println("Unable to delete item: " + year + " " + title);
            System.err.println(e.getMessage());
        }
    }
}