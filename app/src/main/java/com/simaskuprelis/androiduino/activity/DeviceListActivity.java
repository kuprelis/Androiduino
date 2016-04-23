package com.simaskuprelis.androiduino.activity;

import android.support.v4.app.Fragment;

import com.simaskuprelis.androiduino.fragment.DeviceListFragment;

public class DeviceListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return DeviceListFragment.newInstance();
    }
}
