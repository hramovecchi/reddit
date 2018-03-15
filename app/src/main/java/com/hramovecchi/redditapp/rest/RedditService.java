package com.hramovecchi.redditapp.rest;

import com.hramovecchi.redditapp.dto.RedditEntriesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RedditService {
    @GET("top.json")
    Call<RedditEntriesResponse> getRedditEntries(@Query("limit") int limit);
}
