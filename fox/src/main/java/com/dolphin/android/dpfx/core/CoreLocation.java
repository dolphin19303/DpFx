package com.dolphin.android.dpfx.core;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.dolphin.android.dpfx.FxConstants;
import com.dolphin.android.dpfx.bean.FxLocation;
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

    FxLocation mCurrentLocation;

    onLocationChanged mOnLocationChanged;
    CoreLocationListener mCoreLocationListener;

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

    public void connectLocationTracking(CoreLocationListener mCoreLocationListener) {
        if (mLocationClient != null) {
            this.mCoreLocationListener = mCoreLocationListener;
            mLocationClient.connect();
        }
    }

    public void disconnectLocationTracking(CoreLocationListener mCoreLocationListener) {

        if (mLocationClient != null) {
            this.mCoreLocationListener = mCoreLocationListener;
            mLocationClient.disconnect();
        }
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
        if (mCoreLocationListener != null) {
            mCoreLocationListener.onConnected();
        }
    }

    //Connection status - disconnected
    @Override
    public void onDisconnected() {
        if (mCoreLocationListener != null) {
            mCoreLocationListener.onDisconnected();
        }
    }

    //Connection status - failed
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //get current location
    public FxLocation getCurrentLocation() {
        mCurrentLocation = new FxLocation(mLocationClient.getLastLocation());
        return mCurrentLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        notifyLocationChange(new FxLocation(location));
        L.e(TAG + "Location changed: " + location.getLatitude() + " - " + location.getLongitude());
    }

    //location changed interface
    public interface onLocationChanged {
        void onChanged(FxLocation currentLocation);
    }

    //set interface
    public void setOnLocationChanged(onLocationChanged mOnLocationChanged) {
        this.mOnLocationChanged = mOnLocationChanged;
    }

    private void notifyLocationChange(FxLocation location) {
        if (mOnLocationChanged != null) {
            mOnLocationChanged.onChanged(location);
        }
    }

    public interface CoreLocationListener {
        public void onConnected();
        public void onDisconnected();
    }
}
