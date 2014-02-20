package com.dolphin.android.dpfx;

import android.app.Application;

import com.dolphin.android.dpfx.core.CoreLocation;
import com.dolphin.android.dpfx.utils.LayoutUtil;
import com.dolphin.android.dpfx.utils.T;

/**
 * Created by Administrator on 2/13/14.
 */
public class FxApplication extends Application {
    private static CoreLocation mCoreLocation;

    @Override

    public void onCreate() {
        super.onCreate();
        T.init(getApplicationContext());
        LayoutUtil.init(getApplicationContext());
        mCoreLocation = new CoreLocation(this);
    }

    public static CoreLocation getLocationManager() {
        return mCoreLocation;
    }
}
