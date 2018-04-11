package com.semicolon.Halan.Models;

import java.io.Serializable;

/**
 * Created by Delta on 11/04/2018.
 */

public class AvailableDriversModel implements Serializable {
    private String driver_id;
    private String user_google_lat;
    private String user_google_long;
    private String user_name;
    private String user_phone;
    private String user_photo;

    public AvailableDriversModel() {
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getUser_google_lat() {
        return user_google_lat;
    }

    public void setUser_google_lat(String user_google_lat) {
        this.user_google_lat = user_google_lat;
    }

    public String getUser_google_long() {
        return user_google_long;
    }

    public void setUser_google_long(String user_google_long) {
        this.user_google_long = user_google_long;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }
}
