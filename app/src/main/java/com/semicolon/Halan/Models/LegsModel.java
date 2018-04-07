package com.semicolon.Halan.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Emad on 2018-04-07.
 */

public class LegsModel implements Serializable{
    @SerializedName("distance")
    DistanceModel distance;
    @SerializedName("duration")
    DurationModel duration;
    @SerializedName("steps")
    List<StepsModel> steps;

    public LegsModel() {
    }

    public LegsModel(DistanceModel distance, DurationModel duration, List<StepsModel> steps) {
        this.distance = distance;
        this.duration = duration;
        this.steps = steps;
    }

    public DistanceModel getDistance() {
        return distance;
    }

    public void setDistance(DistanceModel distance) {
        this.distance = distance;
    }

    public DurationModel getDuration() {
        return duration;
    }

    public void setDuration(DurationModel duration) {
        this.duration = duration;
    }

    public List<StepsModel> getSteps() {
        return steps;
    }

    public void setSteps(List<StepsModel> steps) {
        this.steps = steps;
    }
}
