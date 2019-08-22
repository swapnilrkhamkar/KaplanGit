package com.assignment.kaplan;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class WindModel implements Serializable {

    private String speed, deg;

    public WindModel(String speed, String deg) {
        this.speed = speed;
        this.deg = deg;
    }

    public WindModel() {
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDeg() {
        return deg;
    }

    public void setDeg(String deg) {
        this.deg = deg;
    }

    @NonNull
    @Override
    public String toString() {
        return "WindModel{" +
                "speed='" + speed + '\'' +
                ", deg='" + deg + '\'' +
                '}';
    }
}
