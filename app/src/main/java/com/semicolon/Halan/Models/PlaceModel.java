package com.semicolon.Halan.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Emad on 2018-04-07.
 */

public class PlaceModel implements Serializable{

    @SerializedName("routes")
    List<RouteModel> routes;

    public PlaceModel() {
    }

    public PlaceModel(List<RouteModel> routes) {
        this.routes = routes;
    }

    public List<RouteModel> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteModel> routes) {
        this.routes = routes;
    }
}
