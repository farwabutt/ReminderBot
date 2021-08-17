package com.example.FinalProject_.resources;

import com.example.FinalProject_.domain.Data;
import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

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
        if(Validate(d.getMessage(), d.getMinutes_from_now(),d.getSlack_handle())){
            return Response.noContent().build();
        }
        else
            return Response.ok("Null fields or Incorrect Input!").build();
        // System.out.println(d.toString());
    }
    public boolean Validate(String m, int t,String h){
        boolean flag=true;
        if (m==null || t==0 || h==null )
            flag=false;
        else if(m.getClass().getSimpleName()!="String" || ((Object)t).getClass().getSimpleName()!="Integer" || h.getClass().getSimpleName()!="String")
            flag=false;
        return flag;
    }
}