package com.hramovecchi.redditapp.app;

import com.hramovecchi.redditapp.model.RedditPostsModel;
import com.hramovecchi.redditapp.presenter.ItemListPresenter;
import com.hramovecchi.redditapp.presenter.LandingPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RedditAppModule.class})
public interface RedditAppComponent {
    void inject(RedditPostsModel injector);
    void inject(LandingPresenter presenter);
    void inject(RedditApp app);
    void inject(ItemListPresenter presenter);
}
