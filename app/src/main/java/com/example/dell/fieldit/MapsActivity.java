package com.example.dell.fieldit;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dell.fieldit.Model.Field;
import com.example.dell.fieldit.Model.Model;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    public static final int ADD_FRAGMENT = 1;
    public static final int REFRESH_FRAGMENT = 2;
    public static final int LOGOUT_FRAGMENT = 3;
    public static final int DETAILS_FRAGMENT = 3;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    // private Location mLastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

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

        Model.getInstance().getAllFieldsAsynch(new Model.GetFieldsListener() {
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

        startActivity(DetailsIntent);

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

                startActivity(AddIntent);

                return true;

            // Handle click on refresh button
//            case R.id.refresh_button:
//
//                // Refresh the trips list
//                refreshTripsList();
//
//                // Show relevant message
//                Toast.makeText(this, R.string.refresh_button_message, Toast.LENGTH_LONG).show();
//
//                // Set the icon of the refresh button to "no updates"
//                changeRefreshButtonIcon(false);
//
//                return true;

            // If user chose to sign out
//            case R.id.sign_out_button:
//
//                // Sign out using firebase api
//                auth.signOut();

                //return true;
        }
        return false;
    }
}
