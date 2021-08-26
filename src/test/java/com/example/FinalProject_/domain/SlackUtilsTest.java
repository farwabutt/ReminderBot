package com.example.FinalProject_.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlackUtilsTest {

    @Test
    void slack_postmessage() {
        SlackUtils su = new SlackUtils();
        su.Slack_postmessage("{\n" +
                "\"message\": \"hello\",\n" +
                "\"minutes_from_now\": \"1\",\n" +
                "\"slack_handle\":reminderbot\n" +
                "}");
    }
}