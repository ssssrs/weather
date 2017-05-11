package com.example.administrator.weatherforecast.User;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2017/4/26.
 */

public class MyApplication extends Application {

    private static RequestQueue mQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mQueue= Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getRequestQueue(){

        return mQueue;
    }
}
