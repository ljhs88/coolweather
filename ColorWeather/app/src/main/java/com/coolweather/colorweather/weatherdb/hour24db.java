package com.coolweather.colorweather.weatherdb;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

public class hour24db extends LitePalSupport {

    public String countyName;
    public String nowTime;
    public String temperature;
    public String rainPercent;
    public String weatherImage;

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getRainPercent() {
        return rainPercent;
    }

    public void setRainPercent(String rainPercent) {
        this.rainPercent = rainPercent;
    }

    public String getWeatherImage() {
        return weatherImage;
    }

    public void setWeatherImage(String weatherImage) {
        this.weatherImage = weatherImage;
    }
}
