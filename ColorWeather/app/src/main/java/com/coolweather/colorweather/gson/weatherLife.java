package com.coolweather.colorweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class weatherLife {

    @SerializedName("daily")
    public List<daily> dailyList;

    public static class daily {
        @SerializedName("category")
        public String lifeText;

        @SerializedName("text")
        public String weatherText;
    }
}
