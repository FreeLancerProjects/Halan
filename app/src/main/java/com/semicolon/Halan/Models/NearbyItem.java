package com.semicolon.Halan.Models;

import java.io.Serializable;

public class NearbyItem implements Serializable{
    private String icon;
    private String name;
    private double distance;
    private double lat;
    private double lng;

    public NearbyItem() {
    }

    public NearbyItem(String icon, String name, double distance, double lat, double lng) {
        this.icon = icon;
        this.name = name;
        this.distance = distance;
        this.lat = lat;
        this.lng = lng;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
