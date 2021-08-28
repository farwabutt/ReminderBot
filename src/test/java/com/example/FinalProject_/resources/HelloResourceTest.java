package com.example.FinalProject_.resources;

import com.amazonaws.Response;
import com.amazonaws.ResponseMetadata;
import org.junit.Assert;
import org.junit.jupiter.api.Test;


import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class HelloResourceTest {

    @Test
    void postJson() throws Exception {
       /* try {
          Response resp= new HelloResource().PostJson("{\n" +
                    "\"message\": \"Test1\",\n" +
                    "\"minutes_from_now\": \"0\",\n" +
                    "\"slack_handle\":farwa\n" +
                    "}");
            assertEquals(200,resp.toString());

    }

        catch(Exception e){
            System.out.println(e.getMessage());
        }*/
    }

    @Test
    void validate() {
        Date d=new Date();
        boolean v=new HelloResource().Validate("0", d ,"farwa");
        Assert.assertEquals(false,v);
    }
}