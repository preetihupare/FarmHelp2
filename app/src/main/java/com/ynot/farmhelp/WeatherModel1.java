package com.ynot.farmhelp;

public class WeatherModel1 {

    private String dayname;
    private String temp;
    private String tempicon;
    private String tempcondi;

    public WeatherModel1(String time, String temperature, String icon, String windSpeed) {
        this.dayname = time;
        this.temp = temperature;
        this.tempicon = icon;
        this.tempcondi = windSpeed;
    }


    public String getDayname() {
        return dayname;
    }

    public void setDayname(String dayname) {
        this.dayname = dayname;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTempicon() {
        return tempicon;
    }

    public void setTempicon(String tempicon) {
        this.tempicon = tempicon;
    }

    public String getTempcondi() {
        return tempcondi;
    }

    public void setTempcondi(String tempcondi) {
        this.tempcondi = tempcondi;
    }
}

