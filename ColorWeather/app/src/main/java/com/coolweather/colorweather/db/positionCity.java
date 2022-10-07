package com.coolweather.colorweather.db;

import org.litepal.crud.LitePalSupport;

public class positionCity extends LitePalSupport {

    private int id;

    private String countyName;

    private int locationId;

    private Boolean first;

    private Boolean second;

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

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getSecond() {
        return second;
    }

    public void setSecond(Boolean second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "positionCity{" +
                "id=" + id +
                ", countyName='" + countyName + '\'' +
                ", locationId=" + locationId +
                ", first=" + first +
                ", second=" + second +
                '}';
    }
}
