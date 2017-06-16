package com.evariant.platform.msawetness.model;

/**
 * Created by Peter Zhou on 06/12/2017.
 */

public class PrecipitationRecord {

    private String wban;
    private String yearMonthDay;
    private int hour;
    private double precipitation;
    private String precipitationFlag;

    public PrecipitationRecord(String wban, String yearMonthDay, int hour, double precipitation, String precipitationFlag) {
        this.wban = wban;
        this.yearMonthDay = yearMonthDay;
        this.hour = hour;
        this.precipitation = precipitation;
        this.precipitationFlag = precipitationFlag;
    }

    public boolean rained() {
        return this.precipitation > 0D;
    }

    public boolean isDay() {
        return this.hour > 7;
    }

    public String getWban() {
        return wban;
    }

    public String getYearMonthDay() {
        return yearMonthDay;
    }

    public int getHour() {
        return hour;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public String getPrecipitationFlag() {
        return precipitationFlag;
    }

}