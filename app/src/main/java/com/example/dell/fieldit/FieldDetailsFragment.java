package com.example.dell.fieldit;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.fieldit.Model.Field;
import com.example.dell.fieldit.Model.Model;

import org.w3c.dom.Text;


public class FieldDetailsFragment extends Fragment {


    // private OnFragmentInteractionListener mListener;
    private Boolean isEditMode = false;
    private String fieldId;
    Boolean imageChanged = false;
    Field fd;
    ArrayAdapter<CharSequence> adapter;

    ImageView imageView = null;
    Bitmap imageBitmap = null;
    EditText nameEt;
    EditText longitudeEt;
    EditText latitudeEt;
    EditText descriptionEt;
    CheckBox isLightedCb;
    Spinner spinner;
    Button saveBtn;
    Button deleteBtn;
    private ProgressBar progressBar;

    public FieldDetailsFragment() {
        // Required empty public constructor
    }

    public static FieldDetailsFragment newInstance() {
        FieldDetailsFragment fragment = new FieldDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            fieldId = getArguments().getString("id");
            fd = Model.getInstance().getFieldById(fieldId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View contentView =  inflater.inflate(R.layout.fragment_field_details, container, false);

        // Get the image view of the view
        imageView = (ImageView) contentView.findViewById(R.id.field_details_imageview);
        imageView.setEnabled(false);

        // Get the progress bar
        progressBar = (ProgressBar) contentView.findViewById(R.id.details_progressBar);

        // Populate values in the types spinner
        setTypesDropDown(contentView);

        nameEt = (EditText) contentView.findViewById(R.id.field_details_name);
        isLightedCb = (CheckBox) contentView.findViewById(R.id.field_details_isLighted);
        longitudeEt = (EditText) contentView.findViewById(R.id.field_details_longitude);
        latitudeEt = (EditText) contentView.findViewById(R.id.field_details_latitude);
        descriptionEt = (EditText) contentView.findViewById(R.id.field_details_description);

        nameEt.setText(fd.getName());
        isLightedCb.setChecked(fd.getIslighted());
        longitudeEt.setText(fd.getLongitude());
        latitudeEt.setText(fd.getLatitude());
        descriptionEt.setText(fd.getDescription());


        if (fd.getImageName() != null) {

            // Load the trip's image
            Model.getInstance().loadImage(fd.getImageName(), new Model.GetImageListener() {
                @Override
                public void onSuccess(Bitmap imagebtmp) {
                    if (imagebtmp != null) {
                        imageBitmap = imagebtmp;
                        // Set the loaded image in the view
                        imageView.setImageBitmap(imagebtmp);
                    }
                }

                @Override
                public void onFail() {
                }
            });
        }

        saveBtn = (Button) contentView.findViewById(R.id.field_details_save_button);
        deleteBtn = (Button) contentView.findViewById(R.id.field_details_delete_button);

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

                // Check validation of fields
                if(!name.trim().isEmpty() && !longitude.trim().isEmpty() && !latitude.trim().isEmpty()
                        && !type.trim().isEmpty() && !description.trim().isEmpty()) {

                    // Check if at least one field has changed
                    if (!name.equals(fd.getName()) || !longitude.equals(fd.getLongitude()) || !latitude.equals(fd.getLatitude())
                            || !type.equals(fd.getType()) || !description.equals(fd.getDescription())
                            || !isLighted.equals(fd.getIslighted()) || imageChanged == true) {

                        final Field editedField = new Field(name,type,latitude,longitude,description,isLighted);
                        editedField.setId(fd.getId());
                        editedField.setImageName(fd.getImageName());
                        progressBar.setVisibility(View.VISIBLE);

                        Model.getInstance().editField(editedField,imageBitmap,new Model.EditFieldListener() {
                            @Override
                            public void onResult() {

                                progressBar.setVisibility(View.GONE);

                                // Show relevant message
                                showMessage(R.string.save_successfully, false);
                                fd = editedField;
                                exitEditMode();
                                getActivity().setResult(Activity.RESULT_OK);
                            }

                            @Override
                            public void onCancel() {

                                // Show relevant message
                                showMessage(R.string.save_error, false);
                            }
                        });

                    } else {
                        Log.d("TAG", "onClick: Nothing has changed");
                        showMessage(R.string.no_field_has_changed, false);
                    }
                } else {
                    // Show relevant message
                    Log.d("TAG", "onClick: Field is empty");
                    showMessage(R.string.must_fill_all_fields, false);
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Model.getInstance().deleteField(fd.getId(),new Model.DeleteFieldListener() {
                    @Override
                    public void onResult(String id) {
                        progressBar.setVisibility(View.GONE);
                        showMessage(R.string.delete_successfully, true);
                        exitEditMode();
                        getActivity().setResult(Activity.RESULT_OK);
                    }

                    @Override
                    public void onCancel() {

                        // Show relevant message
                        showMessage(R.string.delete_error, false);
                    }

                });
            }
        });

