package com.coolweather.colorweather.gson;

import com.google.gson.annotations.SerializedName;

public class nowTime {

    @SerializedName("now")
    public now now;

    public static class now {
        @SerializedName("temp")
        public String temperatureAve;
        @SerializedName("feelsLike")
        public String bodyTemperature;
        @SerializedName("text")
        public String weatherQuality;
        @SerializedName("windDir")
        public String cloudDirection;
        @SerializedName("windScale")
        public String cloudForce;
        @SerializedName("humidity")
        public String humidity;
    }
}
