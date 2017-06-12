package com.gree.myfapp.Eat;

/**
 * Created by asus on 2016/10/2.
 */

public class Eat {
    private String name;
    private double price;
    private double distance;
    private int imageId;
    private String address;
    private String uid;

    public Eat() {
    }

    public Eat(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
