package com.coolweather.colorweather.Adapter;

import com.bumptech.glide.Glide;
import com.coolweather.colorweather.R;
import com.coolweather.colorweather.gson.hour24.hourly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.coolweather.colorweather.weatherdb.*;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {

    //private List<hourly> hourlyList = new ArrayList<>();
    private Context context;

    private List<hour24db> hourlyList = new ArrayList<>();

    /*public HourlyAdapter(List<hourly> hourlyList, Context context) {
        this.hourlyList = hourlyList;
        this.context = context;
    }*/
    public HourlyAdapter(List<hour24db> hour24dbList, Context context) {
        this.hourlyList = hour24dbList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hour_future_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*int first = Integer.parseInt(hourlyList.get(position).nowTime.substring(11, 12));
        int second = Integer.parseInt(hourlyList.get(position).nowTime.substring(12, 13));
        if (first*10+second > 0 && first*10+second < 7) {
            holder.hourTimeText.setText("凌晨" + hourlyList.get(position).nowTime.substring(12,16));
        } else if (first*10+second >= 7 && first*10+second <= 9) {
            holder.hourTimeText.setText("中午" + hourlyList.get(position).nowTime.substring(12,16));
        } else if (first*10+second > 9 && first*10+second <= 12) {
            holder.hourTimeText.setText("中午" + hourlyList.get(position).nowTime.substring(11,16));
        } else if (first*10+second > 12 && first*10+second < 19) {
            holder.hourTimeText.setText("下午" + hourlyList.get(position).nowTime.substring(11,16));
        } else if (first*10+second >= 19 && first*10+second <= 23) {
            holder.hourTimeText.setText("晚上" + hourlyList.get(position).nowTime.substring(11,16));
        } else if (first*10+second==0) {
            holder.hourTimeText.setText("午夜" + hourlyList.get(position).nowTime.substring(11,16));
        }
        holder.tempText.setText(hourlyList.get(position).temperature+"℃");
        if (!hourlyList.get(position).rainPercent.equals("0")) {
            holder.rainPercentText.setText(hourlyList.get(position).rainPercent+"%");
        }
        String url = "https://cdn.heweather.com/cond_icon/" + hourlyList.get(position).weatherImage +".png";
        Glide.with(context).load(url).into(holder.weatherImage);*/
        int first = Integer.parseInt(hourlyList.get(position).getNowTime().substring(11, 12));
        int second = Integer.parseInt(hourlyList.get(position).getNowTime().substring(12, 13));
        if (first*10+second > 0 && first*10+second < 7) {
            holder.hourTimeText.setText("凌晨" + hourlyList.get(position).getNowTime().substring(12,16));
        } else if (first*10+second >= 7 && first*10+second <= 9) {
            holder.hourTimeText.setText("中午" + hourlyList.get(position).getNowTime().substring(12,16));
        } else if (first*10+second > 9 && first*10+second <= 12) {
            holder.hourTimeText.setText("中午" + hourlyList.get(position).getNowTime().substring(11,16));
        } else if (first*10+second > 12 && first*10+second < 19) {
            holder.hourTimeText.setText("下午" + hourlyList.get(position).getNowTime().substring(11,16));
        } else if (first*10+second >= 19 && first*10+second <= 23) {
            holder.hourTimeText.setText("晚上" + hourlyList.get(position).getNowTime().substring(11,16));
        } else if (first*10+second==0) {
            holder.hourTimeText.setText("午夜" + hourlyList.get(position).getNowTime().substring(11,16));
        }
        holder.tempText.setText(hourlyList.get(position).getTemperature()+"℃");
        if (!hourlyList.get(position).rainPercent.equals("0")) {
            holder.rainPercentText.setText(hourlyList.get(position).getRainPercent()+"%");
        }
        String url = "https://cdn.heweather.com/cond_icon/" + hourlyList.get(position).getWeatherImage() +".png";
        Glide.with(context).load(url).into(holder.weatherImage);
    }

    @Override
    public int getItemCount() {
        return hourlyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hourTimeText;
        TextView rainPercentText;
        TextView tempText;
        ImageView weatherImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hourTimeText = itemView.findViewById(R.id.hour_future_time_text);
            rainPercentText = itemView.findViewById(R.id.hour_future_rain_percent_text);
            tempText = itemView.findViewById(R.id.hour_future_temperature_text);
            weatherImage = itemView.findViewById(R.id.hour_future_weather_image);
        }
    }
}
