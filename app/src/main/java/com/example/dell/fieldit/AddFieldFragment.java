package com.example.dell.fieldit;

import android.annotation.TargetApi;
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
        name = nameEt.getText().toString();
        isLightedCb = (CheckBox) contentView.findViewById(R.id.new_field_isLighted);
        isLighted = isLightedCb.isChecked();
        Spinner spinner = (Spinner) contentView.findViewById(R.id.new_field_types_spinner);
        type = spinner.getSelectedItem().toString();
        longitudeEt = (EditText) contentView.findViewById(R.id.new_field_longitude);
        longitude = longitudeEt.getText().toString();
        latitudeEt = (EditText) contentView.findViewById(R.id.new_field_latitude);
        latitude = latitudeEt.getText().toString();
        descriptionEt = (EditText) contentView.findViewById(R.id.new_field_description);
        description = descriptionEt.getText().toString();

        Button saveBtn = (Button) contentView.findViewById(R.id.new_field_save_button);
        Button cancelBtn = (Button) contentView.findViewById(R.id.new_field_cancel_button);

//        saveBtn.setOnClickListener(new View.OnClickListener() {
//            @TargetApi(Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//                Log.d("TAG","Btn Save click");
//                Log.d("TAG",nameEt.getText().toString());
//                Log.d("TAG",idEt.getText().toString());
//                Log.d("TAG",phoneEt.getText().toString());
//                Log.d("TAG",addressEt.getText().toString());
//
//                Student st = new Student();
//                st.name = nameEt.getText().toString();
//                st.id = idEt.getText().toString();
//                st.phone = phoneEt.getText().toString();
//                st.address = addressEt.getText().toString();
//                st.checked = checkedCb.isChecked();
//                st.imageUrl = "";
//                st.birthDay = date.day;
//                st.birthYear = date.year;
//                st.birthMonth = date.month;
//                st.birthMinute = time.minute;
//                st.birthHour = time.hour;
//
//                Model.instace.addStudent(st);
//                android.app.Fragment newFragment = AddFieldFragment.newInstance();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//                // Replace whatever is in the fragment_container view with this fragment,
//                // and add the transaction to the back stack
//                transaction.replace(R.id.main_container , newFragment);
//                transaction.addToBackStack(null);
//
//                // Commit the transaction
//                transaction.commit();
//
//            }
//        });

//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                android.app.Fragment newFragment = AddFieldFragment.newInstance();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//                // Replace whatever is in the fragment_container view with this fragment,
//                // and add the transaction to the back stack
//                transaction.replace(R.id.main_container , newFragment);
//                transaction.addToBackStack(null);
//
//                // Commit the transaction
//                transaction.commit();
//            }
//
//        });

        return contentView;
    }

    private void setTypesDropDown( final View view) {

        // Populate values in the types spinner
        Spinner spinner = (Spinner) view.findViewById(R.id.new_field_types_spinner);

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
}
