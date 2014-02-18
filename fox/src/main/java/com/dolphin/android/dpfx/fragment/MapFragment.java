package com.dolphin.android.dpfx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dolphin.android.dpfx.FxBaseFragment;
import com.dolphin.android.dpfx.R;

/**
 * Created by Administrator on 2/18/14.cls
 *
 */
public class MapFragment extends FxBaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }
}
