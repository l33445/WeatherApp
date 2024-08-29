package com.example.weatherapp02.utils;
import com.example.weatherapp02.data.WeatherData;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    // 私有构造方法，防止实例化
    private JsonUtils() {
    }

    /**
     * 解析天气数据
     *
     * @param jsonResponse JSON字符串
     * @return WeatherData对象
     */
    public static WeatherData parseWeatherData(String jsonResponse) {
        WeatherData weatherData = new WeatherData();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            // 假设天气数据在 "showapi_res_body" 这个JSON对象中
            JSONObject bodyObject = jsonObject.getJSONObject("showapi_res_body").getJSONObject("now");

            // 根据实际数据结构解析数据
            String temperature = bodyObject.getString("temperature");
            String weather = bodyObject.getString("weather");
            // 设置到天气数据对象中
            weatherData.setTemperature(temperature);
            weatherData.setWeather(weather);
            // 解析设置其他需要的字段...

        } catch (JSONException e) {
            e.printStackTrace();
            return null; // 或者可以选择抛出异常，根据你的错误处理策略来决定
        }
        return weatherData;
    }



}