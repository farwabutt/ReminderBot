package com.example.FinalProject_.domain;

import com.google.gson.annotations.SerializedName;

import java.util.concurrent.TimeUnit;

public class Data {
    @SerializedName("message")
    private String message;
    @SerializedName("minutes_from_now")
    private int  minutes_from_now;
    @SerializedName("slack_handle")
    private String slack_handle;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMinutes_from_now() {
        return minutes_from_now;
    }

    public void setMinutes_from_now(int minutes_from_now) {
        this.minutes_from_now =minutes_from_now ;
    }
    // System.currentTimeMillis()+ TimeUnit.MINUTES.toMillis(minutes_from_now)

    public String getSlack_handle() {
        return slack_handle;
    }

    public void setSlack_handle(String slack_handle) {
        this.slack_handle = slack_handle;
    }


}