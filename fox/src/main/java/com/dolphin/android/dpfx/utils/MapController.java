package com.dolphin.android.dpfx.utils;

import com.dolphin.android.dpfx.bean.FxLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;

/**
 * Created by Administrator on 2/21/14.
 */
public class MapController {
    private static final String TAG = "MapController";
    GoogleMap mMap;
    MapControllerCameraCallBack mapControllerCallBack;

    public MapController(GoogleMap map) {
        if (map != null) {
            mMap = map;
        } else {
            L.e(TAG + " get null when init map");
        }
    }

    public void AnimateTo(FxLocation location, int time, int zoomLevel, final MapControllerCameraCallBack mapControllerCallBack) {
        try {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(location.getLatLng())      // Sets the center of the map to Mountain View
                    .zoom(zoomLevel)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), time, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    if (mapControllerCallBack != null) {
                        mapControllerCallBack.onAnimationComplete();
                    }
                }

                @Override
                public void onCancel() {

                }
            });
        } catch (NullPointerException e) {
            L.e(TAG + " Get null when put data");
        }
    }

    public void MoveTo() {
    }

    public interface MapControllerCameraCallBack {
        public void onAnimationComplete();
    }
}
