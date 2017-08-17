package com.example.dell.fieldit;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dell.fieldit.Model.Field;
import com.example.dell.fieldit.Model.Model;

import static android.R.attr.imeSubtypeLocale;
import static android.R.attr.type;

public class AddFieldFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ImageView imageView = null;
    Bitmap imageBitmap = null;
    EditText nameEt;
    EditText longitudeEt;
    EditText latitudeEt;
    EditText descriptionEt;
    CheckBox isLightedCb;
    private ProgressBar progressBar;
    Spinner spinner;
    //ProgressBar progressBar;

    //private OnFragmentInteractionListener mListener;

    public AddFieldFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFieldFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFieldFragment newInstance() {
        AddFieldFragment fragment = new AddFieldFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String name;
        Boolean isLighted;
        String type;
        String longitude;
        String latitude;
        String description;

        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_add_field, container, false);

        // Get the image view of the view
        imageView = (ImageView) contentView.findViewById(R.id.new_field_imageview);

        // Get the progress bar
        progressBar = (ProgressBar) contentView.findViewById(R.id.new_progressBar);

        // Populate values in the types spinner
        setTypesDropDown(contentView);

        nameEt = (EditText) contentView.findViewById(R.id.new_field_name);
        isLightedCb = (CheckBox) contentView.findViewById(R.id.new_field_isLighted);
        spinner = (Spinner) contentView.findViewById(R.id.new_field_types_spinner);
        longitudeEt = (EditText) contentView.findViewById(R.id.new_field_longitude);
        latitudeEt = (EditText) contentView.findViewById(R.id.new_field_latitude);
        descriptionEt = (EditText) contentView.findViewById(R.id.new_field_description);

        Button saveBtn = (Button) contentView.findViewById(R.id.new_field_save_button);
        Button cancelBtn = (Button) contentView.findViewById(R.id.new_field_cancel_button);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                String longitude;
                String latitude;
                String type;
                String description;
                Boolean isLighted;

                // Get the values of the view's fields
                name = nameEt.getText().toString();
                longitude = longitudeEt.getText().toString();
                latitude = latitudeEt.getText().toString();
                type = spinner.getSelectedItem().toString();
                description = descriptionEt.getText().toString();
                isLighted = isLightedCb.isChecked();

                //TODO: CHECK IMAGE CHANGES

                // Check validation of fields
                if(!name.trim().isEmpty() && !longitude.trim().isEmpty() && !latitude.trim().isEmpty()
                        && !type.trim().isEmpty() && !description.trim().isEmpty()) {

                        Field newField = new Field(name,type,latitude,longitude,description,isLighted);
                        progressBar.setVisibility(View.VISIBLE);

                        Model.getInstance().addField(newField,imageBitmap,new Model.AddFieldListener() {
                            @Override
                            public void onResult() {
                                progressBar.setVisibility(View.GONE);
                                showMessage(R.string.field_added_successfully);
                            }

                            @Override
                            public void onCancel() {
                                showMessage(R.string.save_error);
                            }
                        });
                } else {
                    // Show relevant message
                    showMessage(R.string.must_fill_all_fields);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Model.getInstance().deleteField(fd);
                showMessage(R.string.Action_canceled);
                //showMessage(R.string.delete_error);
            }
        });

        return contentView;
    }

    private void setTypesDropDown( final View view) {

        // Populate values in the types spinner
        spinner = (Spinner) view.findViewById(R.id.new_field_types_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.field_types, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    private void showMessage(int messageCode) {
        String message = getResources().getString(messageCode);
        DialogFragment dialog = new AlertDialog();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putBoolean("shouldExit",true);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "TAG");
    }
}
