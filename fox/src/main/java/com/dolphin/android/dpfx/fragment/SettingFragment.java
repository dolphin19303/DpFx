package com.dolphin.android.dpfx.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dolphin.android.dpfx.FxBaseFragment;
import com.dolphin.android.dpfx.R;

/**
 * Created by Administrator on 2/18/14.
 */
public class SettingFragment extends FxBaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        return view;
    }
}
