package com.hramovecchi.redditapp.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hramovecchi.redditapp.R;
import com.hramovecchi.redditapp.app.Constants;
import com.hramovecchi.redditapp.dto.RedditEntryData;
import com.hramovecchi.redditapp.dummy.DummyContent;
import com.hramovecchi.redditapp.utils.DateUtil;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";

    private RedditEntryData mItem;
    private boolean hasSavePermit = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    ImageView thumbnailContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = (RedditEntryData)getArguments().getSerializable(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.title_input)).setText(mItem.getTitle());
            ((TextView) rootView.findViewById(R.id.author_input)).setText(mItem.getAuthor());
            ((TextView) rootView.findViewById(R.id.comments_input)).setText(String.valueOf(mItem.getNumComments()));
            ((TextView) rootView.findViewById(R.id.time_input)).setText(DateUtil.toHoursAgoFormat(mItem.getCreated().longValue()));
            thumbnailContainer = rootView.findViewById(R.id.thumbnail);
            thumbnailContainer.setDrawingCacheEnabled(true);
            if (mItem.getThumbnail()!= null && !mItem.getThumbnail().isEmpty() && mItem.getThumbnail().startsWith("http")){
                Glide.with(this).load(mItem.getThumbnail()).into(thumbnailContainer);
                thumbnailContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkPermissionsAndSave(thumbnailContainer);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mItem.getThumbnail()));
                        startActivity(browserIntent);
                    }
                });
            } else {
                thumbnailContainer.setImageDrawable(getContext().getDrawable(R.mipmap.ic_launcher));
            }
        }

        return rootView;
    }

    private void checkPermissionsAndSave(ImageView thumbnailContainer){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.WRITE_EXTERNAL_STORAGE);
        } else {
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                    thumbnailContainer.getDrawingCache(true), mItem.getId() , mItem.getTitle());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case Constants.WRITE_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                            thumbnailContainer.getDrawingCache(true), mItem.getId() , mItem.getTitle());
                }
                break;

            default:
                break;
        }
    }
}
