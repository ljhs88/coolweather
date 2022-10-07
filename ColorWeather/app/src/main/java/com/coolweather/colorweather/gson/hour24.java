package com.coolweather.colorweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class hour24 {

    @SerializedName("hourly")
    public List<hourly> hourlyList;

    public static class hourly {
        @SerializedName("fxTime")
        public String nowTime;

        @SerializedName("temp")
        public String temperature;

        @SerializedName("pop")
        public String rainPercent;

        @SerializedName("icon")
        public String weatherImage;
    }

}
