package com.example.FinalProject_.domain;

import com.google.gson.Gson;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.Message;

import java.io.IOException;
import java.util.Date;
import java.util.TimerTask;

public class CronJobEvent extends TimerTask {
String payload;
    public CronJobEvent(String p){
        payload=p;
    }
    @Override
    public void run() {
        SlackUtils s= new SlackUtils();
        s.Slack_postmessage(payload);
        System.out.println("Success!");

    }
}
