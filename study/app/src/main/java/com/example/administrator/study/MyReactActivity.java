package com.example.administrator.study;

import androidx.annotation.Nullable;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;

public class MyReactActivity extends ReactActivity {

    @Nullable
    @Override
    protected String getMainComponentName() {
        return "study";
    }

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        return new ReactActivityDelegate(this,getMainComponentName());
    }

}
