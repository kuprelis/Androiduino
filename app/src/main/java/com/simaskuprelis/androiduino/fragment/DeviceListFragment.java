package com.simaskuprelis.androiduino.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.simaskuprelis.androiduino.activity.JoystickActivity;

import java.util.ArrayList;

public class DeviceListFragment extends ListFragment {
    private ArrayList<BluetoothDevice> mDevices;

    public static DeviceListFragment newInstance() {
        return new DeviceListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        mDevices = new ArrayList<>();
        mDevices.addAll(adapter.getBondedDevices());
        setListAdapter(new DeviceAdapter(mDevices));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent i = new Intent(getActivity(), JoystickActivity.class);
        i.putExtra(JoystickActivity.EXTRA_DEVICE, mDevices.get(position));
        startActivity(i);
    }

    private class DeviceAdapter extends ArrayAdapter<BluetoothDevice> {
        public DeviceAdapter(ArrayList<BluetoothDevice> devices) {
            super(getActivity(), 0, devices);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = getActivity().getLayoutInflater()
                        .inflate(android.R.layout.simple_list_item_1, null);
            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(mDevices.get(position).getName());
            return convertView;
        }
    }
}
