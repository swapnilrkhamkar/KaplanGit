package com.assignment.kaplan;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class ForecastModel implements Serializable {

    private ArrayList<WeatherModel> weather;
    private MainModel main;
    private WindModel wind;

    public ForecastModel(ArrayList<WeatherModel> weather, MainModel main, WindModel wind) {
        this.weather = weather;
        this.main = main;
        this.wind = wind;
    }

    public ForecastModel() {
    }

    public ArrayList<WeatherModel> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<WeatherModel> weather) {
        this.weather = weather;
    }

    public MainModel getMain() {
        return main;
    }

    public void setMain(MainModel main) {
        this.main = main;
    }

    public WindModel getWind() {
        return wind;
    }

    public void setWind(WindModel wind) {
        this.wind = wind;
    }

    @Override
    public String toString() {
        return "ForecastModel{" +
                "weather=" + weather +
                ", main=" + main +
                ", wind=" + wind +
                '}';
    }
}
