package com.dolphin.android.dpfx.fragment;

import android.location.Location;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dolphin.android.dpfx.FxApplication;
import com.dolphin.android.dpfx.FxBaseFragment;
import com.dolphin.android.dpfx.FxConstants;
import com.dolphin.android.dpfx.R;
import com.dolphin.android.dpfx.bean.FxLocation;
import com.dolphin.android.dpfx.core.CoreLocation;
import com.dolphin.android.dpfx.utils.MapController;
import com.dolphin.android.dpfx.utils.T;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Administrator on 2/18/14.cls
 */
public class MapFragment extends FxBaseFragment implements FxConstants {
    private static String TAG = "MapFragment";
    private static View view;

    //Location manager
    CoreLocation mCoreLocation;

    //Map layer
    private GoogleMap mMap;
    private MapController mMapController;

    //Marker
    Marker mMarkerUser;

    //Current location button
    Button btnCurrentLocation;
    int currentLocationStatus;
    //Test button
    Button btnTest1, btnTest2, btnTest3;

    //Test lat, lon position
    double testLocation[][] = {{21.033916, 105.852077}, {21.033904, 105.852401}, {21.033839, 105.852932}, {21.033794, 105.853345}, {21.033749, 105.85378}};
    //Test mock Location

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //init core location
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        } else {
            try {
                view = inflater.inflate(R.layout.fragment_map, container, false);
            } catch (InflateException e) {
                /* map is already there, just return view as it is */
            }
        }

        //Init location manager
        mCoreLocation = FxApplication.getLocationManager();
        mCoreLocation.setOnLocationChanged(onLocationChanged);
        mCoreLocation.connectLocationTracking(new CoreLocation.CoreLocationListener() {
            @Override
            public void onConnected() {
                mCoreLocation.startLocationTracking();
            }

            @Override
            public void onDisconnected() {
            }
        });

        //init view
        initView();
        initMap();
        return view;
    }

    //initial view
    private void initView() {

        btnCurrentLocation = (Button) view.findViewById(R.id.btnCurrentLocation);
        btnCurrentLocation.setOnClickListener(onButtonClick);
        //init test button
        btnTest1 = (Button) view.findViewById(R.id.btnTest1);
        btnTest2 = (Button) view.findViewById(R.id.btnTest2);
        btnTest3 = (Button) view.findViewById(R.id.btnTest3);
        btnTest1.setOnClickListener(mTestOnClick);
        btnTest2.setOnClickListener(mTestOnClick);
        btnTest3.setOnClickListener(mTestOnClick);


    }

    private void initMap() {
        //init map
        mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        //set controller for map
        mMapController = new MapController(mMap);

        mMarkerUser = mMap.addMarker(new MarkerOptions()
                .position(DEFAULT_LOCATION.getLatLng())
                .title("Dolphin"));

        mMapController.AnimateTo(DEFAULT_LOCATION, DEFAULT_ANIMATE_TIME, DEFAULT_ZOOM_LEVEL, new MapController.MapControllerCameraCallBack() {
            @Override
            public void onAnimationComplete() {
                T.show("Complete");
            }
        });
    }

    //Action test button
    static int iLocation = 0;
    View.OnClickListener mTestOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnTest1:
                    break;
                case R.id.btnTest2:
                    mCoreLocation.startLocationTracking();
                    break;
                case R.id.btnTest3:
                    T.show("Zoom Level: " + mMapController.getCurrentZoomLevel());
                    break;
            }
        }
    };
    View.OnClickListener onButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCurrentLocation:
                    currentLocationStatus++;
                    if (currentLocationStatus > 3) {
                        currentLocationStatus = 0;
                    }
                    switch (currentLocationStatus) {
                        case 0:
                            btnCurrentLocation.setText("X");
                            mMapController.reset();
                            break;
                        case 1:
                            btnCurrentLocation.setText("Y");
                            mMapController.AnimateTo(mCoreLocation.getCurrentLocation(), DEFAULT_ANIMATE_TIME, DEFAULT_ZOOM_LEVEL, null);
                            break;
                        case 2:
                            btnCurrentLocation.setText("Z");
                            mCoreLocation.startLocationTracking();
                            break;
                        case 3:
                            btnCurrentLocation.setText("T");
                            mCoreLocation.startLocationTracking();
                            break;
                    }
                    break;
            }
        }
    };

    CoreLocation.onLocationChanged onLocationChanged = new CoreLocation.onLocationChanged() {
        @Override
        public void onChanged(FxLocation currentLocation) {
            if (mMarkerUser != null && currentLocation != null) {
                T.show(currentLocation.getLatitude() + " - " + currentLocation.getLongitude());
                mMarkerUser.setPosition(currentLocation.getLatLng());
                mMapController.AnimateTo(currentLocation, DEFAULT_ANIMATE_TIME, DEFAULT_ZOOM_LEVEL, new MapController.MapControllerCameraCallBack() {
                    @Override
                    public void onAnimationComplete() {
                        T.show("Moving!");
                    }
                });
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
