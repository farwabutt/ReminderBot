package com.example.FinalProject_.domain;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

import java.io.IOException;
import java.util.TimerTask;

public class CronJobEvent extends TimerTask {

    @Override
    public void run() {
System.out.println("Success!");
        Slack slack = Slack.getInstance();
        String webhook_url = "https://hooks.slack.com/services/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";
        String token = System.getenv("xoxb-2407115981314-2438163271216-btTokCheWOy68pfL9ztB5oEq");
        MethodsClient methods = slack.methods(token);
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel("#random") // Use a channel ID `C1234567` is preferrable
                .text(":wave: Hi from a bot written in Java!")
                .build();
        // Get a response as a Java object
        ChatPostMessageResponse response;

        {
            try {
                response = methods.chatPostMessage(request);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SlackApiException e) {
                e.printStackTrace();
            }
        }
    }
}
