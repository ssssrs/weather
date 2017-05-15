package com.example.administrator.weatherforecast.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wu873191083 on 2017/5/15.
 */

public class Basic {

    @SerializedName("city")//使用Gson解析的时候直接把json数据里的“city”对应的字符串赋值给cityName,
                            // 不用再写一个具体的类去一一对应
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public UpDate update;

    public class UpDate{
        @SerializedName("loc")
        public String updateTime;
    }
}
