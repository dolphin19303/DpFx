package com.dolphin.android.dpfx.core;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.dolphin.android.dpfx.FxConstants;
import com.dolphin.android.dpfx.utils.L;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * Created by Administrator on 2/19/14.
 */
public class CoreLocation implements FxConstants, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
    private static String TAG = "CoreLocation";
    LocationClient mLocationClient;
    Context mContext;

    Location mCurrentLocation;


    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    // Define an object that holds accuracy and frequency parameters
    LocationRequest mLocationRequest;

    public CoreLocation(Context mContext) {
        this.mContext = mContext;
        mLocationClient = new LocationClient(mContext, this, this);

        //Init location tracking
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
    }

    public void connectLocationTracking() {
        mLocationClient.connect();
        L.e(TAG + "Connecting");
    }

    public void disconnectLocationTracking() {
        mLocationClient.disconnect();
    }

    public void startLocationTracking() {
        if (mLocationClient.isConnected()) {
            L.e(TAG + "Start tracking");
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
        }
    }

    public void stopLocationTracking() {
        if (mLocationClient.isConnected()) {
            mLocationClient.removeLocationUpdates(this);
        }
    }

    //Connection status - connected
    @Override
    public void onConnected(Bundle bundle) {
        mLocationClient.setMockMode(true);
        L.e(TAG + "Connected");
    }

    //Connection status - disconnected
    @Override
    public void onDisconnected() {

    }

    //Connection status - failed
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //get current location
    public Location getCurrentLocation() {
        mCurrentLocation = mLocationClient.getLastLocation();
        return mCurrentLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        L.e(TAG + "Location changed: " + location.getLatitude() + " - " + location.getLongitude());
    }

    //Test mock data
    private static final String PROVIDER = "flp";
    private static final double LAT = 37.377166;
    private static final double LNG = -122.086966;
    private static final float ACCURACY = 3.0f;

    public Location createLocation(double lat, double lng, float accuracy) {
        // Create a new Location
        Location newLocation = new Location(PROVIDER);
        newLocation.setLatitude(lat);
        newLocation.setLongitude(lng);
        newLocation.setAccuracy(accuracy);
        return newLocation;
    }

    public void setMockLocation(Location lc) {
        mLocationClient.setMockLocation(lc);
    }
}
