package com.assignment.kaplan;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class WeatherModel implements Serializable {

    private String main,description;

    public WeatherModel(String main, String description) {
        this.main = main;
        this.description = description;
    }

    public WeatherModel() {
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return "WeatherModel{" +
                "main='" + main + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
