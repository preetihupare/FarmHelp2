package com.ynot.farmhelp;

public class WeatherModel1 {

    private String date;
    private String time;
    private String temperature;
    private String description;
    private String humidity;
    private String clouds;
    private String windSpeed;
    private String isRain;
    //private String dayNight;

    public WeatherModel1(String date, String time, String temperature, String description, String humidity, String clouds, String windSpeed, String isRain) {
        this.date = date;
        this.time = time;
        this.temperature = temperature;
        this.description = description;
        this.humidity = humidity;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.isRain = isRain;
        //this.dayNight = dayNight;
    }

//    public String getDayNight() {
//        return dayNight;
//    }
//
//    public void setDayNight(String dayNight) {
//        this.dayNight = dayNight;
//    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getClouds() {
        return clouds;
    }

    public void setClouds(String clouds) {
        this.clouds = clouds;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getIsRain() {
        return isRain;
    }

    public void setIsRain(String isRain) {
        this.isRain = isRain;
    }
}

