package com.coolweather.colorweather.weatherdb;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

public class weatherLifedb extends LitePalSupport {

    public String countyName;

    public String lifeText;

    public String weatherText;

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getLifeText() {
        return lifeText;
    }

    public void setLifeText(String lifeText) {
        this.lifeText = lifeText;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }
}
