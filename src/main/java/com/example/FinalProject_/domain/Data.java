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
           //insertion();

        }
        catch (Exception e) {
            System.err.println("CreateTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }    }

    public void insertion(){
    Table table = dynamoDB.getTable(tableName);
    //DateTime t=  new DateTime(getMinutes_from_now());
        Date t= getMinutes_from_now();
    String message = getMessage();
    String handle=getSlack_handle();
    String status="pending";
    final HashMap<String,Object> infoMap= new HashMap<String,Object>();
   infoMap.put("status", status);
    try {
        System.out.println("Adding a new item...");
        PutItemOutcome outcome = table
                .putItem(new Item().withPrimaryKey("message", message, "time",t).withMap("info", infoMap));

        System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());

    }
    catch (Exception e) {
        System.err.println("Unable to add item: " + message + " and " + t);
        System.err.println(e.getMessage());
    }
        }
        public  void updateTable() {
        Table table = dynamoDB.getTable(tableName);
            Date t= getMinutes_from_now();
            String message = getMessage();
        //System.out.println("Modifying provisioned throughput for " + tableName);
            UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("message", message, "time", t)
                    .withUpdateExpression("set info.status = :s")
                    .withValueMap(new ValueMap().withString(":s", "failed"));
                    //.withReturnValues(ReturnValue.UPDATED_NEW);
        try {
           // table.updateTable(new ProvisionedThroughput().withReadCapacityUnits(6L).withWriteCapacityUnits(7L));
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            //table.waitForActive();
            System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());
        }
        catch (Exception e) {
            System.err.println("UpdateTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }

    public void deleteRecord() {

        Table table = dynamoDB.getTable(tableName);
        Date t= getMinutes_from_now();
        String message = getMessage();
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("message", message, "time", t)).withValueMap(new ValueMap().withString("status", "pending"));
        try {
            System.out.println("Issuing Delete record request for " + tableName);
           // table.delete();
            System.out.println("Waiting for " + tableName + " to be deleted...this may take a while...");
//            table.waitForDelete();
        }
        catch (Exception e) {
            System.err.println("DeleteTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }
}