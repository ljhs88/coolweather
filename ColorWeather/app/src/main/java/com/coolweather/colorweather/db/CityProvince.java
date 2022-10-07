package com.coolweather.colorweather.db;

import org.litepal.crud.LitePalSupport;

public class CityProvince extends LitePalSupport {

    // 记录实体类编号
    private int id;
    // 记录城市名
    private String cityName;
    // 记录省名
    private String provinceName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
