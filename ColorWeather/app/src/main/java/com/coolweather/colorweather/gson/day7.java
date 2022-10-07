package com.coolweather.colorweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class day7 {

    @SerializedName("daily")
    public List<daily> dailyList;

    public static class daily {
        @SerializedName("tempMax")
        public String temperatureMax;
        @SerializedName("tempMin")
        public String temperatureMin;
        @SerializedName("fxDate")
        public String date;
        @SerializedName("textDay")
        public String weatherSituation;
        @SerializedName("iconDay")
        public String weatherImage;
    }

}
