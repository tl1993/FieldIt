package com.example.dell.fieldit;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.dell.fieldit.Model.Model;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link AddReviewFregment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddReviewFregment extends Fragment {


    // TODO: Rename and change types of parameters
    ImageView imageView = null;
    Bitmap imageBitmap = null;
    EditText editText;
    RatingBar ratingBar;
    String field_id;
    //private OnFragmentInteractionListener mListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_add_review, container, false);
        // Get the image view of the view
        imageView = (ImageView) contentView.findViewById(R.id.new_review_imageview);
        editText = (EditText) contentView.findViewById(R.id.new_review_text);
        ratingBar = (RatingBar) contentView.findViewById(R.id.new_review_rating);

        Button saveBtn = (Button) contentView.findViewById(R.id.new_review_save_button);
        Button cancelBtn = (Button) contentView.findViewById(R.id.new_review_cancel_button);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                int rating = ratingBar.getNumStars();
                Model.getInstance().saveReview(field_id,text,rating);
                //TODO replace transaction
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //TODO replace transaction
            }
        });

        return contentView;
    }


    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
