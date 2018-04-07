package com.semicolon.Halan.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Emad on 2018-04-07.
 */

public class StepsModel implements Serializable {
    @SerializedName("polyline")
    PolylineModel polyline;

    public StepsModel() {
    }

    public StepsModel(PolylineModel polyline) {
        this.polyline = polyline;
    }

    public PolylineModel getPolyline() {
        return polyline;
    }

    public void setPolyline(PolylineModel polyline) {
        this.polyline = polyline;
    }
}
