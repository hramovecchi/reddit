package com.hramovecchi.redditapp.view;

import com.hramovecchi.redditapp.dto.RedditEntry;

import java.util.List;

public interface ItemListView {
    void fillView(List<RedditEntry> redditEntryList);
    void clearPosts();
    void showProgressDialog();
    void hideProgressDialog();
}
