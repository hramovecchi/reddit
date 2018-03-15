package com.hramovecchi.redditapp.model;

import com.hramovecchi.redditapp.app.Constants;
import com.hramovecchi.redditapp.app.RedditApp;
import com.hramovecchi.redditapp.dto.RedditEntriesResponse;
import com.hramovecchi.redditapp.dto.RedditEntry;
import com.hramovecchi.redditapp.event.AppErrorEvent;
import com.hramovecchi.redditapp.event.FetchRedditPostsEvent;
import com.hramovecchi.redditapp.event.LoadRedditEntriesEvent;
import com.hramovecchi.redditapp.event.RedditEntriesLoaded;
import com.hramovecchi.redditapp.event.RedditPostsFetched;
import com.hramovecchi.redditapp.event.RefreshRedditEntriesEvent;
import com.hramovecchi.redditapp.rest.RedditService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hramovecchi.redditapp.app.Constants.PAGE_SIZE;

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

    @Subscribe
    public void onLoadRedditEntriesEvent(LoadRedditEntriesEvent event) {
        bus.post(new RedditEntriesLoaded(getPaginationResult(event.getPageNumber())));
    }

    @Subscribe
    public void onRefreshRedditEntriesEvent(final RefreshRedditEntriesEvent event) {
        Call<RedditEntriesResponse> call = service.getRedditEntries(Constants.REQUEST_LIMIT);
        call.enqueue(new Callback<RedditEntriesResponse>() {
            @Override
            public void onResponse(Call<RedditEntriesResponse> call, Response<RedditEntriesResponse> response) {
                entries = response.body().getRedditEntries().getEntries();
                List<RedditEntry> result = new ArrayList<RedditEntry>(((event.getPagesLoaded()+1) * 10));
                for (int i = 0; i <= event.getPagesLoaded() ; i++) {
                    result.addAll(getPaginationResult(i));
                }

                bus.post(new RedditEntriesLoaded(result));
            }

            @Override
            public void onFailure(Call<RedditEntriesResponse> call, Throwable t) {
                bus.post(new AppErrorEvent("API call fail"));
            }
        });
    }

    private List<RedditEntry> getPaginationResult(int pageNumber) {
        int startIndex = pageNumber * PAGE_SIZE;

        List<RedditEntry> resultEntry = new ArrayList<RedditEntry>(PAGE_SIZE);
        for (int i = startIndex; i < startIndex + PAGE_SIZE; i++) {
            resultEntry.add(entries.get(i));
        }

        return resultEntry;
    }
}
