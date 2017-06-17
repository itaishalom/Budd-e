package com.shalom.itai.theservantexperience.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shalom.itai.theservantexperience.R;

import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.shalom.itai.theservantexperience.services.BuggerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.shalom.itai.theservantexperience.Utils.Constants.SAVE_IMAGE;
import static com.shalom.itai.theservantexperience.Utils.Functions.getDistanceFromLatLonInKm;
import static com.shalom.itai.theservantexperience.Utils.Functions.throwRandom;


public class TripActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DialogCaller {
    private int TAKE_IMAGE = 1;
    private GoogleMap mMap;
    private double savedDistLat = 0;
    private double savedDistLng = 0;
    private boolean isRestarted = false;
    private static final String TAG = "TripActivity";

    /**
     * Provides the entry point to Google Play services.
     */
    private GoogleApiClient mGoogleApiClient;
    private boolean connected = false;
    private boolean readyMap = false;

    /**
     * Represents a geographical location.
     */
    private Location mLastLocation;

    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    private static TripActivity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        Intent intent = getIntent();
        if (intent.getBooleanExtra("restart", false)) {
            isRestarted = true;
            this.showDialog();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        act = this;
        // Get Location Manager and check for GPS & Network location services
        buildGoogleApiClient();
    }

    public static TripActivity getInstance() {
        return act;
    }

    private void askForGPS() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    private synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                Log.d(TAG, "onResult: " + state.isGpsPresent());
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.d(TAG, "onResult: good");
                        placeMe();
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            Log.d(TAG, "onResult: res req");
                            status.startResolutionForResult(
                                    getInstance(), 1000);
                        } catch (IntentSender.SendIntentException e) {
                            Log.d(TAG, "onResult: exp");
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.d(TAG, "onResult: donno");
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        BuggerService.getInstance().unbug();
    }
