package com.example.weatherapp02.utils;

import java.util.HashMap;
import java.util.Map;

public class CityCoordinates {

    private static final Map<String, double[]> cityCoordinatesMap = new HashMap<>();

    static {
        cityCoordinatesMap.put("自动定位", new double[]{41.8057, 123.4315});  // 如果需要自动定位
        cityCoordinatesMap.put("北京市", new double[]{39.9042, 116.4074});
        cityCoordinatesMap.put("沈阳市", new double[]{41.8057, 123.4315});
        cityCoordinatesMap.put("长春市", new double[]{43.8868, 125.3245});
        cityCoordinatesMap.put("哈尔滨市", new double[]{45.8038, 126.5349});
        cityCoordinatesMap.put("天津市", new double[]{39.3434, 117.3616});
        cityCoordinatesMap.put("呼和浩特市", new double[]{40.8183, 111.7665});
        cityCoordinatesMap.put("银川市", new double[]{38.4872, 106.2309});
        cityCoordinatesMap.put("太原市", new double[]{37.8706, 112.5489});
        cityCoordinatesMap.put("石家庄市", new double[]{38.0428, 114.5149});
        cityCoordinatesMap.put("济南市", new double[]{36.6512, 117.1201});
        cityCoordinatesMap.put("郑州市", new double[]{34.7466, 113.6254});
        cityCoordinatesMap.put("西安市", new double[]{34.2649, 108.9428});
        cityCoordinatesMap.put("武汉市", new double[]{30.5928, 114.3055});
        cityCoordinatesMap.put("南京市", new double[]{32.0572, 118.7782});
        cityCoordinatesMap.put("合肥市", new double[]{31.8205, 117.2290});
        cityCoordinatesMap.put("上海市", new double[]{31.2304, 121.4737});
        cityCoordinatesMap.put("长沙市", new double[]{28.2282, 112.9388});
        cityCoordinatesMap.put("南昌市", new double[]{28.6829, 115.8582});
        cityCoordinatesMap.put("杭州市", new double[]{30.2741, 120.1551});
        cityCoordinatesMap.put("福州市", new double[]{26.0745, 119.2965});
        cityCoordinatesMap.put("广州市", new double[]{23.1291, 113.2644});
        cityCoordinatesMap.put("海口市", new double[]{20.0174, 110.3492});
        cityCoordinatesMap.put("南宁市", new double[]{22.8170, 108.3669});
        cityCoordinatesMap.put("重庆市", new double[]{29.5647, 106.5507});
        cityCoordinatesMap.put("昆明市", new double[]{24.8797, 102.8332});
        cityCoordinatesMap.put("贵阳市", new double[]{26.6470, 106.6302});
        cityCoordinatesMap.put("成都市", new double[]{30.5728, 104.0668});
        cityCoordinatesMap.put("兰州市", new double[]{36.0611, 103.8343});
        cityCoordinatesMap.put("西宁市", new double[]{36.6171, 101.7782});
        cityCoordinatesMap.put("拉萨市", new double[]{29.6525, 91.1721});
        cityCoordinatesMap.put("乌鲁木齐市", new double[]{43.8256, 87.6168});
    }

    public static double[] getCoordinates(String city) {
        return cityCoordinatesMap.getOrDefault(city, new double[]{0.0, 0.0});
    }
}
