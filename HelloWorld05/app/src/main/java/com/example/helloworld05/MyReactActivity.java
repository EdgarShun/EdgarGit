package com.example.helloworld05;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;

public class MyReactActivity extends ReactActivity {

//    public static void start(Context context) {
//        Intent intent = new Intent(context, MyReactActivity.class);
//        context.startActivity(intent);
//    }

    @Nullable
    @Override
    protected String getMainComponentName() {
        return "HelloWorld05";
    }

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        return new ReactActivityDelegate(this,getMainComponentName());
    }

}
