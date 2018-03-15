package com.hramovecchi.redditapp.event;

public class AppErrorEvent {
    private String message;

    public AppErrorEvent(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}