package com.semicolon.Halan.Models;

import java.io.Serializable;

/**
 * Created by Emad on 2018-04-07.
 */

public class PlaceModel implements Serializable{

    /*
    @SerializedName("routes")
    List<Route> routes;

    public PlaceModel() {
    }

   *//* public PlaceModel(List<RouteModel> routes) {
        this.routes = routes;
    }

    public List<RouteModel> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteModel> routes) {
        this.routes = routes;
    }*//*

    public PlaceModel(List<Route> routes) {
        this.routes = routes;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public class Route
    {
        @SerializedName("legs")
        private List<Legs> legs;
        @SerializedName("overview_polyline")
        private Overview_polyline overview_polyline;

        public Route() {
        }

        public Route(List<Legs> legs, Overview_polyline overview_polyline) {
            this.legs = legs;
            this.overview_polyline = overview_polyline;
        }

        public List<Legs> getLegs() {
            return legs;
        }

        public void setLegs(List<Legs> legs) {
            this.legs = legs;
        }

        public Overview_polyline getOverview_polyline() {
            return overview_polyline;
        }

        public void setOverview_polyline(Overview_polyline overview_polyline) {
            this.overview_polyline = overview_polyline;
        }
    }
    public class Legs
    {
        @SerializedName("distance")
        private Distance distance;
        @SerializedName("duration")
        private Duration duration;
        @SerializedName("start_location")
        private Location start_location;
        @SerializedName("end_location")
        private Location end_location;
        @SerializedName("steps")
        private List<Steps> steps;

        public Legs() {
        }

        public Legs(Distance distance, Duration duration, Location start_location, Location end_location, List<Steps> steps) {
            this.distance = distance;
            this.duration = duration;
            this.start_location = start_location;
            this.end_location = end_location;
            this.steps = steps;
        }

        public Distance getDistance() {
            return distance;
        }

        public void setDistance(Distance distance) {
            this.distance = distance;
        }

        public Duration getDuration() {
            return duration;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        public Location getStart_location() {
            return start_location;
        }

        public void setStart_location(Location start_location) {
            this.start_location = start_location;
        }

        public Location getEnd_location() {
            return end_location;
        }

        public void setEnd_location(Location end_location) {
            this.end_location = end_location;
        }

        public List<Steps> getSteps() {
            return steps;
        }

        public void setSteps(List<Steps> steps) {
            this.steps = steps;
        }
    }
    public class Distance
    {
        @SerializedName("text")
        private String text;

        public Distance() {
        }

        public Distance(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
    public class Duration
    {
        @SerializedName("text")
        private String text;

        public Duration() {
        }

        public Duration(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
    public class Overview_polyline
    {
        @SerializedName("points")
        private String points;

        public Overview_polyline() {
        }

        public Overview_polyline(String points) {
            this.points = points;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }
    }
    public class Location
    {
        @SerializedName("lat")
        private double lat;
        @SerializedName("lng")
        private double lng;

        public Location() {
        }

        public Location(double lat, double lng) {
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
    public class Steps
    {
        @SerializedName("polyline")
        private Polyline polyline;

        public Steps() {
        }

        public Steps(Polyline polyline) {
            this.polyline = polyline;
        }

        public Polyline getPolyline() {
            return polyline;
        }

        public void setPolyline(Polyline polyline) {
            this.polyline = polyline;
        }
    }
    public class Polyline
    {
        @SerializedName("points")
        private String points;

        public Polyline() {
        }

        public Polyline(String points) {
            this.points = points;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }
    }*/


}
