package com.coolweather.colorweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import com.coolweather.colorweather.db.positionCity;

public class positionActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addButton;
    private Button backButton;
    private List<positionCity> positionList;

    // positionList
    private List<positionCity> positionCityList;

    // fragment
    private List<weatherAreaFragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);
        Log.d("123", "positionActivity onCreate");

        addButton = (Button) findViewById(R.id.add_city);
        backButton = (Button) findViewById(R.id.back);
        addButton.setOnClickListener(this);
        // 添加活动
        ActivityCollector.addActivity(this);
        if (ActivityCollector.activities.size() == 1) {
            backButton.setVisibility(View.GONE);
        }

        positionList = LitePal.findAll(positionCity.class);
        List<String> cityName = new ArrayList<>();
        List<String> cityLocation = new ArrayList<>();
        for (positionCity city : positionList) {
            cityName.add(city.getCountyName());
            cityLocation.add(String.valueOf(city.getLocationId()));
        }
        Log.d("123", "positionActivity:" + positionList.toString());
        // 跳转活动
        Intent intent = getIntent();
        String countyName = intent.getStringExtra("cityName");
        //Log.d("123", "onCreate end");

    }

    /*class MyFragmentAdapter extends FragmentPagerAdapter {

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

    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_city:
                Intent intent = new Intent(this, AccessAreaActivity.class);
                startActivity(intent);
                break;
        }
    }

}