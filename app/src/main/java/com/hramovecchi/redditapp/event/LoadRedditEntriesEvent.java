package com.hramovecchi.redditapp.event;

public class LoadRedditEntriesEvent {

    private int pageNumber;

    public LoadRedditEntriesEvent(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }
}
