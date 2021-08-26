package com.example.FinalProject_.domain;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DataTest {
    String payload="{\n" +
            "\"message\": \"hello\",\n" +
            "\"minutes_from_now\": \"1\",\n" +
            "\"slack_handle\":reminderbot\n" +
            "}";
    final Data d = new Gson().fromJson(payload, Data.class);

    @Test
    void getMessage() {

      String s=d.getMessage();
      assertEquals("Hello",s);
    }

    @Test
    void setMessage() {
        d.setMessage("test");
    }

    @Test
    void getMinutes_from_now() {
        Date d1=d.getMinutes_from_now();
        assertEquals("Thu Aug 26 21:00:04 PKT 2021",d1);
    }

    @Test
    void setMinutes_from_now() {
        d.setMinutes_from_now(10);
    }

    @Test
    void getSlack_handle() {
        String s=d.getMessage();
        assertEquals("Hello",s);
    }

    @Test
    void setSlack_handle() {
        d.setSlack_handle("hello");
    }

    @Test
    void createTable() {
        d.createTable();
    }

    @Test
    void insertion() {
        d.insertion();
    }

    @Test
    void updateRecord() {
        d.updateRecord();
    }

    @Test
    void deleteRecord() {
d.deleteRecord();
    }
}