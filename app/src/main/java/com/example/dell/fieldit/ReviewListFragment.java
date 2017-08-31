package com.example.dell.fieldit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import com.example.dell.fieldit.Model.Model;
import com.example.dell.fieldit.Model.Review;


public class ReviewListFragment extends Fragment {
    List<Review> reviewsList = new LinkedList<>();
    ListView list;
    ReviewsAdapter adapter;
    String field_id;

    public ReviewListFragment() {
        // Required empty public constructor
    }

    public void setTripsList(List<Review> reviews) {
        reviewsList = reviews;
    }

    public ListView getList() {
        return list;
    }

    public ReviewsAdapter getAdapter() {
        return adapter;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.field_id = getArguments().getString("field_id");
        }
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    public static ReviewListFragment newInstance() {
        ReviewListFragment fragment = new ReviewListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the view from the XML
        View view = inflater.inflate(R.layout.fragment_review_list, container, false);

        // Show progress bar
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.reviewsListProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        list = (ListView) view.findViewById(R.id.reviewsListListView);
        adapter = new ReviewsAdapter();
        list.setAdapter(adapter);

        // Get all trips from databases
        Model.getInstance().getAllUpdatedReviews(this.field_id,new Model.GetReviewsListener() {
            @Override
            public void onResult(List<Review> reviews, List<Review> reviewsToDelete) {

                // Hide progress bar
                progressBar.setVisibility(View.GONE);

                // Set the trips list and refresh the displayed list
                reviewsList = reviews;
                adapter.notifyDataSetChanged();

                if (!Model.checkNetwork()) {
                    Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onCancel() {
                // Hide progress bar
                progressBar.setVisibility(View.GONE);

                // Show relevant message
                Toast.makeText(getActivity(), getString(R.string.reviews_fetch_failed), Toast.LENGTH_LONG);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    class ReviewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return reviewsList.size();
        }

        @Override
        public Object getItem(int i) {
            return reviewsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getViewTypeCount() {

            if (getCount() != 0)
                return getCount();

            return 1;
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            // If there is no view yet
            if (view == null){
                // Inflate the view from the XML
                view = getActivity().getLayoutInflater().inflate(R.layout.fragment_review_list_row, null);
            }

            // Get all the details of the required trip
            final Review review = reviewsList.get(i);

            // Set the trip name in the relevant field in the view
            TextView nameTv = (TextView) view.findViewById(R.id.reviewUserID);
            nameTv.setText(review.getUser_email());
            TextView TextTv = (TextView) view.findViewById(R.id.reviewText);
            TextTv.setText(review.getText());
            RatingBar starRb =(RatingBar) view.findViewById(R.id.starRating);
            starRb.setRating(review.getStars());

            // Inflate the layout for this view
            return view;
        }
    }
}
