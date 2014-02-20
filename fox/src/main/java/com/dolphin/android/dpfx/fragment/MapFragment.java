package com.dolphin.android.dpfx.fragment;

import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dolphin.android.dpfx.FxBaseFragment;
import com.dolphin.android.dpfx.R;
import com.dolphin.android.dpfx.core.CoreLocation;

/**
 * Created by Administrator on 2/18/14.cls
 */
public class MapFragment extends FxBaseFragment {
    private static String TAG = "MapFragment";
    private static View view;
    CoreLocation mCoreLocation;

    //Test button
    Button btnTest1, btnTest2, btnTest3;

    //Test lat, lon position
    double testLocation[][] = {{21.033916, 105.852077}, {21.033904, 105.852401}, {21.033839, 105.852932}, {21.033794, 105.853345}, {21.033749, 105.85378}};
    //Test mock Location

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //init core location
        mCoreLocation = new CoreLocation(getActivity());


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
        mCoreLocation.connectLocationTracking();

        //init test button
        btnTest1 = (Button) view.findViewById(R.id.btnTest1);
        btnTest2 = (Button) view.findViewById(R.id.btnTest2);
        btnTest3 = (Button) view.findViewById(R.id.btnTest3);
        btnTest1.setOnClickListener(mTestOnClick);
        btnTest2.setOnClickListener(mTestOnClick);
        btnTest3.setOnClickListener(mTestOnClick);
        return view;
    }

    //Action test button
    static int iLocation = 0;
    View.OnClickListener mTestOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnTest1:
                    mCoreLocation.connectLocationTracking();
                    break;
                case R.id.btnTest2:
//                    mCoreLocation.setMockLocation(mCoreLocation.createLocation(testLocation[iLocation][0], testLocation[iLocation][1], 3.0f));
//                    L.e(TAG + String.valueOf(testLocation[iLocation][0]) + " " + String.valueOf(testLocation[iLocation][1]));
//                    iLocation++;
//                    if (iLocation >= testLocation.length) {
//                        iLocation = 0;
//                    }

                    break;
                case R.id.btnTest3:
                    mCoreLocation.startLocationTracking();
                    break;
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
