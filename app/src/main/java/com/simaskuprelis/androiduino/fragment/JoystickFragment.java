package com.simaskuprelis.androiduino.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simaskuprelis.androiduino.Joystick;
import com.simaskuprelis.androiduino.R;

public class JoystickFragment extends Fragment {
    private static final String TAG = "JoystickFragment";

    private Joystick mJoystick;
    private Callbacks mCallbacks;

    public static JoystickFragment newInstance() {
        return new JoystickFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            mCallbacks = (Callbacks) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_joystick, null);
        mJoystick = (Joystick) v.findViewById(R.id.joystick);
        mJoystick.setOnMoveListener(new Joystick.onMoveListener() {
            @Override
            public void onMove(int x, int y) {
                mCallbacks.onMove(x, y);
            }
        });
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public interface Callbacks {
        void onMove(int x, int y);
    }
}
