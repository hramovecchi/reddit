package com.hramovecchi.redditapp.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RedditEntries implements Serializable{
    @SerializedName("after")
    @Expose
    private String after;

    @SerializedName("dist")
    @Expose
    private Integer dist;

    @SerializedName("modhash")
    @Expose
    private String modhash;

    @SerializedName("whitelist_status")
    @Expose
    private String whitelistStatus;

    @SerializedName("before")
    @Expose
    private String before;

    @SerializedName("children")
    @Expose
    private List<RedditEntry> entries;

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public Integer getDist() {
        return dist;
    }

    public void setDist(Integer dist) {
        this.dist = dist;
    }

    public String getModhash() {
        return modhash;
    }

    public void setModhash(String modhash) {
        this.modhash = modhash;
    }

    public String getWhitelistStatus() {
        return whitelistStatus;
    }

    public void setWhitelistStatus(String whitelistStatus) {
        this.whitelistStatus = whitelistStatus;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public List<RedditEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<RedditEntry> entries) {
        this.entries = entries;
    }
}
