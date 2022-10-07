package com.coolweather.colorweather.util;

import android.content.Context;
import android.util.Log;

import com.coolweather.colorweather.db.CityLocationId;
import com.coolweather.colorweather.db.CityProvince;
import com.coolweather.colorweather.db.ProvinceName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class savePC {

    private static Set<String> provinceSet;
    private static Set<String> citySet;

    public static void saveFile(Context context) {
        BufferedReader reader = null;
        provinceSet = new HashSet<>();
        citySet = new HashSet<>();
        Log.d("123", "save begin");
        try {
            // 读取文件
            InputStream inputStream = context.getAssets().open("file.txt");
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String contentLine = "";
            while ((contentLine = reader.readLine()) != null) {
                String[] token = contentLine.split(",");
                provinceSet.add(token[7]);
                citySet.add(token[7]+","+token[9]);
                //Log.d("123", token[0]);
                //Log.d("123", token[2]);
                CityLocationId city = new CityLocationId();

                city.setLocationId(Integer.parseInt(token[0]));
                city.setCountyName(token[2]);
                city.setCityName(token[9]);

                city.save();
            }
            saveProvince();
            saveCity();
            Log.d("123", "Save finish");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void saveProvince() {
        Log.d("123", provinceSet.toString());
        for (String provinceName : provinceSet) {
            ProvinceName province = new ProvinceName();
            province.setProvinceName(provinceName);
            province.save();
        }
    }

    private static void saveCity() {
        Log.d("123", citySet.toString());
        for (String cityName : citySet) {
            String[] token = cityName.split(",");
            CityProvince cityProvince = new CityProvince();
            cityProvince.setCityName(token[1]);
            cityProvince.setProvinceName(token[0]);
            cityProvince.save();
        }
    }

}
