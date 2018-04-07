package com.semicolon.Halan.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Emad on 2018-04-07.
 */

public class RouteModel implements Serializable{

    @SerializedName("legs")
    List<LegsModel> legs;

    public RouteModel() {
    }

    public RouteModel(List<LegsModel> legs) {
        this.legs = legs;
    }

    public List<LegsModel> getLegs() {
        return legs;
    }

    public void setLegs(List<LegsModel> legs) {
        this.legs = legs;
    }
}
