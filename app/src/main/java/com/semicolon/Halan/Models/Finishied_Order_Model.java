package com.semicolon.Halan.Models;

import java.io.Serializable;

public class Finishied_Order_Model implements Serializable {
    private String driver_id;
    private String driver_name;
    private String driver_image;
    private String order_id;
    private String order_details;

    public Finishied_Order_Model() {
    }

    public Finishied_Order_Model(String driver_id, String driver_name, String driver_image, String order_id, String order_details) {
        this.driver_id = driver_id;
        this.driver_name = driver_name;
        this.driver_image = driver_image;
        this.order_id = order_id;
        this.order_details = order_details;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_image() {
        return driver_image;
    }

    public void setDriver_image(String driver_image) {
        this.driver_image = driver_image;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_details() {
        return order_details;
    }

    public void setOrder_details(String order_details) {
        this.order_details = order_details;
    }
}
