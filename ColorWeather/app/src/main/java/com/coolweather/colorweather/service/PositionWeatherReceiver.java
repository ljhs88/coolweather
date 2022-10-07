package com.coolweather.colorweather.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.coolweather.colorweather.MainActivity2;

public class PositionWeatherReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("123", "onReceive");
        if (intent.getAction().equals("com.coolweather.colorweather")) {
            Intent intentStart = new Intent(context, PositionWeatherService.class);
            context.startService(intentStart);
        }
    }

}