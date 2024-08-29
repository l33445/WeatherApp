package com.example.weatherapp02.activities;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp02.R;
import com.example.weatherapp02.data.WeatherForecast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyViewModel extends ViewModel {

    private MutableLiveData<Integer> day ;
    private MutableLiveData<String> city;
    private MutableLiveData<String> weather;
    private MutableLiveData<Double> temperature;
    private List<MutableLiveData<Double>> hourTemperatureLiveDataList;
    private List<MutableLiveData<String>> skyconList;
    private List<MutableLiveData<Integer>> iconResourceLiveDataList;
    private List<MutableLiveData<Double>> weekMaxTemperature;
    private List<MutableLiveData<Double>> weekMinTemperature;
    private List<MutableLiveData<String>> dataList;
    private  MutableLiveData<List<WeatherForecast>> weatherForecasts;


    public MyViewModel() {
        hourTemperatureLiveDataList = new ArrayList<>();
        iconResourceLiveDataList = new ArrayList<>();
        weekMaxTemperature = new ArrayList<>();
        weekMinTemperature = new ArrayList<>();
        dataList = new ArrayList<>();
        weatherForecasts = new MutableLiveData<>();


        for (int i = 0; i < 5; i++) {
            hourTemperatureLiveDataList.add(new MutableLiveData<>(20.0));
        }
        for (int i = 0; i < 5; i++) {
            iconResourceLiveDataList.add(new MutableLiveData<>(R.drawable.icon_clear_night));
        }
        for (int i = 0; i < 7; i++) {
            weekMaxTemperature.add(new MutableLiveData<>(20.0));
        }
        for (int i = 0; i < 7; i++) {
            weekMinTemperature.add(new MutableLiveData<>(20.0));
        }
        for (int i = 0; i < 7; i++) {
            dataList.add(new MutableLiveData<>("2024-05-26"));
        }
        WeatherForecast weather = new WeatherForecast("2024-05-26", R.drawable.icon_clear_night, 20.0, 20.0);
        List<WeatherForecast> list = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            list.add(weather);
        }
        weatherForecasts.setValue(list);

    }


    public void setWeatherForecasts(List<WeatherForecast> weatherForecasts) {
        this.weatherForecasts.setValue(weatherForecasts);
    }
