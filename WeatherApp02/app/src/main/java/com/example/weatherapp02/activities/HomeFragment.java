package com.example.weatherapp02.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.weatherapp02.R;
import com.example.weatherapp02.adapter.WeatherForecastAdapter;
import com.example.weatherapp02.data.WeatherForecast;
import com.example.weatherapp02.databinding.FragmentHomeBinding;
import com.example.weatherapp02.utils.CityCoordinates;
import com.example.weatherapp02.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private MyViewModel myviewModel;
    private FragmentHomeBinding binding;

    private RecyclerView recyclerView;
    private WeatherForecastAdapter adapter;
    private int clickCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        myviewModel = new ViewModelProvider(this).get(MyViewModel.class);

        binding.setData(myviewModel);
        binding.setLifecycleOwner(this);
        ConstraintLayout constraintLayout = binding.getRoot().findViewById(R.id.constraint_layout);
        recyclerView = binding.getRoot().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new WeatherForecastAdapter(myviewModel.getWeatherForecasts().getValue());
        recyclerView.setAdapter(adapter);

        Spinner spinner = binding.getRoot().findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.city_slots, android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals("自动选择")) {
                    fetchLocationAndWeather();
                } else {


                        updateWeather(CityCoordinates.getCoordinates(item)[0], CityCoordinates.getCoordinates(item)[1], item);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 当没有任何项被选中时
            }
        });
