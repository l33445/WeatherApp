package com.example.weatherapp02.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp02.R;
import com.example.weatherapp02.data.WeatherForecast;

import java.util.List;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.ViewHolder> {

    private List<WeatherForecast> mData;

    public WeatherForecastAdapter(List<WeatherForecast> data) {
        mData = data;
    }

    public void setmData(List<WeatherForecast> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherForecast forecast = mData.get(position);
        //holder.bind(forecast);
        holder.weekTextView.setText(forecast.getDay());
        holder.weekImageView.setImageResource(forecast.getWeatherIconResId());
        holder.maxTextView.setText(String.valueOf(forecast.getMaxTemperature()) + "°");
        holder.minTextView.setText(String.valueOf(forecast.getMinTemperature()) + "°");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // 声明视图引用
        TextView weekTextView, maxTextView, minTextView;
        ImageView weekImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 初始化视图引用
            weekTextView = itemView.findViewById(R.id.weekTextView);
            maxTextView = itemView.findViewById(R.id.maxTextView);
            minTextView = itemView.findViewById(R.id.minTextView);
            weekImageView = itemView.findViewById(R.id.weekImageView);
        }

        public void bind(WeatherForecast forecast) {
            // 绑定数据到视图
        }
    }

}
