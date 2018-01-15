package com.tools.simron.weightcheckapp;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by simro on 11/8/2017.
 */

public class WeightInfo implements Serializable, Comparable<WeightInfo>{
    private long date;
    private String userId;
    private int hour;
    private int min;
    private double weight;
    private String note;
    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getMin() {
        return min;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public long getDate() {
        return date;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int compareTo(@NonNull WeightInfo weightInfo) {
        return Long.compare(weightInfo.date, this.date);
    }
}
