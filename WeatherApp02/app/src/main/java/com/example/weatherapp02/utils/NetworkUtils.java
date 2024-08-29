package com.example.weatherapp02.utils;

import android.net.Uri;
import android.os.Handler;
import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp02.activities.MyViewModel;
import com.show.api.ShowApiRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkUtils {
    static String url = "https://route.showapi.com/9-2";
    static String showapi_appid = "***";
    static String showapi_sign = "***";

    static String curl = "https://api.caiyunapp.com/v2.6/***/";//注意添加密钥
    // 禁止实例化
    private NetworkUtils() {}

    public interface Callback {
        void onResult(String result);
        void onError(String error);
    }

    /**
     * 从特定API获取天气数据
     *
     * @param handler 用于执行UI线程操作的Handler对象
     * @param callback 结果回调
     */
    public static void getWeatherData(String city, Handler handler, Callback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String res = new ShowApiRequest(url, showapi_appid, showapi_sign)
                        .addTextPara("area", city)
                        .addTextPara("needMoreDay", "0")
                        .addTextPara("needIndex", "0")
                        .addTextPara("need3HourForcast", "1")
                        .addTextPara("needAlarm", "0")
                        .addTextPara("needHourData", "")
                        .post();

                // 如果handler和callback不为空就通过handler.sendEmptyMessage将结果传回主线程
                if (handler != null && callback != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResult(res);

                        }
                    });
                }

            }
        }).start();
    }

    /**
     * 使用HttpURLConnection从彩云天气API获取实时天气数据
     * @param callback 回调接口，用于处理请求结果
     */
    public static void fetchCaiyunWeatherData(String state, String site, Callback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(curl+site+"/"+state);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                if (callback != null) {
                    callback.onResult(result.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onError(e.toString());
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

// 用于结果回调的接口, 现在包括返回城市名称
    public interface LocationResultListener {
        void onLocationResult(double lat, double lng, String city);
        void onError(String error);
    }

    // 工具类中添加获取位置信息及城市名称的方法
    public static void fetchLocationFromIP(Context context, final LocationResultListener listener) {

        String baseurl = "https://apis.map.qq.com/ws/location/v1/ip?&key=***";//你的密钥

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONObject result = jsonResponse.getJSONObject("result");
                            JSONObject location = result.getJSONObject("location");
                            JSONObject ad_info = result.getJSONObject("ad_info");

                            double lat = location.getDouble("lat");
                            double lng = location.getDouble("lng");
                            String city = ad_info.getString("city");

                            if (listener != null) {
                                listener.onLocationResult(lat, lng, city); // 现在包含city参数
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            if(listener != null) {
                                listener.onError(e.getMessage());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(listener != null) {
                            listener.onError(error.toString());
                        }
                    }
                });

        queue.add(stringRequest);
    }


    public interface WeatherDataCallback {
        void onSuccess(double temperature, String skycon);
        void onError(String error);
    }

    // 方法用来发起网络请求并解析返回的天气数据
    public static void fetchWeatherData(Context context, double lat, double lon, final WeatherDataCallback callback) {
        String baseUrl = "https://api.caiyunapp.com/v2.6/***";//你的密钥
        String url = baseUrl + "/" + lon + "," + lat + "/realtime";

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONObject result = jsonResponse.getJSONObject("result");
                            JSONObject realtime = result.getJSONObject("realtime");

                            double temperature = realtime.getDouble("temperature");
                            String skycon = realtime.getString("skycon");

                            callback.onSuccess(temperature, skycon);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onError(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());
            }
        });

        queue.add(stringRequest);
    }

    public interface hourWeatherDataCallback {
        void onSuccess(List<Double> temperatures, List<String> skycons);
        void onError(String error);
    }
//彩云天气api获取小时天气
    public static void fetchHourlyWeatherData(Context context, double lat, double lon, final hourWeatherDataCallback callback) {
        String baseUrl = "https://api.caiyunapp.com/v2.6/***";//你的密钥
        String url = baseUrl + "/" + lon + "," + lat + "/hourly?hourlysteps=5";

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONObject result = jsonResponse.getJSONObject("result");
                            JSONObject hourly = result.getJSONObject("hourly");
                            JSONArray temperaturesArray = hourly.getJSONArray("temperature");
                            JSONArray skyconsArray = hourly.getJSONArray("skycon");

                            List<Double> temperatures = new ArrayList<>();
                            List<String> skycons = new ArrayList<>();

                            for (int i = 0; i < 5; i++) {
                                JSONObject tempObject = temperaturesArray.getJSONObject(i);
                                temperatures.add(tempObject.getDouble("value"));

                                JSONObject skyconObject = skyconsArray.getJSONObject(i);
                                skycons.add(skyconObject.getString("value"));
                            }

                            callback.onSuccess(temperatures, skycons);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onError(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());
            }
        });

        queue.add(stringRequest);
    }

    //彩云天气api获取未来7天天气
    public interface weekWeatherDataCallback {
        void onSuccess(List<String> times,List<Double> max, List<Double> min,  List<String> skycons);
        void onError(String error);
    }
    public static void fetchWeeklyWeatherData(Context context, double lat, double lon, final weekWeatherDataCallback callback){
        String baseUrl = "https://api.caiyunapp.com/v2.6/***";//你的密钥
        String url = baseUrl + "/" + lon + "," + lat + "/daily?dailysteps=7";

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONObject result = jsonResponse.getJSONObject("result");
                            JSONObject daily = result.getJSONObject("daily");
                            JSONArray temperaturesArray = daily.getJSONArray("temperature");
                            JSONArray skyconsArray = daily.getJSONArray("skycon");

                            List<String> times = new ArrayList<>();
                            List<Double> max = new ArrayList<>();
                            List<Double> min = new ArrayList<>();
                            List<String> skycons = new ArrayList<>();

                            for (int i = 0; i < 7; i++) {
                                JSONObject tempObject = temperaturesArray.getJSONObject(i);
                                times.add(tempObject.getString("date").substring(0, 10));
                                max.add(tempObject.getDouble("max"));
                                min.add(tempObject.getDouble("min"));

                                JSONObject skyconObject = skyconsArray.getJSONObject(i);
                                skycons.add(skyconObject.getString("value"));
                            }

                            callback.onSuccess(times, max, min, skycons);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onError(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());
            }
        });

        queue.add(stringRequest);
    }

}
