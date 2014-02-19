package com.dolphin.android.dpfx.fragment;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dolphin.android.dpfx.FxBaseFragment;
import com.dolphin.android.dpfx.R;
import com.dolphin.android.dpfx.core.CoreLocation;
import com.dolphin.android.dpfx.core.LocationUtils;
import com.dolphin.android.dpfx.core.SendMockLocationService;
import com.dolphin.android.dpfx.utils.L;

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
// Broadcast receiver for local broadcasts from SendMockLocationService
    private ServiceMessageReceiver mMessageReceiver;

    // Intent to send to SendMockLocationService. Contains the type of test to run
    private Intent mRequestIntent;

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

                    // Instantiate a broadcast receiver for Intents coming from the Service
                    mMessageReceiver = new ServiceMessageReceiver();

                    /*
                     * Filter incoming Intents from the Service. Receive only Intents with a particular action
                     * value.
                     */
                    IntentFilter filter = new IntentFilter(LocationUtils.ACTION_SERVICE_MESSAGE);

                    /*
                     * Restrict detection of Intents. Only Intents from other components in this app are
                     * detected.
                     */
                    LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, filter);

                    // Instantiate the Intent that starts SendMockLocationService
                    mRequestIntent = new Intent(getActivity(), SendMockLocationService.class);

                    // Verify the input values and put them into global variables
                    if (getInputValues()) {
                        // Notify SendMockLocationService to loop once through the mock locations
                        mRequestIntent.setAction(LocationUtils.ACTION_START_ONCE);
                        // Start SendMockLocationService
                        getActivity().startService(mRequestIntent);
                    }
                    break;
                case R.id.btnTest3:
                    mCoreLocation.startLocationTracking();
                    break;
            }
        }
    };

    /**
     * Verify the pause interval and send interval from the UI. If they're correct, store
     * them in the Intent that's used to start SendMockLocationService
     *
     * @return true if all the input values are correct; otherwise false
     */
    public boolean getInputValues() {

        // Get the values from the UI
        String pauseIntervalText = "1";
        String sendIntervalText = "1";

        if (TextUtils.isEmpty(pauseIntervalText)) {
            return false;
        } else if (Integer.valueOf(pauseIntervalText) <= 0) {
            return false;
        }

        if (TextUtils.isEmpty(sendIntervalText)) {
            return false;
        } else if (Integer.valueOf(sendIntervalText) <= 0) {
            return false;
        }

        int pauseValue = Integer.valueOf(pauseIntervalText);
        int sendValue = Integer.valueOf(sendIntervalText);

        mRequestIntent.putExtra(LocationUtils.EXTRA_PAUSE_VALUE, pauseValue);
        mRequestIntent.putExtra(LocationUtils.EXTRA_SEND_INTERVAL, sendValue);

        return true;
    }

    /**
     * Broadcast receiver triggered by broadcast Intents within this app that match the
     * receiver's filter (see onCreate())
     */
    private class ServiceMessageReceiver extends BroadcastReceiver {

        /*
         * Invoked when a broadcast Intent from SendMockLocationService arrives
         *
         * context is the Context of the app
         * intent is the Intent object that triggered the receiver
         */
        @Override
        public void onReceive(Context context, Intent intent) {

            // Get the message code from the incoming Intent
            int code1 = intent.getIntExtra(LocationUtils.KEY_EXTRA_CODE1, 0);
            int code2 = intent.getIntExtra(LocationUtils.KEY_EXTRA_CODE2, 0);

            // Choose the action, based on the message code
            switch (code1) {
                /*
                 * SendMockLocationService reported that the location client is connected. Update
                 * the app status reporting field in the UI.
                 */
                case LocationUtils.CODE_CONNECTED:
                    L.e(TAG + " Service connected");
                    break;

                /*
                 * SendMockLocationService reported that the location client disconnected. This
                 * happens if Location Services drops the connection. Update the app status and the
                 * connection status reporting fields in the UI.
                 */
                case LocationUtils.CODE_DISCONNECTED:
                    L.e(TAG + " Service disConnected");
                    break;

                /*
                 * SendMockLocationService reported that an attempt to connect to Location
                 * Services failed. Testing can't continue. The Service has already stopped itself.
                 * Update the connection status reporting field and include the error code.
                 * Also update the app status field
                 */
                case LocationUtils.CODE_CONNECTION_FAILED:
                    L.e(TAG + " Service failed");
                    break;

                /*
                 * SendMockLocationService reported that the tester requested a test, but a test
                 * is already underway. Update the app status reporting field.
                 */
                case LocationUtils.CODE_IN_TEST:
                    L.e(TAG + " Service continuous testing");
                    break;

                /*
                 * SendMockLocationService reported that the test run finished. Turn off the
                 * progress indicator, update the app status reporting field and the connection
                 * status reporting field. Since this message can only occur if
                 * SendMockLocationService disconnected the client, the connection status is
                 * "disconnected".
                 */
                case LocationUtils.CODE_TEST_FINISHED:
                    L.e(TAG + " Service finished");
                    break;

                /*
                 * SendMockLocationService reported that the tester interrupted the test.
                 * Turn off the activity indicator and update the app status reporting field.
                 */
                case LocationUtils.CODE_TEST_STOPPED:
                    L.e(TAG + " Service stopped");
                    break;

                /*
                 * An unknown broadcast Intent was received. Log an error.
                 */
                default:
                    break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
