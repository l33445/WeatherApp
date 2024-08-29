package com.example.weatherapp02.data;

public class WeatherForecast {
    private String day;
    private int weatherIconResId;
    private double maxTemperature;
    private double minTemperature;

    public WeatherForecast(String day, int weatherIconResId, double maxTemperature, double minTemperature) {
        this.day = day;
        this.weatherIconResId = weatherIconResId;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getWeatherIconResId() {
        return weatherIconResId;
    }

    public void setWeatherIconResId(int weatherIconResId) {
        this.weatherIconResId = weatherIconResId;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }
}
