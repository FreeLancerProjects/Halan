package com.semicolon.Halan.Models;

/**
 * Created by Elashry on 10/04/2018.
 */


public class MyOrderModel {
    private String client_location;
    private String market_location;
    private String cost;
    private String order_start_time;
    private String client_name;
    private String order_start_from_minute;

    public MyOrderModel(String client_location, String market_location, String cost, String order_start_time, String client_name, String order_start_from_minute) {
        this.client_location = client_location;
        this.market_location = market_location;
        this.cost = cost;
        this.order_start_time = order_start_time;
        this.client_name = client_name;
        this.order_start_from_minute = order_start_from_minute;
    }

    public String getClient_location() {
        return client_location;
    }

    public void setClient_location(String client_location) {
        this.client_location = client_location;
    }

    public String getMarket_location() {
        return market_location;
    }

    public void setMarket_location(String market_location) {
        this.market_location = market_location;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getOrder_start_time() {
        return order_start_time;
    }

    public void setOrder_start_time(String order_start_time) {
        this.order_start_time = order_start_time;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getOrder_start_from_minute() {
        return order_start_from_minute;
    }

    public void setOrder_start_from_minute(String order_start_from_minute) {
        this.order_start_from_minute = order_start_from_minute;
    }
}
