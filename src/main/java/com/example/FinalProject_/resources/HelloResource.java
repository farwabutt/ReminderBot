package com.example.FinalProject_.resources;
import com.example.FinalProject_.domain.CronJobEvent;
import com.example.FinalProject_.domain.Data;
import com.example.FinalProject_.domain.SlackUtils;
import com.google.gson.Gson;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
@Path("/my-reminder-app")
public class HelloResource {
    @POST
    @Path("/remind-me")
    public Response PostJson(String payload) throws Exception {
        final Data d = new Gson().fromJson(payload, Data.class);
        //d.createTable();
       // Thread t = new Thread();
        //t.start();
        Timer timer=new Timer();
        TimerTask task=new CronJobEvent(payload);
        String log4jConfPath = System.getProperty("user.dir")+ File.separator+"log4j.properties";
        if(Validate(d.getMessage(), d.getMinutes_from_now(),d.getSlack_handle())){
             d.insertion();
            timer.schedule(task,d.getMinutes_from_now());
            return Response.ok("You will receive the reminder at "+ d.getMinutes_from_now()).build();
        }
        else
            return Response.ok("Null fields or Incorrect Input!").build();
    }
    public boolean Validate(String m, Date t, String h){
        boolean flag=true;
        if (m=="" || m==null || t==null || h=="" || h==null )
            flag=false;
        return flag;
}
}