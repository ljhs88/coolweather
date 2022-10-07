package com.coolweather.colorweather.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coolweather.colorweather.R;
import com.coolweather.colorweather.gson.day7;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.coolweather.colorweather.weatherdb.*;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder> {

    //private List<day7.daily> dailyList;
    private Context context;

    private List<day7db> dailyList;

    public DailyAdapter(List<day7db> dailyList, Context context) {
        this.dailyList = dailyList;
        this.context = context;
    }

    @NonNull
    @Override
    public DailyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_future_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DailyAdapter.ViewHolder holder, int position) {
        holder.date.setText(dailyList.get(position).getDate().substring(5, 7)+"/"+
                dailyList.get(position).getDate().substring(8, 10));
        holder.temp.setText(dailyList.get(position).getTemperatureMax() + "℃/" +dailyList.get(position).getTemperatureMin()+"℃");
        holder.weatherSituation.setText(dailyList.get(position).getWeatherSituation());
        String url = "https://cdn.heweather.com/cond_icon/"+ dailyList.get(position).getWeatherImage() +".png";
        Glide.with(context).load(url).into(holder.weatherImage);
    }

    @Override
    public int getItemCount() {
        return dailyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView temp;
        TextView date;
        TextView weatherSituation;
        ImageView weatherImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            temp = itemView.findViewById(R.id.day_future_temp_text);
            date = itemView.findViewById(R.id.day_future_date_text);
            weatherSituation = itemView.findViewById(R.id.day_future_weather_text);
            weatherImage = itemView.findViewById(R.id.day_future_weather_image);
        }
    }
}
