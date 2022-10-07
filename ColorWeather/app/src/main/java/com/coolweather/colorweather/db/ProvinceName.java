package com.coolweather.colorweather.db;

import org.litepal.crud.LitePalSupport;

import java.util.Objects;

public class ProvinceName extends LitePalSupport {
    // 记录实体类编号
    private int id;
    // 记录省名
    private String provinceName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProvinceName that = (ProvinceName) o;
        return id == that.id &&
                Objects.equals(provinceName, that.provinceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, provinceName);
    }
}
