package com.coolweather.colorweather.util;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
                /*.Builder()
                .connectTimeout(10, TimeUnit.MINUTES) // 连接超时
                .readTimeout(10, TimeUnit.MINUTES)    // 读取超时
                .writeTimeout(10, TimeUnit.MINUTES)   // 写超时
                .build();*/

        Request request = new Request.Builder()
                .url(address)
                .build();
        //client.newCall(request).enqueue(callback);
        client.newCall(request).enqueue(callback);
        //Log.d("123", "okHttp connect");
    }

}