/*
    protected void onStop() {
        //  mGoogleApiClient.disconnect();
        super.onStop();
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation == null) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            placeMe();
                        }

                    }, 2000);
                } else {
                    placeMe();
                }
                // Make sure the app is not already connected or attempting to connect
               /*
                if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
                */
            } else {
                buildGoogleApiClient();
                Log.d(TAG, "onActivityResult: error! canceled");
            }
        } else if (requestCode == TAKE_IMAGE) {

            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(TripActivity.this, "Thanks!" +BuggerService.getInstance().getRandomBless(), Toast.LENGTH_SHORT).show();
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                askForPicure();
                //Write your code if there's no result
            }
        } else {
            buildGoogleApiClient();
            Log.d(TAG, "onActivityResult: error! canceled");
        }
    }

    public Location getCurrentLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }


    public void notGettingCloser() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(TripActivity.this, "We are not getting close!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void weAreThere() {
        BuggerService.getInstance().unTrip();
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(TripActivity.this, "Yey! We are here!! Show me !!!", Toast.LENGTH_LONG).show();

            }
        });
        askForPicure();
        mGoogleApiClient.disconnect();
    }

    private void askForPicure() {
        Intent i = new Intent(this, PictureActivty.class).putExtra(SAVE_IMAGE, true);
        startActivityForResult(i, TAKE_IMAGE);
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
        readyMap = true;
        mMap = googleMap;
        if (connected) {
            //  placeMe();
        }
        // Add a marker in Sydney and move the camera

    }


    private void placeMe() {
        if (mLastLocation != null) {
            LatLng whereAreWe = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(whereAreWe).title("Our position"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(whereAreWe));
            if (!isRestarted) {
                searchFood();
            } else {
                LatLng target = new LatLng(BuggerService.getInstance().getDistanceLat(), BuggerService.getInstance().getDistanceLng());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(target)      // Sets the center of the map to Mountain View
                        .zoom(16)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.addMarker(new MarkerOptions().position(target).title("Jon wants to visit"));
            }
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        connected = true;
        if (readyMap && mLastLocation != null) {
            //     placeMe();
        }

        /*
        if (mLastLocation != null) {
            mLatitudeText.setText(String.format("%s: %f", mLatitudeLabel,
                    mLastLocation.getLatitude()));
            mLongitudeText.setText(String.format("%s: %f", mLongitudeLabel,
                    mLastLocation.getLongitude()));
        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }
        */
    }


    public class getData extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... args) {

            StringBuilder result = new StringBuilder();

            try {
                String googleAPIKey = "AIzaSyCvcfb2pxac2baMGVFvzCAKFPmY735Cw14";

                String requesturl = "https://maps.googleapis.com/maps/api/place/search/json?types=point_of_interest&radius=1000&&key=" + googleAPIKey + "&location=" + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();

                URL url = new URL(requesturl);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }


            return result.toString();
        }

        private boolean checkIsLodging(JSONArray types) {
            for (int j = 0; j < types.length(); j++) {
                if (types.optString(j).equals("lodging")) {
                    return true;
                }
            }
            return false;
        }


        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonList = new JSONObject(result);
                JSONArray arrayOfResults = jsonList.optJSONArray("results");
                int counter = 0;
                while (true) {
                    counter++;
                    if (counter == arrayOfResults.length()) {
                        break;
                    }
                    int index = throwRandom(arrayOfResults.length(), 0);
                    JSONObject firstInList = arrayOfResults.optJSONObject(index);
                    if (firstInList == null) {
                        continue;
                    }
                    if (checkIsLodging(firstInList.optJSONArray("types"))) {
                        if (counter < arrayOfResults.length() - 1) {
                            arrayOfResults.put(index, null);
                            continue;
                        }
                    }
                    String name = firstInList.optString("name");
                    JSONObject location = firstInList.optJSONObject("geometry").optJSONObject("location");

                    double lat = location.optDouble("lat");
                    double lng = location.optDouble("lng");
                    LatLng whereAreWe = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(whereAreWe).title("Jon wants to visit"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(whereAreWe));
                    //   mMap.animateCamera(CameraUpdateFactory.zoomIn());
                    //     mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                    //    mMap.setMaxZoomPreference(6);

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(whereAreWe)      // Sets the center of the map to Mountain View
                            .zoom(16)                   // Sets the zoom
                            .bearing(90)                // Sets the orientation of the camera to east
                            .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    double distanceFromDestination = getDistanceFromLatLonInKm(lat, lng, mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    Toast.makeText(getInstance(), "Let's go to the " + name + "! It's only " + distanceFromDestination + " km from us!!", Toast.LENGTH_LONG).show();
                    BuggerService.getInstance().setDistanceToDest(lat, lng);

//TODO SAVE IT!
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showDialog();
                        }
                    }, 3000);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Do something with the JSON string

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    public void showDialog() {
        ActivityManager mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
        String activityOnTop = ar.topActivity.getClassName();


        if (!activityOnTop.contains("com.shalom.itai.theservantexperience.Activities.TripActivity")) {

            Intent intent = new Intent(this, TripActivity.class).putExtra("restart", true);
            this.startActivity(intent);
        } else {
            DialogFragment newFragment = MyAlertDialogFragment
                    .newInstance(R.string.alert_dialog_Trip_buttons_title, "Yes", "No", getClass().getName());
            newFragment.show(getSupportFragmentManager(), "dialog");
        }
    }

    @Override
    public void doPositive() {
        BuggerService.setSYSTEM_GlobalPoints(1);
        Toast.makeText(this, "YeY!!! A Trip! " + BuggerService.getInstance().getRandomBless(), Toast.LENGTH_LONG).show();
        BuggerService.getInstance().goToTrip();
        Log.i("FragmentAlertDialog", "Positive click!");
    }

    @Override
    public void doNegative() {
        Toast.makeText(this, BuggerService.getInstance().getRandomInsult(), Toast.LENGTH_LONG).show();
        BuggerService.setSYSTEM_GlobalPoints(-1);
        finish();
        Log.i("FragmentAlertDialog", "Negative click!");
    }


    private void searchFood() {
        //    https://maps.googleapis.com/maps/api/place/search/json?types=bar&restaurant&radius=500&&key=AIzaSyCvcfb2pxac2baMGVFvzCAKFPmY735Cw14&location=31.7813273,35.2148459
        new getData().execute();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}