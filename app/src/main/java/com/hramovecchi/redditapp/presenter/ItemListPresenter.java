package com.hramovecchi.redditapp.presenter;

import com.hramovecchi.redditapp.app.RedditApp;
import com.hramovecchi.redditapp.event.LoadRedditEntriesEvent;
import com.hramovecchi.redditapp.event.RedditEntriesLoaded;
import com.hramovecchi.redditapp.event.RefreshRedditEntriesEvent;
import com.hramovecchi.redditapp.view.ItemListView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

public class ItemListPresenter {
    private ItemListView view;
    private int currentPage;
    private boolean refreshingInProgress;

    @Inject
    Bus bus;

    public ItemListPresenter(ItemListView view) {
        RedditApp.getApp().getRedditAppComponent().inject(this);
        this.view = view;
        currentPage = 0;
        bus.register(this);
    }

    public void load() {
        bus.post(new LoadRedditEntriesEvent(currentPage));
    }

    public void refresh(){
        if (!refreshingInProgress) {
            refreshingInProgress = true;
            view.showProgressDialog();
            view.clearPosts();
            bus.post(new RefreshRedditEntriesEvent(currentPage));
        }
    }

    public void onDestroy() {
        this.view = null;
        bus.unregister(this);
    }

    public void clearPosts() {
        this.view.clearPosts();
    }

    @Subscribe
    public void onRedditEntriesLoaded(RedditEntriesLoaded event) {
        refreshingInProgress = false;
        view.fillView(event.getEntries());
        view.hideProgressDialog();
    }

    public void loadNextPage() {
        if (!refreshingInProgress && currentPage < 4){
            refreshingInProgress = true;
            currentPage++;
            bus.post(new LoadRedditEntriesEvent(currentPage));
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
