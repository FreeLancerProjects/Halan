package com.semicolon.Halan.Models;

import java.io.Serializable;

/**
 * Created by Delta on 09/04/2018.
 */

public class LocationUpdateModel implements Serializable {
    private double lat;
    private double lng;

    public LocationUpdateModel() {
    }

    public LocationUpdateModel(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
