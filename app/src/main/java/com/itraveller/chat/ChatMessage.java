package com.itraveller.chat;

/**
 * Created by rohan bundelkhandi on 17/12/2015.
 */
public class ChatMessage {
    public boolean left;
    public String sender_name;
    public String message;
    public String date;
    public String time;


    public ChatMessage(boolean left,String sender_name, String message,String date,String time) {
        super();
        this.left = left;
        this.sender_name=sender_name;
        this.message = message;
        this.date=date;
        this.time=time;
    }
}

