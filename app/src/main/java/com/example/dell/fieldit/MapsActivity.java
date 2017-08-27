package com.example.dell.fieldit;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dell.fieldit.Model.Field;
import com.example.dell.fieldit.Model.Model;
import com.example.dell.fieldit.Model.ModelFirebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,GoogleMap.OnMapLongClickListener,FieldUpdateListener
{
    public static final int ADD_FRAGMENT = 1;
    // public static final int REFRESH_FRAGMENT = 2;
    // public static final int LOGOUT_FRAGMENT = 3;
    public static final int DETAILS_FRAGMENT = 3;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    public void onFieldChange() {
        Model.getInstance().getAllUpdatedFields(new Model.GetFieldsListener() {
            @Override
            public void onResult(List<Field> fields, List<Field> tripsToDelete) {
                mMap.clear();
                List<Field> data = fields;
                for(Field field : data) {
                    LatLng position = new LatLng(Double.parseDouble(field.getLatitude()), Double.parseDouble(field.getLongitude()));
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(field.getName())
                            .snippet("For more info click here"));
                    m.setTag(field.getId());
                }
                //mMap.setOnInfoWindowClickListener(this);
            }
            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        Model.getInstance().setFieldUpdateListener(this);
        // Set authorize listener to handle change in the user state
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // Get the current user
                FirebaseUser user = firebaseAuth.getCurrentUser();

                // If user chose to log out
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MapsActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Model.getInstance().getAllUpdatedFields(new Model.GetFieldsListener() {
            @Override
            public void onResult(List<Field> fields, List<Field> tripsToDelete) {
                List<Field> data = fields;
                for(Field field : data) {
                    LatLng position = new LatLng(Double.parseDouble(field.getLatitude()), Double.parseDouble(field.getLongitude()));
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(field.getName())
                            .snippet("For more info click here"));
                    m.setTag(field.getId());
                }
                //mMap.setOnInfoWindowClickListener(this);
            }
            @Override
            public void onCancel() {
            }
        });

        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapLongClickListener(this);
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
//        Toast.makeText(this, "Info window clicked",
//                Toast.LENGTH_SHORT).show();
        Intent DetailsIntent = new Intent(this, FieldActivity.class);
        DetailsIntent.putExtra("frgToLoad", DETAILS_FRAGMENT);
        DetailsIntent.putExtra("id", marker.getTag().toString());

        //startActivity(DetailsIntent);
        startActivityForResult(DetailsIntent, DETAILS_FRAGMENT);
    }

    @Override
    public void onMapLongClick(LatLng point) {
        String latitude =String.valueOf(point.latitude);
        String longitude = String.valueOf(point.longitude);
        Intent AddIntent = new Intent(this, FieldActivity.class);
        AddIntent.putExtra("frgToLoad", ADD_FRAGMENT);
        AddIntent.putExtra("latitude",latitude);
        AddIntent.putExtra("longitude",longitude);
        startActivityForResult(AddIntent, ADD_FRAGMENT);

    }

    private void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            // mLastKnownLocation = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.refresh_button){
//            getFragmentManager().popBackStack();
//        }
//        else if (id == R.id.main_add) {
//            Fragment newFragment = new AddFieldFragment();
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//            // Replace whatever is in the fragment_container view with this fragment,
//            // and add the transaction to the back stack
//            transaction.replace(R.id.main_container , newFragment);
//            transaction.addToBackStack(null);
//
//            // Commit the transaction
//            transaction.commit();
//        }
//        else
//        {
//            return super.onOptionsItemSelected(item);
//        }
//        return true;

        // Handle menu action according to item id
        switch (item.getItemId()) {

            // Handle click on new trip button
            case R.id.add_field_button:

                // Start the Add Field 9Fragment
//                Fragment newFragment = new AddFieldFragment();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//                // Replace whatever is in the fragment_container view with this fragment,
//                // and add the transaction to the back stack
//                transaction.replace(R.id.map , newFragment);
//                transaction.addToBackStack(null);
//
//                // Commit the transaction
//                transaction.commit();

                Intent AddIntent = new Intent(this, FieldActivity.class);
                AddIntent.putExtra("frgToLoad", ADD_FRAGMENT);

                startActivityForResult(AddIntent, ADD_FRAGMENT);

                return true;

            // Handle click on refresh button
            case R.id.refresh_button:

                // Refresh the trips list
                //Model.getInstance().refreshFieldsList();
                refreshMapObjects();
                return true;

            // If user chose to sign out
            case R.id.signout_button:
                // Sign out using firebase api
                auth.signOut();

                return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Handle data change by user
        if (requestCode == ADD_FRAGMENT || requestCode == DETAILS_FRAGMENT) {
            if(resultCode == Activity.RESULT_OK) {
                refreshMapObjects();
            }
        }
    }

    private void refreshMapObjects() {
        mMap.clear();
        List<Field> updatedFields = Model.getInstance().refreshFieldsList();
        for(Field field : updatedFields) {
            LatLng position = new LatLng(Double.parseDouble(field.getLatitude()), Double.parseDouble(field.getLongitude()));
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(field.getName())
                    .snippet("For more info click here"));
            m.setTag(field.getId());
        }
    }
}
