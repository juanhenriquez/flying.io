package com.example.android.flyingio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by juanhenriquez on 12/27/16.
 */

public class City {

    private String name;
    private List<Map<String, String>> cities;

    @SuppressWarnings("unused")
    private City() {}

    City(String name){
        this.name = name;
        this.cities = new ArrayList<>();
    }

    public void setCity(Map<String, String> city){
        Map<String, String> cityValue = city;
        cities.add(city);
    }

    public String getCityName() {
        return this.name;
    }

    public String getPrice(String cityName){
        String price = "0";
        for (Map<String, String> city : cities) {
            if (city.containsKey(cityName)) {
                price = city.get(cityName);
            }
        }
        return price;
    }

    public List<Map<String, String>> getCitiesList() {
        return this.cities;
    }
}
