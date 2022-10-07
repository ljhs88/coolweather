package com.coolweather.colorweather;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.coolweather.colorweather.db.positionCity;
import com.coolweather.colorweather.gson.airQuality;
import com.coolweather.colorweather.gson.day7;
import com.coolweather.colorweather.gson.hour24;
import com.coolweather.colorweather.gson.nowTime;
import com.coolweather.colorweather.gson.weatherLife;
import com.coolweather.colorweather.util.HttpUtil;
import com.coolweather.colorweather.Adapter.DailyAdapter;
import com.coolweather.colorweather.Adapter.HourlyAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import com.coolweather.colorweather.weatherdb.*;

public class weatherAreaFragment extends Fragment {

    // LocationId and cityName
    private String locationId;
    private String cityName;

    // 空气质量
    private TextView airSituationText;
    private TextView airPercentText;
    private TextView pm10Text;
    private TextView pm2p5Text;
    private TextView no2Text;
    private TextView so2Text;
    private TextView o3Text;
    private TextView coText;
    private TextView airText;

    // 实时天气
    private TextView temperatureAveText;
    private TextView bodyTemperatureText;
    private TextView cloudForceText;
    private TextView cloudDirectionText;
    private TextView weatherQualityText;
    private TextView humidityText;

    // 天气生活
    private TextView clothSituation;
    private TextView sportSituation;
    private TextView medicine;
    private TextView uv;
    private TextView washcar;
    private TextView fish;

    // 最高低温度
    private TextView tempMaxMin;

    // 下拉刷新
    private SwipeRefreshLayout swipeRefresh;

    // adapter
    private HourlyAdapter adapterHourly;
    private DailyAdapter adapterDaily;

    // DrawerLayout
    private DrawerLayout drawerLayout;

    // AlterDialog
    public static AlertDialog.Builder dialog;

    // Activity
    private weatherActivity activity;

    // View
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("123", "onCreateView");
        Bundle bundle = getArguments();
        Log.d("123", "onCreateViewbefore"+cityName + locationId);
        locationId = bundle.getString("LocationId");
        cityName = bundle.getString("cityName");
        Log.d("123", "onCreateViewafter"+cityName + locationId);
        bundle = null;
        view = inflater.inflate(R.layout.weather_fragment, container, false);
        activity = (weatherActivity) getActivity();
        // 对话框
        dialogMethod();

        List<positionCity> positionCityList = LitePal.findAll(positionCity.class);

