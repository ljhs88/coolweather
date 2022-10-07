package com.coolweather.colorweather;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.coolweather.colorweather.db.CityLocationId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.colorweather.db.CityProvince;
import com.coolweather.colorweather.db.ProvinceName;
import com.coolweather.colorweather.db.position;
import com.coolweather.colorweather.db.positionCity;
import com.coolweather.colorweather.service.PositionWeatherService;
import com.coolweather.colorweather.util.savePC;

import org.litepal.LitePal;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class weatherActivity extends AppCompatActivity  {

    // context
    private Context context;

    // positionList
    private List<positionCity> positionCities;

    // fragment
    private List<weatherAreaFragment> fragmentList = new ArrayList<>();

    List<String> cityNameList;
    List<String> cityLocationIdList;

    //请求权限码
    private static final int REQUEST_PERMISSIONS = 9527;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private String countyName;

    private static int key = 0;

    private static final int UPDATE_CITY = 1;

    private FragmentPagerAdapter adapter;

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Log.d("123", "weatherActivity onCreate");

        position position = new position();
        position.setCountyName("长安区");
        position.save();
        checkingAndroidVersion();
        // Load the data
        List<ProvinceName> provinceNameList = LitePal.findAll(ProvinceName.class);
        List<CityLocationId> cityLocationIdList = LitePal.findAll(CityLocationId.class);
        List<CityProvince> cityProvinceList = LitePal.findAll(CityProvince.class);
        if (provinceNameList.size() == 0 && cityLocationIdList.size() == 0 && cityProvinceList.size() == 0) {
            savePC.saveFile(this);
            // Start service
            Intent intent = new Intent(weatherActivity.this, PositionWeatherService.class);
            startService(intent);
        }

        Intent intent = getIntent();
        String countyName = "";
        countyName = intent.getStringExtra("countyName");
        if (countyName == null) {
            // Get location city
            countyName = LitePal.findFirst(position.class).getCountyName();
            Log.d("123", countyName);
        }
        if (!countyName.equals("adapter")) {
            StringBuilder stringName = new StringBuilder(countyName);
            stringName.deleteCharAt(stringName.length()-1);
            // 先查重
            final List<positionCity> positionCityList = LitePal.findAll(positionCity.class);
            Log.d("123", "weatherActivityBefore:" + positionCityList.toString());
            for (int i = 0; i < positionCityList.size(); i++) {
                if (positionCityList.get(i).getCountyName().equals(stringName.toString())) {
                    key=1;
                    break;
                }
            }
            List<CityLocationId> county = LitePal
                    .where("countyName = ?", stringName.toString())
                    .limit(1)
                    .find(CityLocationId.class);
            // second全部指定为false
            positionCity allCity = new positionCity();
            allCity.setFirst(false);
            allCity.setSecond(false);
            allCity.updateAll();
            if (key == 0) {
                positionCity city = new positionCity();
                city.setCountyName(stringName.toString());
                city.setLocationId(county.get(0).getLocationId());
                city.setFirst(true);
                city.setSecond(true);
                city.save();
            } else {
                positionCity allCity2 = new positionCity();
                allCity2.setFirst(true);
                allCity2.setSecond(true);
                allCity2.updateAll("countyName = ?", stringName.toString());
            }
        }

        // Load the fragmentList
        initFragment();

    }

    /**
     * 检查Android版本
     */
    private void checkingAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android6.0及以上获取权限再定位
            requestPermission();
        } else {
            // Android6.0以下直接定位
            // 启动定位
            //mLocationClient.startLocation();
            Intent intent = new Intent(this, PositionWeatherService.class);
            startService(intent);
        }
    }

    /**
     * 动态请求权限
     */
    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    private void requestPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (EasyPermissions.hasPermissions(this, permissions)) {
            // true 有权限 开始定位
            showMsg("定位中...");

            // 启动定位
            //mLocationClient.startLocation();
            Intent intent = new Intent(this, PositionWeatherService.class);
            startService(intent);
        } else {
            // false 无权限
            EasyPermissions.requestPermissions(this, "需要权限", REQUEST_PERMISSIONS, permissions);
        }
    }

    /**
     * Toast提示
     * @param msg 提示内容
     */
    private void showMsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 请求权限结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 设置权限请求结果
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void initFragment() {
        positionCities = LitePal.findAll(positionCity.class);
        cityNameList = new ArrayList<>();
        cityLocationIdList = new ArrayList<>();
        for (positionCity positionCity : positionCities) {
            cityNameList.add(positionCity.getCountyName());
            cityLocationIdList.add(String.valueOf(positionCity.getLocationId()));
        }
        Log.d("123","init:"+cityNameList.toString());
        Log.d("123","init:"+cityLocationIdList.toString());
        int j;
        for (j = 0; j < positionCities.size(); j++) {
            if (positionCities.get(j).getSecond() == true) {
                weatherAreaFragment fragment = new weatherAreaFragment();
                Bundle bundle = new Bundle();
                bundle.putString("cityName", cityNameList.get(j));
                bundle.putString("LocationId", cityLocationIdList.get(j));
                Log.d("123","j:" + cityNameList.get(j));
                fragment.setArguments(bundle);
                fragmentList.add(fragment);
                break;
            }
        }
        Log.d("123", "j = " + String.valueOf(j));
        for (int i = 0; i < positionCities.size(); i++) {
            if (i != j) {
                weatherAreaFragment fragment = new weatherAreaFragment();
                Bundle bundle = new Bundle();
                bundle.putString("cityName", cityNameList.get(i));
                bundle.putString("LocationId", cityLocationIdList.get(i));
                Log.d("123","i:" + cityNameList.get(i));
                fragment.setArguments(bundle);
                fragmentList.add(fragment);
            }
        }
        pager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new MyFragmentAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("123", "onRestart");
        positionCities = null;
        fragmentList.clear();
        cityNameList = null;
        cityLocationIdList = null;
        pager = null;
        adapter = null;
        initFragment();
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    // 退出后在后台运行
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}