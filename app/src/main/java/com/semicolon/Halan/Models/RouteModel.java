package com.semicolon.Halan.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Emad on 2018-04-07.
 */

public class RouteModel implements Serializable{

    @SerializedName("overview_polyline")
    PolylineModel overview_polyline;
    @SerializedName("legs")
    List<LegsModel> legs;

    public RouteModel() {
    }

    public RouteModel(PolylineModel overview_polyline, List<LegsModel> legs) {
        this.overview_polyline = overview_polyline;
        this.legs = legs;
    }

    public PolylineModel getOverview_polyline() {
        return overview_polyline;
    }

    public void setOverview_polyline(PolylineModel overview_polyline) {
        this.overview_polyline = overview_polyline;
    }

    public List<LegsModel> getLegs() {
        return legs;
    }

    public void setLegs(List<LegsModel> legs) {
        this.legs = legs;
    }
}
