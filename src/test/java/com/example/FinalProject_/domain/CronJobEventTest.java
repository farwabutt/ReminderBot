package com.example.FinalProject_.domain;

import com.example.FinalProject_.resources.HelloResource;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CronJobEventTest {

    @Test
    void runTest() {
       CronJobEvent cr=new CronJobEvent("{\n" +
               "\"message\": \"Test1\",\n" +
               "\"minutes_from_now\": \"1\",\n" +
               "\"slack_handle\":reminderbot\n" +
               "}");
       cr.run();
    }
}