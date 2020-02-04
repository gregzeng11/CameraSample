package com.example.android.library.cameraview.model;

import android.app.Activity;
import android.support.v4.app.Fragment;


public class TContextWrap {
    private Activity activity;
    private Fragment fragment;

    public static TContextWrap of(Activity activity) {
        return new TContextWrap(activity);
    }

    public static TContextWrap of(Fragment fragment) {
        return new TContextWrap(fragment);
    }

    private TContextWrap(Activity activity) {
        this.activity = activity;
    }

    private TContextWrap(Fragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
