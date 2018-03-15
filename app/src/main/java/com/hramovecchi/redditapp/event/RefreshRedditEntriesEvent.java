package com.hramovecchi.redditapp.event;

public class RefreshRedditEntriesEvent {

    private int pagesLoaded;

    public RefreshRedditEntriesEvent(int currentPage) {
        this.pagesLoaded = currentPage;
    }

    public int getPagesLoaded() {
        return pagesLoaded;
    }
}
