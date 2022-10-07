package com.coolweather.colorweather.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.coolweather.colorweather.ActivityCollector;
import com.coolweather.colorweather.R;
import com.coolweather.colorweather.util.HttpUtil;
import com.coolweather.colorweather.weatherActivity;
import com.google.gson.Gson;
import org.litepal.LitePal;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import com.coolweather.colorweather.gson.nowTime;
import com.coolweather.colorweather.db.positionCity;
import com.coolweather.colorweather.positionAreaFragment;
import com.coolweather.colorweather.weatherAreaFragment;

public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.ViewHolder> {

    private List<positionCity> positionList;
    private Context context;
    private Activity activity;

    public PositionAdapter(List<positionCity> positionList, Context context, Activity activity) {
        this.positionList = positionList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.position_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("123", "delete");
                String name = (String) holder.positionNameText.getText();
                for (positionCity city : positionList) {
                    if (city.getCountyName().equals(name)) {
                        positionList.remove(city);
                        break;
                    }
                }
                List<positionCity> cityList = LitePal.where("countyName = ?", name)
                        .limit(1)
                        .find(positionCity.class);
                if (cityList.get(0).getSecond() == true) {
                    LitePal.deleteAll(positionCity.class, "countyName = ?", name);
                    List<positionCity> list = LitePal.findAll(positionCity.class);
                    positionCity city = new positionCity();
                    city.setSecond(true);
                    city.updateAll("countyName = ?", list.get(0).getCountyName());
                } else {
                    LitePal.deleteAll(positionCity.class, "countyName = ?", name);
                }

                // 更新
                positionAreaFragment.adapter.notifyDataSetChanged();
            }
        });

        holder.toWeatherActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // first,second全部指定为false
                positionCity allCity = new positionCity();
                allCity.setSecond(false);
                allCity.updateAll();
                // 然后指定当前为true
                positionCity city = new positionCity();
                city.setSecond(true);
                city.updateAll("countyName = ?", (String)holder.positionNameText.getText());
                // 跳转活动
                Intent intent = new Intent(context, weatherActivity.class);
                intent.putExtra("countyName", "adapter");
                context.startActivity(intent);
                ActivityCollector.finishAll();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("123", "adapter："+positionList.toString());
        positionCity city = positionList.get(position);
        Log.d("123", "positionAdapter:" + positionList.toString());
        holder.positionNameText.setText(city.getCountyName());
        if (city.getFirst() == true) {
            holder.positionImage.setImageResource(R.drawable.position);
        } else {
            holder.positionImage.setImageResource(R.drawable.backstar3);
        }
        int LocationId = city.getLocationId();
        String url = "https://devapi.qweather.com/v7/weather/now?location=" +
                LocationId + "&key=9b399ace380d4928b51b492839d86b37";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        weatherAreaFragment.dialog.show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String content = response.body().string();
                //Log.d("123","碎片天气："+ content);
                Gson gson = new Gson();
                nowTime nowTime = gson.fromJson(content, nowTime.class);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        holder.temperatureText.setText(nowTime.now.temperatureAve);
                        holder.weatherText.setText(nowTime.now.weatherQuality);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return positionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView positionNameText;
        TextView temperatureText;
        TextView weatherText;
        ImageView positionImage;
        Button deleteButton;
        Button toWeatherActivity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            positionNameText = (TextView) itemView.findViewById(R.id.position_city_name);
            temperatureText = (TextView) itemView.findViewById(R.id.position_city_temperature);
            weatherText = (TextView) itemView.findViewById(R.id.position_city_weather);
            positionImage = (ImageView) itemView.findViewById(R.id.position_city_image);
            deleteButton = (Button) itemView.findViewById(R.id.delete_button);
            toWeatherActivity = (Button) itemView.findViewById(R.id.to_weatherActivity);
        }
    }
}
