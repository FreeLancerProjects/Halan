package com.semicolon.Halan.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Emad on 2018-04-07.
 */

public class PolylineModel implements Serializable{
    @SerializedName("points")
    private String points;

    public PolylineModel() {
    }

    public PolylineModel(String points) {
        this.points = points;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
