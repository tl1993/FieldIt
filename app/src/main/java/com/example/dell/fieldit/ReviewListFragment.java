package com.example.dell.fieldit;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.FragmentTransaction;

import java.util.LinkedList;
import java.util.List;
import com.example.dell.fieldit.Model.Model;
import com.example.dell.fieldit.Model.Review;


public class ReviewListFragment extends Fragment implements ReviewUpdateListener {
    List<Review> reviewsList = new LinkedList<>();
    ListView list;
    ReviewsAdapter adapter;
    TextView textView;
    String field_id;

    public ReviewListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onReviewChange() {
        // Get all reviews from databases
        Model.getInstance().getAllUpdatedReviews(this.field_id,new Model.GetReviewsListener() {
            @Override
            public void onResult(List<Review> reviews, List<Review> reviewsToDelete) {


                // Set the reviews list and refresh the displayed list
                reviewsList = reviews;
                adapter.notifyDataSetChanged();

                if (!Model.checkNetwork()) {
                    Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancel() {
                // Show relevant message
                Toast.makeText(getActivity(), getString(R.string.reviews_fetch_failed), Toast.LENGTH_LONG).show();
            }
        });
    }

    public ListView getList() {
        return list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.field_id = getArguments().getString("field_id");
        }
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        Model.getInstance().setReviewUpdateListener(this);
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

        textView = (TextView) view.findViewById(R.id.emptyListTextView);
        list = (ListView) view.findViewById(R.id.reviewsListListView);
        adapter = new ReviewsAdapter();
        list.setAdapter(adapter);

        // Get all reviews from databases
        Model.getInstance().getAllUpdatedReviews(this.field_id,new Model.GetReviewsListener() {
            @Override
            public void onResult(List<Review> reviews, List<Review> reviewsToDelete) {

                // Hide progress bar
                progressBar.setVisibility(View.GONE);

                // Set the reviews list and refresh the displayed list
                reviewsList = reviews;
                adapter.notifyDataSetChanged();
                if (reviewsList.isEmpty()) {
                    list.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);

                } else {
                    // Set the reviews list and refresh the displayed list
                    reviewsList = reviews;
                    adapter.notifyDataSetChanged();
                    list.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                }

                if (!Model.checkNetwork()) {
                    Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancel() {
                // Hide progress bar
                progressBar.setVisibility(View.GONE);

                // Show relevant message
                Toast.makeText(getActivity(), getString(R.string.reviews_fetch_failed), Toast.LENGTH_LONG).show();
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

            // Get all the details of the required review
            final Review review = reviewsList.get(i);

            // Set the review name in the relevant field in the view
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

    @Override
    public void onStop()
    {
        super.onStop();
        Model.getInstance().setReviewUpdateListener(null);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
