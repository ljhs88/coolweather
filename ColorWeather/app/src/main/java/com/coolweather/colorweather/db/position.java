package com.coolweather.colorweather.db;

import org.litepal.crud.LitePalSupport;

public class position extends LitePalSupport {

    private int id;

    private String countyName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }
}
