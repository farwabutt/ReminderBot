package com.example.FinalProject_.resources;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.example.FinalProject_.domain.CronJobEvent;
import com.example.FinalProject_.domain.Data;
import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Path("/my-reminder-app")
public class HelloResource {
   /* @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }*/


    @POST
    @Path("/remind-me")
    public Response PostJson(String payload) throws Exception {
        final Data d = new Gson().fromJson(payload, Data.class);
        Timer timer= new Timer();
        TimerTask task=new CronJobEvent();
        //d.createTable();
        if(Validate(d.getMessage(), d.getMinutes_from_now(),d.getSlack_handle())){
             //d.insertion();
             timer.schedule(task,d.getMinutes_from_now());
            return Response.ok("You will receive the reminder at "+ d.getMinutes_from_now()).build();
        }
        else
            return Response.ok("Null fields or Incorrect Input!").build();
        // System.out.println(d.toString());
    }
    public boolean Validate(String m, Date t, String h){
        boolean flag=true;
        if (m=="" || m==null || t==null || h=="" || h==null )
            flag=false;
     ////  else if(m.getClass().getSimpleName()!="String" || ((Object)t).getClass().getSimpleName()!="Integer" || h.getClass().getSimpleName()!="String")
          // flag=false;
        return flag;

}


}