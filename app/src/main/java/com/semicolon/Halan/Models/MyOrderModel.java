package com.semicolon.Halan.Models;

import java.io.Serializable;

/**
 * Created by Elashry on 10/04/2018.
 */

public class MyOrderModel implements Serializable{
    private String client_location;
    private String market_location;
    private String cost;
    private String order_start_time;
    private String client_name;
    private String order_start_from_minute;
    private Double client_google_lang;
    private Double client_google_lat;
    private String order_details;
    private Double market_google_lang;
    private Double market_google_lat;
    private String client_phone;
    private String driver_name;
    private String message_id;
    private String order_id;
    private String client_id_fk;
    private String order_driver_cost;
    private String messages_status;
    private String order_date;
    private String driver_photo;
    private String driver_id;
    private String driver_car_num;
    private String driver_car_model;
    private String client_photo;
    private String client_id;
    private String client_email;
    private String room_id;

    public String getRoom_id() {
        return room_id;
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

    public Double getClient_google_lang() {
        return client_google_lang;
    }

    public void setClient_google_lang(Double client_google_lang) {
        this.client_google_lang = client_google_lang;
    }

    public Double getClient_google_lat() {
        return client_google_lat;
    }

    public void setClient_google_lat(Double client_google_lat) {
        this.client_google_lat = client_google_lat;
    }

    public String getOrder_details() {
        return order_details;
    }

    public void setOrder_details(String order_details) {
        this.order_details = order_details;
    }

    public Double getMarket_google_lang() {
        return market_google_lang;
    }

    public void setMarket_google_lang(Double market_google_lang) {
        this.market_google_lang = market_google_lang;
    }

    public Double getMarket_google_lat() {
        return market_google_lat;
    }

    public void setMarket_google_lat(Double market_google_lat) {
        this.market_google_lat = market_google_lat;
    }

    public String getClient_phone() {
        return client_phone;
    }

    public void setClient_phone(String client_phone) {
        this.client_phone = client_phone;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getClient_id_fk() {
        return client_id_fk;
    }

    public void setClient_id_fk(String client_id_fk) {
        this.client_id_fk = client_id_fk;
    }

    public String getOrder_driver_cost() {
        return order_driver_cost;
    }

    public void setOrder_driver_cost(String order_driver_cost) {
        this.order_driver_cost = order_driver_cost;
    }

    public String getMessages_status() {
        return messages_status;
    }

    public void setMessages_status(String messages_status) {
        this.messages_status = messages_status;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getDriver_photo() {
        return driver_photo;
    }

    public void setDriver_photo(String driver_photo) {
        this.driver_photo = driver_photo;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_car_num() {
        return driver_car_num;
    }

    public void setDriver_car_num(String driver_car_num) {
        this.driver_car_num = driver_car_num;
    }

    public String getDriver_car_model() {
        return driver_car_model;
    }

    public void setDriver_car_model(String driver_car_model) {
        this.driver_car_model = driver_car_model;
    }

    public String getClient_photo() {
        return client_photo;
    }

    public void setClient_photo(String client_photo) {
        this.client_photo = client_photo;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_email() {
        return client_email;
    }

    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }
}
