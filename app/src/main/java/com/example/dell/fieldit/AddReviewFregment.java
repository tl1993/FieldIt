package com.example.dell.fieldit;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.dell.fieldit.Model.Model;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link AddReviewFregment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddReviewFregment extends Fragment {

    EditText editText;
    RatingBar ratingBar;
    String field_id;
    ProgressBar progressBar;

    public AddReviewFregment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddReviewFregment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddReviewFregment newInstance() {
        AddReviewFregment fragment = new AddReviewFregment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.field_id = getArguments().getString("field_id");
        }
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_add_review, container, false);
        editText = (EditText) contentView.findViewById(R.id.new_review_text);
        ratingBar = (RatingBar) contentView.findViewById(R.id.new_review_rating);
        progressBar = (ProgressBar) contentView.findViewById(R.id.review_progressBar);
        Button saveBtn = (Button) contentView.findViewById(R.id.new_review_save_button);
        Button cancelBtn = (Button) contentView.findViewById(R.id.new_review_cancel_button);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                float rating = ratingBar.getRating();
               progressBar.setVisibility(View.VISIBLE);
                if (Model.checkNetwork()) {
                    Model.getInstance().saveReview(field_id, text, rating, new Model.AddReviewListener() {
                        @Override
                        public void onResult() {
                            progressBar.setVisibility(View.GONE);
                            showMessage(R.string.review_added_successfully);
                        }

                        @Override
                        public void onCancel() {
                            showMessage(R.string.save_error);
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        return contentView;
    }

    private void showMessage(int messageCode) {
        String message = getResources().getString(messageCode);
        DialogFragment dialog = new AlertDialog();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putBoolean("shouldExit",true);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "TAG");
    }
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onStop()
    {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
