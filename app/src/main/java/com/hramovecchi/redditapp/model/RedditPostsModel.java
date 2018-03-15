package com.hramovecchi.redditapp.model;

import com.hramovecchi.redditapp.app.Constants;
import com.hramovecchi.redditapp.app.RedditApp;
import com.hramovecchi.redditapp.dto.RedditEntriesResponse;
import com.hramovecchi.redditapp.dto.RedditEntry;
import com.hramovecchi.redditapp.event.AppErrorEvent;
import com.hramovecchi.redditapp.event.FetchRedditPostsEvent;
import com.hramovecchi.redditapp.event.RedditPostsFetched;
import com.hramovecchi.redditapp.rest.RedditService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedditPostsModel {
    @Inject
    Bus bus;

    @Inject
    RedditService service;

    private List<RedditEntry> entries;

    public RedditPostsModel () {
        RedditApp.getApp().getRedditAppComponent().inject(this);
        this.bus.register(this);
    }

    @Subscribe
    public void onFetchRedditPostsEvent(FetchRedditPostsEvent event) {
        Call<RedditEntriesResponse> call = service.getRedditEntries(Constants.REQUEST_LIMIT);
        call.enqueue(new Callback<RedditEntriesResponse>() {
            @Override
            public void onResponse(Call<RedditEntriesResponse> call, Response<RedditEntriesResponse> response) {
                entries = response.body().getRedditEntries().getEntries();
                bus.post(new RedditPostsFetched());
            }

            @Override
            public void onFailure(Call<RedditEntriesResponse> call, Throwable t) {
                bus.post(new AppErrorEvent("API call fail: " + t.getMessage()));
            }
        });
    }
}
