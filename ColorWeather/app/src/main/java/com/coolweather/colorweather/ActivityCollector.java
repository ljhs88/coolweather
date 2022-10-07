package com.coolweather.colorweather;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    //存储Activity的List
    public static List<Activity> activities = new ArrayList<>();

    // 存储长度
    public static int len = activities.size();

    //添加Activity
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    //移出Activity
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    //销毁所有Activity
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}