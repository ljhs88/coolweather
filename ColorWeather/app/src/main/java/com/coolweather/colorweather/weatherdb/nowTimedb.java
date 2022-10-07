package com.coolweather.colorweather.weatherdb;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

public class nowTimedb extends LitePalSupport {

    public String countyName;
    public String temperatureAve;
    public String bodyTemperature;
    public String weatherQuality;
    public String cloudDirection;
    public String cloudForce;
    public String humidity;

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getTemperatureAve() {
        return temperatureAve;
    }

    public void setTemperatureAve(String temperatureAve) {
        this.temperatureAve = temperatureAve;
    }

    public String getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(String bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public String getWeatherQuality() {
        return weatherQuality;
    }

    public void setWeatherQuality(String weatherQuality) {
        this.weatherQuality = weatherQuality;
    }

    public String getCloudDirection() {
        return cloudDirection;
    }

    public void setCloudDirection(String cloudDirection) {
        this.cloudDirection = cloudDirection;
    }

    public String getCloudForce() {
        return cloudForce;
    }

    public void setCloudForce(String cloudForce) {
        this.cloudForce = cloudForce;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
