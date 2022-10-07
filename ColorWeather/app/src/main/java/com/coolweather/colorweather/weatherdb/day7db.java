package com.coolweather.colorweather.weatherdb;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

public class day7db extends LitePalSupport {

    public String countyName;
    public String temperatureMax;
    public String temperatureMin;
    public String date;
    public String weatherSituation;
    public String weatherImage;

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(String temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public String getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(String temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeatherSituation() {
        return weatherSituation;
    }

    public void setWeatherSituation(String weatherSituation) {
        this.weatherSituation = weatherSituation;
    }

    public String getWeatherImage() {
        return weatherImage;
    }

    public void setWeatherImage(String weatherImage) {
        this.weatherImage = weatherImage;
    }
}
