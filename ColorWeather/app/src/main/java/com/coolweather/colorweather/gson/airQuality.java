package com.coolweather.colorweather.gson;

import com.google.gson.annotations.SerializedName;

public class airQuality {

    @SerializedName("now")
    public now now;

    public static class now {
        @SerializedName("aqi")
        public String aqi;
        @SerializedName("category")
        public String category;
        @SerializedName("pm10")
        public String pm10;
        @SerializedName("pm2p5")
        public String pm2p5;
        @SerializedName("no2")
        public String no2;
        @SerializedName("so2")
        public String so2;
        @SerializedName("co")
        public String co;
        @SerializedName("o3")
        public String o3;
    }
}
