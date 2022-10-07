package com.coolweather.colorweather;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.coolweather.colorweather.db.ProvinceName;

import java.util.List;

public class AccessAreaActivity extends AppCompatActivity {

    private List<ProvinceName> provinceNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_area);
    }
}