// 获取并设置按钮点击事件
        ImageButton changeColorButton = binding.getRoot().findViewById(R.id.changeColorButton);
        changeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBackgroundColor();
            }
        });

        return binding.getRoot();
    }

    private void changeBackgroundColor() {
        clickCount++;
        ConstraintLayout constraintLayout = binding.getRoot().findViewById(R.id.constraint_layout);
        if (clickCount % 3 == 0) {
            constraintLayout.setBackgroundResource(R.color.colorBlue);
        } else {
            Drawable newBackground = getRandomGradientDrawable();
            constraintLayout.setBackground(newBackground);
        }
    }


    private Drawable getRandomGradientDrawable() {
        // 生成两个在深蓝色和紫色系之间的随机颜色
        int color1 = getRandomColorInRange(0xFF000080, 0xFF8A2BE2); // 从深蓝色到紫色
        int color2 = getRandomColorInRange(0xFF000080, 0xFF8A2BE2);

        // 创建一个渐变 drawable
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.BL_TR, // 渐变方向
                new int[] {color1, color2});        // 渐变颜色

        return gradientDrawable;
    }

    private int getRandomColorInRange(int startColor, int endColor) {
        Random random = new Random();
        float[] startHSB = new float[3];
        float[] endHSB = new float[3];

        // 将颜色转换为 HSB (Hue, Saturation, Brightness)
        Color.colorToHSV(startColor, startHSB);
        Color.colorToHSV(endColor, endHSB);

        // 生成随机 HSB 值
        float randomHue = startHSB[0] + random.nextFloat() * (endHSB[0] - startHSB[0]);
        float randomSaturation = startHSB[1] + random.nextFloat() * (endHSB[1] - startHSB[1]);
        float randomBrightness = startHSB[2] + random.nextFloat() * (endHSB[2] - startHSB[2]);

        // 将随机 HSB 值转换回颜色
        return Color.HSVToColor(new float[] {randomHue, randomSaturation, randomBrightness});
    }

    private void fetchLocationAndWeather() {
        NetworkUtils.fetchLocationFromIP(getActivity().getApplicationContext(), new NetworkUtils.LocationResultListener() {
            @Override
            public void onLocationResult(double lat, double lng, String city) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> updateWeather(lat, lng, city));
                }
            }

            @Override
            public void onError(String error) {
                // 处理错误
            }
        });
    }

    private void updateWeather(double lat, double lng, String city) {
        myviewModel.getCity().setValue(city);
        NetworkUtils.fetchWeatherData(getActivity().getApplicationContext(), lat, lng, new NetworkUtils.WeatherDataCallback() {
            @Override
            public void onSuccess(double temperature, String skycon) {
                myviewModel.getTemperature().setValue(temperature);
                binding.weatherImageView.setImageResource(selectIcon(skycon));
                myviewModel.getWeather().setValue(selectWeather(skycon));
            }

            @Override
            public void onError(String error) {
                // 处理错误
            }
        });

        // 更新小时预报数据
        NetworkUtils.fetchHourlyWeatherData(getActivity(), lat, lng, new NetworkUtils.hourWeatherDataCallback() {
            @Override
            public void onSuccess(List<Double> temperatures, List<String> skycons) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> updateHourlyWeather(temperatures, skycons));
                }
            }

            @Override
            public void onError(String error) {
                // 处理错误
            }
        });

        // 更新周预报数据
        NetworkUtils.fetchWeeklyWeatherData(getActivity(), lat, lng, new NetworkUtils.weekWeatherDataCallback() {
            @Override
            public void onSuccess(List<String> times, List<Double> max, List<Double> min, List<String> skycons) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> updateWeeklyWeather(times, max, min, skycons));
                }
            }

            @Override
            public void onError(String error) {
                // 处理错误
            }
        });
    }

    private void updateHourlyWeather(List<Double> temperatures, List<String> skycons) {
        for (int i = 0; i < 5; i++) {
            myviewModel.setTemperatureAtIndex(i, temperatures.get(i));
            myviewModel.setIconResourceLiveDataListAtIndex(i, selectIcon(skycons.get(i)));
        }
        binding.dayImageView1.setImageResource(selectIcon(skycons.get(0)));
        binding.dayImageView2.setImageResource(selectIcon(skycons.get(1)));
        binding.dayImageView3.setImageResource(selectIcon(skycons.get(2)));
        binding.dayImageView4.setImageResource(selectIcon(skycons.get(3)));
        binding.dayImageView5.setImageResource(selectIcon(skycons.get(4)));
    }

    private void updateWeeklyWeather(List<String> times, List<Double> max, List<Double> min, List<String> skycons) {
        List<WeatherForecast> forecastList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            forecastList.add(new WeatherForecast(times.get(i), selectIcon(skycons.get(i)), max.get(i), min.get(i)));
        }
        myviewModel.setWeatherForecasts(forecastList);
        adapter.setmData(myviewModel.getWeatherForecasts().getValue());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public int selectIcon(String weather) {
        String iconName;
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
        return getResources().getIdentifier(iconName, "drawable", getContext().getPackageName());
    }

    public String selectWeather(String weather) {
        String dayWeather;
        switch (weather) {
            case "CLEAR_DAY":
                dayWeather = "晴";
                break;
            case "CLEAR_NIGHT":
                dayWeather = "晴";
                break;
            case "PARTLY_CLOUDY_DAY":
                dayWeather = "多云";
                break;
            case "PARTLY_CLOUDY_NIGHT":
                dayWeather = "多云";
                break;
            case "CLOUDY":
                dayWeather = "阴";
                break;
            case "LIGHT_HAZE":
            case "HEAVY_HAZE":
            case "MODERATE_HAZE":
            case "FOG":
                dayWeather = "雾霾";
                break;
            case "LIGHT_RAIN":
                dayWeather = "小雨";
                break;
            case "MODERATE_RAIN":
                dayWeather = "中雨";
                break;
            case "HEAVY_RAIN":
                dayWeather = "大雨";
                break;
            case "STORM_RAIN":
                dayWeather = "暴雨";
                break;
            case "LIGHT_SNOW":
                dayWeather = "小雪";
                break;
            case "MODERATE_SNOW":
            case "STORM_SNOW":
            case "HEAVY_SNOW":
                dayWeather = "大雪";
                break;
            case "DUST":
            case "SAND":
            case "WIND":
                dayWeather = "大风";
                break;
            default:
                dayWeather = "晴";
                break;
        }
        return dayWeather;
    }


    @Override
    public void onActivityCreated(@NonNull Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton musicButton = getView().findViewById(R.id.musicButton);
        musicButton.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(v);
            controller.navigate(R.id.action_homeFragment_to_musicFragment);
        });
        ImageButton timeButton = getView().findViewById(R.id.timeButton);
        timeButton.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(v);
            controller.navigate(R.id.action_homeFragment_to_timeFragment);
        });
    }


}