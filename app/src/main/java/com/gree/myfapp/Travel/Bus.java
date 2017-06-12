package com.gree.myfapp.Travel;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by asus on 2016/11/8.
 */
public class Bus {
    private String name;
    private LatLng location;
    private String yLocation;
    private String to;
    private double distance;

    public Bus() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getyLocation() {
        return yLocation;
    }

    public void setyLocation(String yLocation) {
        this.yLocation = yLocation;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
