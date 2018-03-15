package com.hramovecchi.redditapp.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RedditEntry implements Serializable{
    @SerializedName("kind")
    @Expose
    private String kind;

    @SerializedName("data")
    @Expose
    private RedditEntryData data;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public RedditEntryData getData() {
        return data;
    }

    public void setData(RedditEntryData data) {
        this.data = data;
    }
}
