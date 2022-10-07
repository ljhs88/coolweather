package com.coolweather.colorweather.db;

import org.litepal.crud.LitePalSupport;

public class CityLocationId extends LitePalSupport {
    // 记录实体类编号
    private int id;
    // 记录城市LocationId
    private int locationId;
    // 记录县名
    private String countyName;
    // 记录市名
    private String cityName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    @Override
    public String toString() {
        return "CityLocationId{" +
                "id=" + id +
                ", locationId=" + locationId +
                ", countyName='" + countyName + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
