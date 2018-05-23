package com.semicolon.Halan.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NearbyModel implements Serializable{

    @SerializedName("results")
    private List<ResultObject> results;
    @SerializedName("status")
    private String status;


    public NearbyModel() {
    }

    public NearbyModel(List<ResultObject> results) {
        this.results = results;
    }

    public List<ResultObject> getResults() {
        return results;
    }

    public void setResults(List<ResultObject> results) {
        this.results = results;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class ResultObject
    {
        @SerializedName("geometry")
        private GeometryObject geometryObject;
        @SerializedName("icon")
        private String icon;
        @SerializedName("name")
        private String name;
        @SerializedName("place_id")
        private String place_id;

        public ResultObject() {
        }

        public ResultObject(GeometryObject geometryObject, String icon, String name, String place_id) {
            this.geometryObject = geometryObject;
            this.icon = icon;
            this.name = name;
            this.place_id = place_id;
        }

        public GeometryObject getGeometryObject() {
            return geometryObject;
        }

        public void setGeometryObject(GeometryObject geometryObject) {
            this.geometryObject = geometryObject;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlace_id() {
            return place_id;
        }
    }
    public class GeometryObject
    {
        @SerializedName("location")
        private LocationObject locationObject;

        public GeometryObject() {
        }

        public GeometryObject(LocationObject locationObject) {
            this.locationObject = locationObject;
        }

        public LocationObject getLocationObject() {
            return locationObject;
        }

        public void setLocationObject(LocationObject locationObject) {
            this.locationObject = locationObject;
        }
    }
    public class LocationObject
    {
        private double lat;
        private double lng;

        public LocationObject() {
        }

        public LocationObject(double lat, double lng) {
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
