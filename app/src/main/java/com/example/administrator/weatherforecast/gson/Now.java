package com.example.administrator.weatherforecast.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wu873191083 on 2017/5/15.
 */

public class Now {
    @SerializedName("temp")
    public String temperature;

    @SerializedName("cond")
    public  More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }
}
