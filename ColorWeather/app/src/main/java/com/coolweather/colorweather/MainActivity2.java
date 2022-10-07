package com.coolweather.colorweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.coolweather.colorweather.service.PositionWeatherReceiver;
import com.coolweather.colorweather.service.PositionWeatherService;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity2 extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.coolweather.colorweather");
        PositionWeatherReceiver receiver = new PositionWeatherReceiver();
        registerReceiver(receiver, intentFilter);

        button = (Button) findViewById(R.id.startService);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, PositionWeatherService.class);
                startService(intent);
            }
        });

        button = (Button) findViewById(R.id.stopService);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, PositionWeatherService.class);
                stopService(intent);
            }
        });

    }

}