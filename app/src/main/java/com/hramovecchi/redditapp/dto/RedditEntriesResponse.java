package com.hramovecchi.redditapp.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RedditEntriesResponse {

    @SerializedName("kind")
    @Expose
    private String kind;

    @SerializedName("data")
    @Expose
    private RedditEntries data;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public RedditEntries getRedditEntries() {
        return data;
    }

    public void setRedditEntries(RedditEntries data) {
        this.data = data;
    }
}
