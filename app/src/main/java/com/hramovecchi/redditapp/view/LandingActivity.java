package com.hramovecchi.redditapp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.hramovecchi.redditapp.R;
import com.hramovecchi.redditapp.presenter.LandingPresenter;

public class LandingActivity extends AppCompatActivity implements LandingView{

    private ProgressBar progressBar;
    private LandingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        presenter = new LandingPresenter(this);
        presenter.load();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void navigateToRedditEntries() {
        Intent i = new Intent(getApplicationContext(), ItemListActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
