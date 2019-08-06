package com.example.administrator.study;

import android.content.Context;
import android.content.Intent;


import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;



import javax.annotation.Nonnull;

public class FunctionModule extends ReactContextBaseJavaModule {

    private Context mContext;

    public FunctionModule(@Nonnull ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @Nonnull
    @Override
    public String getName() {
        return "StartVoice";
    }


    @ReactMethod
    public void show() {
        Intent jumpIntent = new Intent(mContext, MainActivity.class);
        jumpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        mContext.startActivity(jumpIntent);
    }
}
