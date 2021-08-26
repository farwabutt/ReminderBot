package com.example.FinalProject_.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.Message;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class SlackUtils {
    Slack slack = Slack.getInstance();
  public void Slack_postmessage (String payload){
      final Data d = new Gson().fromJson(payload, Data.class);
        String token = System.getenv("SLACK_TOKEN");
        try{
            ChatPostMessageResponse response = slack.methods(token).chatPostMessage(ChatPostMessageRequest.builder()
                    .channel(d.getSlack_handle())
                    .text(d.getMessage())
                    .build());
            if (response.isOk()) {
                Message postedMessage = response.getMessage();
                System.out.println(postedMessage.toString());
                d.deleteRecord();
            } else {
                String errorCode = response.getError(); // e.g., "invalid_auth", "channel_not_found"
                System.out.println(errorCode);
                d.updateRecord();
            }
        } catch (SlackApiException | IOException  requestFailure  ) {
            System.out.println(requestFailure.getMessage());
            d.updateRecord();
        }
}
}
