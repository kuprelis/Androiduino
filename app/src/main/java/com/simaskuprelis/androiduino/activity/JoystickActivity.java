package com.simaskuprelis.androiduino.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.simaskuprelis.androiduino.R;
import com.simaskuprelis.androiduino.fragment.JoystickFragment;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class JoystickActivity extends SingleFragmentActivity implements JoystickFragment.Callbacks {
    private static final String TAG = "JoystickActivity";
    public static final String EXTRA_DEVICE = "com.simaskuprelis.androiduino.device";
    private static final UUID BTID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothSocket mSocket;
    private BluetoothDevice mDevice;

    @Override
    protected Fragment createFragment() {
        return JoystickFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDevice = getIntent().getParcelableExtra(EXTRA_DEVICE);
        getSupportActionBar().setTitle(mDevice.getName());
        mSocket = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ConnectTask().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSocket != null) try {
            mSocket.close();
        } catch (IOException ioe) {
            Log.e(TAG, "Error closing socket: " + ioe);
        }
    }

    @Override
    public void onMove(int x, int y) {
        if (mSocket == null || !mSocket.isConnected()) return;
        String msg = x + " " + y + " ";
        try {
            OutputStream os = mSocket.getOutputStream();
            os.write(msg.getBytes());
        } catch (IOException ioe) {
            Log.e(TAG, "Error sending message: " + ioe);
        }
    }

    private class ConnectTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                mSocket = mDevice.createRfcommSocketToServiceRecord(BTID);
                mSocket.connect();
            } catch (IOException ioe) {
                Log.e(TAG, "Error connecting: " + ioe);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            String text;
            if (mSocket != null && mSocket.isConnected())
                text = getString(R.string.connected);
            else
                text = getString(R.string.connection_fail);
            Snackbar.make(getWindow().getDecorView(), text, Snackbar.LENGTH_SHORT).show();
        }
    }
}
