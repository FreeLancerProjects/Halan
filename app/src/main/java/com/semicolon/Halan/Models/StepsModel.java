package com.semicolon.Halan.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Emad on 2018-04-07.
 */

public class StepsModel implements Serializable {

    @SerializedName("start_location")
    private StartLoc start_location;
    @SerializedName("end_location")
    private EndLoc end_location;

    public StepsModel() {
    }

    public StepsModel(StartLoc start_location, EndLoc end_location) {
        this.start_location = start_location;
        this.end_location = end_location;
    }

    public StartLoc getStart_location() {
        return start_location;
    }

    public void setStart_location(StartLoc start_location) {
        this.start_location = start_location;
    }

    public EndLoc getEnd_location() {
        return end_location;
    }

    public void setEnd_location(EndLoc end_location) {
        this.end_location = end_location;
    }

    public class StartLoc
    {
        @SerializedName("lat")
        private double lat;
        @SerializedName("lng")
        private double lng;

        public StartLoc() {
        }

        public StartLoc(double lat, double lng) {
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
    public class EndLoc
    {
        @SerializedName("lat")
        private double lat;
        @SerializedName("lng")
        private double lng;

        public EndLoc() {
        }

        public EndLoc(double lat, double lng) {
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
}
