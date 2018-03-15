package com.hramovecchi.redditapp.app;

import android.app.Application;
import android.widget.Toast;

import com.hramovecchi.redditapp.event.AppErrorEvent;
import com.hramovecchi.redditapp.model.RedditPostsModel;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

public class RedditApp extends Application{
    private static final String BASE_URL = "https://www.reddit.com/";

    private static RedditApp app;
    private RedditAppComponent redditAppComponent;
    private RedditPostsModel injector;

    @Inject
    Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        redditAppComponent = DaggerRedditAppComponent.builder()
                .redditAppModule(new RedditAppModule(BASE_URL))
                .build();

        redditAppComponent.inject(this);

        injector = new RedditPostsModel();
        bus.register(this);
    }

    public static RedditApp getApp() {
        return app;
    }

    public RedditAppComponent getRedditAppComponent() {
        return redditAppComponent;
    }

    @Subscribe
    public void onAppError(AppErrorEvent appErrorEvent){
        Toast.makeText(this, appErrorEvent.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
