package com.hramovecchi.redditapp.event;

import com.hramovecchi.redditapp.dto.RedditEntry;

import java.util.List;

public class RedditEntriesLoaded {
    private List<RedditEntry> entries;

    public RedditEntriesLoaded(List<RedditEntry> entries) {
        this.entries = entries;
    }

    public List<RedditEntry> getEntries() {
        return entries;
    }
}
