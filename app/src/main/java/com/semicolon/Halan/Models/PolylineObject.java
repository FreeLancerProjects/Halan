package com.semicolon.Halan.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Emad on 2018-04-07.
 */

public class PolylineObject implements Serializable {
    @SerializedName("overview_polyline")
    PolylineModel polyline;

    public PolylineObject() {
    }

    public PolylineObject(PolylineModel polyline) {
        this.polyline = polyline;
    }

    public PolylineModel getPolyline() {
        return polyline;
    }

    public void setPolyline(PolylineModel polyline) {
        this.polyline = polyline;
    }
}