        // 添加活动
        ActivityCollector.addActivity(getActivity());

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.city:
                        Intent intent = new Intent(activity, positionActivity.class);
                        intent.putExtra("cityName", cityName);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        drawerLayout = (DrawerLayout) view.findViewById(R.id.draw_layout);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.position);
        }
        /*Bundle bundle = getArguments();
        //Log.d("123", "onCreateViewbefore"+cityName + locationId);
        locationId = bundle.getString("LocationId");
        cityName = bundle.getString("cityName");
        //Log.d("123", "onCreateViewafter"+cityName + locationId);*/

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(cityName);

        // 空气质量
        airSituationText = (TextView) view.findViewById(R.id.air_situation);
        airPercentText = (TextView) view.findViewById(R.id.air_percent);
        pm10Text = (TextView) view.findViewById(R.id.PM10);
        pm2p5Text = (TextView) view.findViewById(R.id.PM2_5);
        no2Text = (TextView) view.findViewById(R.id.NO2);
        so2Text = (TextView) view.findViewById(R.id.SO2);
        o3Text = (TextView) view.findViewById(R.id.O3);
        coText = (TextView) view.findViewById(R.id.CO);
        airText = (TextView) view.findViewById(R.id.air_quality);

        // 实时天气
        temperatureAveText = (TextView) view.findViewById(R.id.temperature_ave);
        weatherQualityText = (TextView) view.findViewById(R.id.weather_quality);
        bodyTemperatureText = (TextView) view.findViewById(R.id.body_temperature_text);
        cloudForceText = (TextView) view.findViewById(R.id.cloud_force_text);
        cloudDirectionText = (TextView) view.findViewById(R.id.cloud_direction_text);
        humidityText = (TextView) view.findViewById(R.id.humidity_text);

        // 天气生活
        clothSituation = (TextView) view.findViewById(R.id.cloth_situation_text);
        sportSituation = (TextView) view.findViewById(R.id.sport_situation_text);
        medicine = (TextView) view.findViewById(R.id.medicine_text);
        uv = (TextView) view.findViewById(R.id.uv_text);
        washcar = (TextView) view.findViewById(R.id.washcar_text);
        fish = (TextView) view.findViewById(R.id.fish_text);

        // 最高低温度
        tempMaxMin = (TextView) view.findViewById(R.id.temperature_max_min);

        // 获取天气
        getWeather(locationId);

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        swipeRefresh.setColorSchemeResources(R.color.black);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWeatherThread(locationId);
            }
        });
        return view;
    }

    private void dialogMethod() {
        dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("提示：");
        dialog.setMessage("您未联网，请点击OK退出，联网后进入.");
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar, menu);
    }

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);// GravityCompat.START为了保证这里的行为和xml中定义的一样
                break;
        }
        return true;
    }*/

    private void getWeather(String locationId) {
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                // 空气质量
                //getAirQualityWeather(locationId);
                setAirQualityTextFromDb();
                // 实时天气
                //getNowTimeWeather(locationId);
                setNowTimeTextFromDb();
                // 天气生活
                //getLifeWeather(locationId);
                setWeatherLifeTextFromDb();
                // 逐小时
                //getHourly(locationId, getContext());
                setAdapterHourlyFromDb();
                // 逐天
                //getDaily(locationId, getContext());
                setAdapterDailyFromDb();
            }
        }).start();*/
        // 空气质量
        setAirQualityTextFromDb();
        // 实时天气
        setNowTimeTextFromDb();
        // 天气生活
        setWeatherLifeTextFromDb();
        // 逐小时
        setAdapterHourlyFromDb();
        // 逐天
        setAdapterDailyFromDb();
    }

    private void getWeatherThread(String locationId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 空气质量
                        setAirQualityTextFromDb();
                        // 实时天气
                        setNowTimeTextFromDb();
                        // 天气生活
                        setWeatherLifeTextFromDb();
                        // 逐小时
                        setAdapterHourlyFromDb();
                        // 逐天
                        setAdapterDailyFromDb();
                        /*// 空气质量
                        getAirQualityWeather(locationId);
                        // 实时天气
                        getNowTimeWeather(locationId);
                        // 天气生活
                        getLifeWeather(locationId);
                        // 逐小时
                        getHourly(locationId, getContext());
                        // 逐天
                        getDaily(locationId, getContext());*/
                        // 更新一下recyclerView
                        //adapterHourly.notifyDataSetChanged();
                        //adapterDaily.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void setAirQualityTextFromDb() {
        List<airQualitydb> airQuality = LitePal
                .where("countyName = ?", cityName)
                .limit(1)
                .find(airQualitydb.class);
        String aqi = airQuality.get(0).getAqi();
        String category = airQuality.get(0).getCategory();
        String pm10 = airQuality.get(0).getPm10();
        String pm2p5 = airQuality.get(0).getPm2p5();
        String no2 = airQuality.get(0).getNo2();
        String so2 = airQuality.get(0).getSo2();
        String co = airQuality.get(0).getCo();
        String o3 = airQuality.get(0).getO3();
        airText.setText(" 空气" + category);
        airSituationText.setText(category);
        airPercentText.setText(aqi);
        pm10Text.setText(pm10);
        pm2p5Text.setText(pm2p5);
        no2Text.setText(no2);
        so2Text.setText(so2);
        coText.setText(co);
        o3Text.setText(o3);
    }

    private void setNowTimeTextFromDb() {
        List<nowTimedb> nowTime = LitePal
                .where("countyName = ?", cityName)
                .limit(1)
                .find(nowTimedb.class);
        temperatureAveText.setText(nowTime.get(0).getTemperatureAve()+"℃");
        weatherQualityText.setText(nowTime.get(0).getWeatherQuality());
        cloudForceText.setText(nowTime.get(0).getCloudForce());
        cloudDirectionText.setText(nowTime.get(0).getCloudDirection());
        bodyTemperatureText.setText(nowTime.get(0).getBodyTemperature());
        humidityText.setText(nowTime.get(0).getHumidity()+"%");
    }

    private void setWeatherLifeTextFromDb() {
        List<weatherLifedb> dailyList = LitePal
                .where("countyName = ?", cityName)
                .limit(6)
                .find(weatherLifedb.class);
        uv.setText(dailyList.get(0).lifeText);
        washcar.setText(dailyList.get(1).lifeText);
        sportSituation.setText(dailyList.get(2).lifeText);
        clothSituation.setText(dailyList.get(3).lifeText);
        fish.setText(dailyList.get(4).lifeText);
        medicine.setText(dailyList.get(5).lifeText);
    }

    public void setAdapterHourlyFromDb() {
        List<hour24db> hour24dbList = LitePal
                .where("countyName = ?", cityName)
                .limit(24)
                .find(hour24db.class);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.hour_future);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        adapterHourly = new HourlyAdapter(hour24dbList, getContext());
        recyclerView.setAdapter(adapterHourly);
        adapterHourly.notifyDataSetChanged();
    }

    public void setAdapterDailyFromDb() {
        List<day7db> dailyList = LitePal
                .where("countyName = ?", cityName)
                .limit(7)
                .find(day7db.class);
        tempMaxMin.setText(dailyList.get(0).getTemperatureMax()+ "℃/"
                + dailyList.get(0).getTemperatureMin()+"℃");
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.day_future);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        adapterDaily = new DailyAdapter(dailyList, getContext());
        recyclerView.setAdapter(adapterDaily);
        adapterDaily.notifyDataSetChanged();
    }

    // -----------------------------------------------------------------------------------------------

    private void getDaily(String LocationId, Context context) {
        String url = "https://devapi.qweather.com/v7/weather/7d?location="+
                LocationId+"&key=9b399ace380d4928b51b492839d86b37";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String dailyContent = response.body().string();
                //Log.d("123", dailyContent);
                Gson gson = new Gson();
                day7 day7 = gson.fromJson(dailyContent, day7.class);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*tempMaxMin.setText(day7.dailyList.get(0).temperatureMax+ "℃/"
                                + day7.dailyList.get(0).temperatureMin+"℃");
                        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.day_future);
                        LinearLayoutManager manager = new LinearLayoutManager(context);
                        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        recyclerView.setLayoutManager(manager);
                        adapterDaily = new DailyAdapter(day7.dailyList, context);
                        recyclerView.setAdapter(adapterDaily);
                        adapterDaily.notifyDataSetChanged();*/
                    }
                });
            }
        });
    }

    private void getHourly(String LocationId, Context context) {
        String url = "https://devapi.qweather.com/v7/weather/24h?location=" +
                LocationId+ "&key=9b399ace380d4928b51b492839d86b37";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String hourlyContent = response.body().string();
                //Log.d("123", hourlyContent);
                Gson gson = new Gson();
                hour24 hour24 = gson.fromJson(hourlyContent, hour24.class);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.hour_future);
                        LinearLayoutManager manager = new LinearLayoutManager(context);
                        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        recyclerView.setLayoutManager(manager);
                        adapterHourly = new HourlyAdapter(hour24.hourlyList, context);
                        recyclerView.setAdapter(adapterHourly);
                        adapterHourly.notifyDataSetChanged();*/
                    }
                });
            }
        });
    }

    public void getAirQualityWeather(String LocationId) {
        String url = "https://devapi.qweather.com/v7/air/now?location="
                + LocationId + "&key=9b399ace380d4928b51b492839d86b37";
        HttpUtil.sendOkHttpRequest(url, new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String airQualityContent = response.body().string();
                //Log.d("123", airQualityContent);
                Gson gson = new Gson();
                airQuality airQuality = gson.fromJson(airQualityContent, airQuality.class);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAirQualityText(airQuality);
                    }
                });
            }
        });
    }

    public void getNowTimeWeather(String LocationId) {
        String url = "https://devapi.qweather.com/v7/weather/now?location=" +
                LocationId + "&key=9b399ace380d4928b51b492839d86b37";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String nowTimeContent = response.body().string();
                //Log.d("123", nowTimeContent);
                Gson gson = new Gson();
                nowTime nowTime = gson.fromJson(nowTimeContent, nowTime.class);
                Log.d("123",nowTime.now.bodyTemperature);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setNowTimeText(nowTime);
                    }
                });
            }
        });
    }

    public void getLifeWeather(String LocationId) {
        String url = "https://devapi.qweather.com/v7/indices/1d?type=1,2,3,4,5,9&" +
                "location=" + LocationId + "&key=9b399ace380d4928b51b492839d86b37";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String weatherLifeContent = response.body().string();
                //Log.d("123", weatherLifeContent);
                Gson gson = new Gson();
                weatherLife weatherLife = gson.fromJson(weatherLifeContent, weatherLife.class);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setWeatherLifeText(weatherLife);
                    }
                });
            }
        });
    }



    private void setWeatherLifeText(weatherLife weatherLife) {
        uv.setText(weatherLife.dailyList.get(0).lifeText);
        washcar.setText(weatherLife.dailyList.get(1).lifeText);
        sportSituation.setText(weatherLife.dailyList.get(2).lifeText);
        clothSituation.setText(weatherLife.dailyList.get(3).lifeText);
        fish.setText(weatherLife.dailyList.get(4).lifeText);
        medicine.setText(weatherLife.dailyList.get(5).lifeText);
    }

    private void setNowTimeText(nowTime nowTime) {
        temperatureAveText.setText(nowTime.now.temperatureAve+"℃");
        weatherQualityText.setText(nowTime.now.weatherQuality);
        cloudForceText.setText(nowTime.now.cloudForce);
        cloudDirectionText.setText(nowTime.now.cloudDirection);
        bodyTemperatureText.setText(nowTime.now.bodyTemperature);
        humidityText.setText(nowTime.now.humidity+"%");
    }

    private void setAirQualityText(airQuality airQuality) {
        String api = airQuality.now.aqi;
        String category = airQuality.now.category;
        String pm10 = airQuality.now.pm10;
        String pm2p5 = airQuality.now.pm2p5;
        String no2 = airQuality.now.no2;
        String so2 = airQuality.now.so2;
        String co = airQuality.now.co;
        String o3 = airQuality.now.o3;
        airText.setText(" 空气" + airQuality.now.category);
        airSituationText.setText(category);
        airPercentText.setText(api);
        pm10Text.setText(pm10);
        pm2p5Text.setText(pm2p5);
        no2Text.setText(no2);
        so2Text.setText(so2);
        coText.setText(co);
        o3Text.setText(o3);
    }

}
