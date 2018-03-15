package com.hramovecchi.redditapp.presenter;

import com.hramovecchi.redditapp.app.RedditApp;
import com.hramovecchi.redditapp.event.FetchRedditPostsEvent;
import com.hramovecchi.redditapp.event.RedditPostsFetched;
import com.hramovecchi.redditapp.view.LandingView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

public class LandingPresenter {
    @Inject
    Bus bus;

    private LandingView view;

    public LandingPresenter(LandingView view) {
        RedditApp.getApp().getRedditAppComponent().inject(this);
        this.view = view;
        this.bus.register(this);
    }

    public void load(){
        view.showProgress();
        bus.post(new FetchRedditPostsEvent());
    }

    public void onDestroy(){
        bus.unregister(this);
        view = null;
    }

    @Subscribe
    public void onRedditPostsFetched(RedditPostsFetched event) {
        view.hideProgress();
        view.navigateToRedditEntries();
    }
}
