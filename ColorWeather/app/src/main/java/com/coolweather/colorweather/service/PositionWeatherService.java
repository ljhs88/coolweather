package com.coolweather.colorweather.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.coolweather.colorweather.gson.airQuality;
import com.coolweather.colorweather.gson.day7;
import com.coolweather.colorweather.gson.hour24;
import com.coolweather.colorweather.gson.nowTime;
import com.coolweather.colorweather.gson.weatherLife;
import com.coolweather.colorweather.util.HttpUtil;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.coolweather.colorweather.weatherdb.*;

import org.litepal.LitePal;
import com.coolweather.colorweather.db.*;

public class PositionWeatherService extends Service implements AMapLocationListener {

    //请求权限码
    private static final int REQUEST_PERMISSIONS = 9527;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private static int key = 0;

    private Context context;

    private List<positionCity> locationIdList;

    public PositionWeatherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("123", "Service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("123", "Service onStartCommand");
        // 初始化定位
        initLocation();
        // 开始定位
        mLocationClient.startLocation();
        // 加载天气
        locationIdList = LitePal.limit(10).find(positionCity.class);
        getWeather();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent("com.coolweather.colorweather");
        sendBroadcast(intent);
        Log.d("123", "Service onDestroy");
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制，高精度定位会产生缓存。
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    /**
     * 接收异步返回的定位结果
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                // 地址
                //String address = aMapLocation.getAddress();
                // 区县
                String countyName = aMapLocation.getDistrict();
                Log.d("123", countyName);
                LitePal.deleteAll(position.class);
                position position = new position();
                position.setCountyName(countyName);
                position.save();
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
            // 销毁本地服务
            //mLocationClient.onDestroy();
        }
    }

    private void getWeather() {
        // 空气质量
        getAirQualityWeather();
        // 实时天气
        getNowTimeWeather();
        // 天气生活
        getLifeWeather();
        // 逐小时
        getHourly();
        // 逐天
        getDaily();
    }

    private void getDaily() {
        for (positionCity city : locationIdList) {
            int LocationId = city.getLocationId();
            String countyName = city.getCountyName();
            String url = "https://devapi.qweather.com/v7/weather/7d?location="+
                    LocationId+"&key=9b399ace380d4928b51b492839d86b37";
            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String dailyContent = response.body().string();
                    Log.d("123", "Service" + dailyContent);
                    Gson gson = new Gson();
                    day7 day7 = gson.fromJson(dailyContent, day7.class);
                    // 清空
                    LitePal.deleteAll(day7db.class,"countyName = ?", countyName);
                    for (day7.daily day : day7.dailyList) {
                        day7db db = new day7db();
                        db.setCountyName(countyName);
                        db.setDate(day.date);
                        db.setTemperatureMax(day.temperatureMax);
                        db.setTemperatureMin(day.temperatureMin);
                        db.setWeatherImage(day.weatherImage);
                        db.setWeatherSituation(day.weatherSituation);
                        db.save();
                    }
                }
            });
        }
    }

    private void getHourly() {
        for (positionCity city : locationIdList) {
            int LocationId = city.getLocationId();
            String countyName = city.getCountyName();
            String url = "https://devapi.qweather.com/v7/weather/24h?location=" +
                    LocationId + "&key=9b399ace380d4928b51b492839d86b37";
            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String hourlyContent = response.body().string();
                    Log.d("123", "Service" + hourlyContent);
                    Gson gson = new Gson();
                    hour24 hour24 = gson.fromJson(hourlyContent, hour24.class);
                    // 清空
                    LitePal.deleteAll(hour24db.class,"countyName = ?", countyName);
                    for (hour24.hourly hourly : hour24.hourlyList) {
                        hour24db db = new hour24db();
                        db.setCountyName(countyName);
                        db.setNowTime(hourly.nowTime);
                        db.setRainPercent(hourly.rainPercent);
                        db.setTemperature(hourly.temperature);
                        db.setWeatherImage(hourly.weatherImage);
                        db.save();
                    }
                }
            });
        }
    }

    public void getAirQualityWeather() {
        for (positionCity city : locationIdList) {
            int LocationId = city.getLocationId();
            String countyName = city.getCountyName();
            String url = "https://devapi.qweather.com/v7/air/now?location="
                    + LocationId + "&key=9b399ace380d4928b51b492839d86b37";
            HttpUtil.sendOkHttpRequest(url, new okhttp3.Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String airQualityContent = response.body().string();
                    Log.d("123", "Service" + airQualityContent);
                    Gson gson = new Gson();
                    airQuality airQuality = gson.fromJson(airQualityContent, airQuality.class);
                    // 先清空上次天气信息
                    LitePal.deleteAll(airQualitydb.class,"countyName = ?", countyName);
                    // 存入本次天气信息
                    airQualitydb db = new airQualitydb();
                    db.setCountyName(countyName);
                    db.setAqi(airQuality.now.aqi);
                    db.setCategory(airQuality.now.category);
                    db.setCo(airQuality.now.co);
                    db.setNo2(airQuality.now.no2);
                    db.setO3(airQuality.now.o3);
                    db.setPm2p5(airQuality.now.pm2p5);
                    db.setPm10(airQuality.now.pm10);
                    db.setSo2(airQuality.now.so2);
                    db.save();
                }
            });
        }
    }

    public void getNowTimeWeather() {
        for (positionCity city : locationIdList) {
            int LocationId = city.getLocationId();
            String countyName = city.getCountyName();
            String url = "https://devapi.qweather.com/v7/weather/now?location=" +
                    LocationId + "&key=9b399ace380d4928b51b492839d86b37";
            HttpUtil.sendOkHttpRequest(url, new Callback() {
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String nowTimeContent = response.body().string();
                    Log.d("123", "Service" + nowTimeContent);
                    Gson gson = new Gson();
                    nowTime nowTime = gson.fromJson(nowTimeContent, nowTime.class);
                    // 清空
                    LitePal.deleteAll(nowTimedb.class,"countyName = ?", countyName);
                    nowTimedb db = new nowTimedb();
                    db.setCountyName(countyName);
                    db.setBodyTemperature(nowTime.now.bodyTemperature);
                    db.setCloudDirection(nowTime.now.cloudDirection);
                    db.setCloudForce(nowTime.now.cloudForce);
                    db.setHumidity(nowTime.now.humidity);
                    db.setTemperatureAve(nowTime.now.temperatureAve);
                    db.setWeatherQuality(nowTime.now.weatherQuality);
                    db.save();
                }
            });
        }
    }

    public void getLifeWeather() {
        for (positionCity city : locationIdList) {
            int LocationId = city.getLocationId();
            String countyName = city.getCountyName();
            String url = "https://devapi.qweather.com/v7/indices/1d?type=1,2,3,4,5,9&" +
                    "location=" + LocationId + "&key=9b399ace380d4928b51b492839d86b37";
            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String weatherLifeContent = response.body().string();
                    Log.d("123", "Service" + weatherLifeContent);
                    Gson gson = new Gson();
                    weatherLife weatherLife = gson.fromJson(weatherLifeContent, weatherLife.class);
                    LitePal.deleteAll(weatherLifedb.class, "countyName = ?", countyName);
                    for (weatherLife.daily daily : weatherLife.dailyList) {
                        weatherLifedb db = new weatherLifedb();
                        db.setCountyName(countyName);
                        db.setLifeText(daily.lifeText);
                        db.setWeatherText(daily.weatherText);
                        db.save();
                    }
                }
            });
        }
    }

}