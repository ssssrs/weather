package com.example.administrator.weatherforecast.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wu873191083 on 2017/5/15.
 */

public class Aqi {

    public  AqiCity city;

    public class AqiCity{
        @SerializedName("aqi")
        public String aqi;

        @SerializedName("pm25")
        public String pm25;
    }
}