//    public WeatherForecast getWeatherForecastsAtIndex(int index) {
//            return weatherForecasts.get(index);
//    }
    public MutableLiveData<List<WeatherForecast>> getWeatherForecasts() {
        return weatherForecasts;
    }

    public MutableLiveData<Double> getTemperature() {
        if(temperature == null){
            temperature = new MutableLiveData<>();
            temperature.setValue(20.00);
        }
        return temperature;
    }

    public MutableLiveData<Double> getTemperatureLiveDataAtIndex(int index) {
        if (index >= 0 && index < hourTemperatureLiveDataList.size()) {
            return hourTemperatureLiveDataList.get(index);
        } else {
            throw new IndexOutOfBoundsException("Invalid index for temperature LiveData list.");
        }
    }

    public void setTemperatureAtIndex(int index, Double temperature) {
        if (index >= 0) {
            hourTemperatureLiveDataList.get(index).setValue(temperature);
        } else {
            throw new IndexOutOfBoundsException("Invalid index for setting temperature in LiveData list.");
        }
    }

    public MutableLiveData<Double> getWeekMaxTemperatureAtIndex(int index) {
        if (index >= 0 && index < weekMaxTemperature.size()) {
            return weekMaxTemperature.get(index);
        } else {
            throw new IndexOutOfBoundsException("Invalid index for temperature LiveData list.");
        }
    }

    public void setWeekMaxTemperatureAtIndex(int index, Double temperature) {
        if (index >= 0) {
            weekMaxTemperature.get(index).setValue(temperature);
        } else {
            throw new IndexOutOfBoundsException("Invalid index for setting temperature in LiveData list.");
        }
    }

    public MutableLiveData<Double> getWeekMinTemperatureAtIndex(int index) {
        if (index >= 0 && index < weekMinTemperature.size()) {
            return weekMinTemperature.get(index);
        } else {
            throw new IndexOutOfBoundsException("Invalid index for temperature LiveData list.");
        }
    }

    public void setWeekMinTemperatureAtIndex(int index, Double temperature) {
        if (index >= 0) {
            weekMinTemperature.get(index).setValue(temperature);
        } else {
            throw new IndexOutOfBoundsException("Invalid index for setting temperature in LiveData list.");
        }
    }

    public MutableLiveData<String> getDataListAtIndex(int index) {
        if (index >= 0 && index < dataList.size()) {
            return dataList.get(index);
        } else {
            throw new IndexOutOfBoundsException("Invalid index for temperature LiveData list.");
        }
    }

    public void setDataListAtIndex(int index, String data) {
        if (index >= 0) {
            dataList.get(index).setValue(data);
        } else {
            throw new IndexOutOfBoundsException("Invalid index for setting temperature in LiveData list.");
        }
    }

    public MutableLiveData<String> getSkyconListAtIndex(int index) {
        if (index >= 0 && index < skyconList.size()) {
            return skyconList.get(index);
        } else {
            throw new IndexOutOfBoundsException("Invalid index for skyconList.");
        }
    }
    public void setSkyconListAtIndex(int index, String skycon) {
        if (index >= 0 && index < skyconList.size()) {
            skyconList.get(index).setValue(selectIcon(skycon));
        } else {
            throw new IndexOutOfBoundsException("Invalid index for skyconList.");
        }
    }

    private String selectIcon(String weather){
        String iconName;
        // 判断天气状况并选择相应的图标资源名称
        switch (weather) {
            case "CLEAR_DAY":
                iconName = "icon_clear_day";
                break;
            case "CLEAR_NIGHT":
                iconName = "icon_clear_night";
                break;
            case "PARTLY_CLOUDY_DAY":
                iconName = "icon_partly_cloudy_day";
                break;
            case "PARTLY_CLOUDY_NIGHT":
                iconName = "icon_partly_cloudy_night";
                break;
            case "CLOUDY":
                iconName = "icon_cloudy";
                break;
            case "LIGHT_HAZE":
            case "HEAVY_HAZE":
            case "MODERATE_HAZE":
            case "FOG":
                iconName = "icon_haze";
                break;
            case "LIGHT_RAIN":
                iconName = "icon_light_rain";
                break;
            case "MODERATE_RAIN":
                iconName = "icon_moderate_rain";
                break;
            case "HEAVY_RAIN":
                iconName = "icon_heavy_rain";
                break;
            case "STORM_RAIN":
                iconName = "icon_storm_rain";
                break;
            case "LIGHT_SNOW":
                iconName = "icon_light_snow";
                break;
            case "MODERATE_SNOW":
            case "STORM_SNOW":
            case "HEAVY_SNOW":
                iconName = "icon_snow";
                break;
            case "DUST":
            case "SAND":
            case "WIND":
                iconName = "icon_wind";
                break;
            default:
                iconName = "icon_clear_night";
                break;
        }
        return iconName;
    }
    public void setIconResourceLiveDataListAtIndex(int index, int resId) {
        if (index >= 0 && index < iconResourceLiveDataList.size()) {
            iconResourceLiveDataList.get(index).setValue(resId);
        } else {
            throw new IndexOutOfBoundsException("Invalid index for setting skycon in LiveData list.");
        }
    }
    public Integer getIconResourceLiveDataListAtIndex(int index) {
        if (index >= 0 && index < iconResourceLiveDataList.size()) {

            return iconResourceLiveDataList.get(index).getValue();
        } else {
            throw new IndexOutOfBoundsException("Invalid index for temperature LiveData list.");
        }
    }


    public MutableLiveData<Integer> getDay() {
        if (day == null) {
            day = new MutableLiveData<>();
            // 初始化LiveData值
            Calendar calendar = Calendar.getInstance();
            day.setValue(calendar.get(Calendar.DAY_OF_MONTH));
        }
        // 而不是在获取时设置值，这个方法只返回LiveData对象
        return day;
    }

    public MutableLiveData<String> getCity() {
        if(city == null){
            city = new MutableLiveData<>();
            city.setValue("沈阳");
        }
        return city;
    }

    public MutableLiveData<String> getWeather() {
        if(weather == null){
            weather = new MutableLiveData<>();
            weather.setValue("晴");
        }
        return weather;
    }

    // 您可能会有一个设置值的方法
    public void updateDay() {
        Calendar calendar = Calendar.getInstance();
        day.setValue(calendar.get(Calendar.DAY_OF_MONTH));
    }



}
