package com.dolphin.android.dpfx;

import android.location.Location;

import com.dolphin.android.dpfx.bean.FxLocation;

/**
 * Created by Administrator on 2/13/14.
 */
public interface FxConstants {
    public static final boolean releaseMode = false;
    public static final FxLocation DEFAULT_LOCATION = new FxLocation(21.033333, 105.85);
    public static final int DEFAULT_ZOOM_LEVEL = 17;
    public static final int DEFAULT_ANIMATE_TIME = 2000;
}
