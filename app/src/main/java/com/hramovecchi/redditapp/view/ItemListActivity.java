package com.hramovecchi.redditapp.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.hramovecchi.redditapp.R;
import com.hramovecchi.redditapp.dto.RedditEntries;
import com.hramovecchi.redditapp.dto.RedditEntry;
import com.hramovecchi.redditapp.dummy.DummyContent;
import com.hramovecchi.redditapp.presenter.ItemListPresenter;

import java.util.List;

public class ItemListActivity extends AppCompatActivity implements ItemListView{

    private boolean mTwoPane;
    private ItemListPresenter presenter;
    private RecyclerView recyclerView;
    private SimpleItemRecyclerViewAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton removeAllBtn = (ImageButton)findViewById(R.id.delete_all_button);
        removeAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.clearPosts();
            }
        });

        ImageButton refreshAllBtn = (ImageButton)findViewById(R.id.refresh_button);
        refreshAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.refresh();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }

        RedditEntries savedRedditEntries = (savedInstanceState != null) ?
                (RedditEntries)savedInstanceState.getSerializable("REDDIT_SAVED_POSTS"):
                null;

        recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;

        presenter = new ItemListPresenter(this);
        if (savedRedditEntries == null) {
            presenter.load();
        } else {
            presenter.setCurrentPage(savedInstanceState.getInt("CURRENT_PAGE_NUMBER"));
            fillView(savedRedditEntries.getEntries());
        }
    }

    @Override
    public void fillView(List<RedditEntry> redditEntryList) {
        if (adapter == null) {
            adapter = new SimpleItemRecyclerViewAdapter(this, redditEntryList, mTwoPane);
            recyclerView.setAdapter(adapter);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    // Bottom of the scroll
                    if (!recyclerView.canScrollVertically(1)) {
                        presenter.loadNextPage();
                    }
                }
            });
        } else {
            int itemCount = adapter.getItemCount();
            adapter.getItems().addAll(redditEntryList);
            adapter.notifyItemRangeInserted(itemCount, redditEntryList.size());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        RedditEntries entries= new RedditEntries();
        entries.setEntries(adapter.getItems());

        outState.putSerializable("REDDIT_SAVED_POSTS", entries);
        outState.putInt("CURRENT_PAGE_NUMBER", presenter.getCurrentPage());
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void clearPosts() {
        if (adapter != null) {
            adapter.getItems().clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<RedditEntry> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedditEntry item = (RedditEntry) view.getTag();

                item.getData().setVisited(true);
                view.setBackgroundColor(Color.LTGRAY);

                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putSerializable(ItemDetailFragment.ARG_ITEM_ID, item.getData());
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.getData());

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<RedditEntry> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.title.setText(mValues.get(holder.getAdapterPosition()).getData().getTitle());
            holder.author.setText(mValues.get(holder.getAdapterPosition()).getData().getAuthor());

            String thumbnail = mValues.get(holder.getAdapterPosition()).getData().getThumbnail();
            if (thumbnail != null && !thumbnail.isEmpty() && thumbnail.startsWith("http")) {
                Glide.with(getApplicationContext()).load(thumbnail).into(holder.thumbnail);
            } else {
                holder.thumbnail.setImageDrawable(getApplicationContext().getDrawable(R.mipmap.ic_launcher));
            }

            holder.itemView.setTag(mValues.get(holder.getAdapterPosition()));

            if (mValues.get(holder.getAdapterPosition()).getData().getVisited()){
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
            holder.itemView.setOnClickListener(mOnClickListener);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = mValues.indexOf(holder.itemView.getTag());
                    mValues.remove(position);
                    adapter.notifyItemRemoved(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public List<RedditEntry> getItems() {
            return mValues;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView thumbnail;
            final TextView title;
            final TextView author;
            final ImageButton deleteButton;

            ViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                author = (TextView) view.findViewById(R.id.author);
                thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
                deleteButton = (ImageButton) view.findViewById(R.id.delete_button);
            }
        }
    }
}
