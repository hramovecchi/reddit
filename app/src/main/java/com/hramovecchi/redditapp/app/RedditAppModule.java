package com.hramovecchi.redditapp.app;

import com.hramovecchi.redditapp.rest.RedditService;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RedditAppModule {

    private String baseURL;

    public RedditAppModule(String baseURL) {
        this.baseURL = baseURL;
    }

    @Singleton
    @Provides
    Bus provideEventBus() {
        return new Bus(ThreadEnforcer.ANY);
    }

    @Singleton
    @Provides
    RedditService provideRedditService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RedditService.class);
    }
}
