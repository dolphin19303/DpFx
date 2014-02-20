package com.dolphin.android.dpfx.bean;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Administrator on 2/20/14.
 */
public class FxLocation extends Location {
    //Default provider
    private static String PROVIDER_DEFAULT = "flp";

    public FxLocation(String provider) {
        super(provider == null ? PROVIDER_DEFAULT : provider);
    }

    public FxLocation(Location location) {
        super(location);
    }

    public FxLocation(String provider, double lat, double lng) {
        super(provider == null ? PROVIDER_DEFAULT : provider);
        setLatitude(lat);
        setLongitude(lng);
    }

    public FxLocation(double lat, double lng) {
        super(PROVIDER_DEFAULT);
        setLatitude(lat);
        setLongitude(lng);
    }

    public LatLng getLatLng() {
        return new LatLng(getLatitude(), getLongitude());
    }

    public Location getLocation() {
        return this;
    }
}