        // Set click handler for the image view
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open camera and take user picture
                takePicture();
            }
        });

        return contentView;
    }

    private void takePicture(){
        // Start the camera in order to take picture from the user
        Intent takeImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takeImageIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takeImageIntent, AddFieldFragment.TAKING_IMAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Handle the processing of the image from the user
        if (requestCode == AddFieldFragment.TAKING_IMAGE && resultCode == Activity.RESULT_OK) {
            imageChanged = true;
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

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
        // mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setTypesDropDown( final View view) {

        // Populate values in the types spinner
        spinner = (Spinner) view.findViewById(R.id.field_details_types_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.field_types, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(fd.getType()));
        spinner.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.edit_field_button) {
            changeEnableEditFields();
            if (!isEditMode) {
                isEditMode = true;
                enableButtons();
            } else {
                isEditMode = false;
                disableButtons();
                cancelChanges();
                //showMessage(R.string.Action_canceled, false);
            }
        }
        else if(id == R.id.add_review_button)
        {
            AddReviewFregment addReviewFregment = AddReviewFregment.newInstance();
            Bundle args = new Bundle();
            args.putString("field_id", fieldId);
            addReviewFregment.setArguments(args);
            FragmentTransaction tran = getFragmentManager().beginTransaction();
            tran.replace(R.id.activity_field,addReviewFregment);
            tran.commit();
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void changeEnableEditFields() {
        nameEt.setEnabled(!nameEt.isEnabled());
        imageView.setEnabled(!imageView.isEnabled());
        longitudeEt.setEnabled(!longitudeEt.isEnabled());
        latitudeEt.setEnabled(!latitudeEt.isEnabled());
        descriptionEt.setEnabled(!descriptionEt.isEnabled());
        spinner.setEnabled(!spinner.isEnabled());
        isLightedCb.setEnabled(!isLightedCb.isEnabled());
    }

    private void enableButtons() {
        saveBtn.setVisibility(View.VISIBLE);
        saveBtn.setEnabled(true);
        deleteBtn.setVisibility(View.VISIBLE);
        deleteBtn.setEnabled(true);
    }

    private void disableButtons() {
        saveBtn.setVisibility(View.INVISIBLE);
        saveBtn.setEnabled(false);
        deleteBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setEnabled(false);
    }

    private void cancelChanges() {
        nameEt.setText(fd.getName());
        isLightedCb.setChecked(fd.getIslighted());
        longitudeEt.setText(fd.getLongitude());
        latitudeEt.setText(fd.getLatitude());
        descriptionEt.setText(fd.getDescription());

        spinner.setSelection(adapter.getPosition(fd.getType()));
        if( fd.getImageName() != null) {
            imageView.setImageBitmap(imageBitmap);
        } else {
            Bitmap defualtImage = BitmapFactory.decodeResource(MyApplication.getAppContext().getResources(),
                    R.drawable.field);
            imageView.setImageBitmap(defualtImage);
        }

    }

    private void showMessage(int messageCode, Boolean shouldExit) {
        String message = getResources().getString(messageCode);
        DialogFragment dialog = new AlertDialog();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putBoolean("shouldExit",shouldExit);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "TAG");
    }

    private void exitEditMode() {
        changeEnableEditFields();
        isEditMode = false;
        disableButtons();
    }


}
