package com.assignment.kaplan;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class MainModel implements Serializable {

    private String temp, pressure, humidity;

    public MainModel(String temp, String pressure, String humidity) {
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public MainModel() {
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    @NonNull
    @Override
    public String toString() {
        return "MainModel{" +
                "temp='" + temp + '\'' +
                ", pressure='" + pressure + '\'' +
                ", humidity='" + humidity + '\'' +
                '}';
    }
}
