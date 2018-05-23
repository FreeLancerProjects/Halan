package com.semicolon.Halan.Models;

import java.io.Serializable;

public class ClientLastOrderModel implements Serializable{
    private String message_id;
    private String order_id;
    private String driver_id_fk;
    private String order_start;
    private String client_id_fk;
    private String messages_status;
    private String messages_date_s;
    private String driver_name;
    private String driver_phone;
    private String driver_evaluation_count;
    private String driver_client_count;
    private String driver_image;
    private String order_cost;
    private String order_details;
    private String order_date;
    private double rate_evaluation;
    private int stars_evaluation;
    private String order_replay_from_minute;


    public ClientLastOrderModel() {
    }

    public ClientLastOrderModel(String message_id, String order_id, String driver_id_fk, String order_start, String client_id_fk, String messages_status, String messages_date_s, String driver_name, String driver_phone, String driver_evaluation_count, String driver_client_count, String driver_image, String order_cost, String order_details, String order_date, int rate_evaluation, int stars_evaluation, String order_replay_from_minute) {
        this.message_id = message_id;
        this.order_id = order_id;
        this.driver_id_fk = driver_id_fk;
        this.order_start = order_start;
        this.client_id_fk = client_id_fk;
        this.messages_status = messages_status;
        this.messages_date_s = messages_date_s;
        this.driver_name = driver_name;
        this.driver_phone = driver_phone;
        this.driver_evaluation_count = driver_evaluation_count;
        this.driver_client_count = driver_client_count;
        this.driver_image = driver_image;
        this.order_cost = order_cost;
        this.order_details = order_details;
        this.order_date = order_date;
        this.rate_evaluation = rate_evaluation;
        this.stars_evaluation = stars_evaluation;
        this.order_replay_from_minute = order_replay_from_minute;
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

    public String getDriver_id_fk() {
        return driver_id_fk;
    }

    public void setDriver_id_fk(String driver_id_fk) {
        this.driver_id_fk = driver_id_fk;
    }

    public String getOrder_start() {
        return order_start;
    }

    public void setOrder_start(String order_start) {
        this.order_start = order_start;
    }

    public String getClient_id_fk() {
        return client_id_fk;
    }

    public void setClient_id_fk(String client_id_fk) {
        this.client_id_fk = client_id_fk;
    }

    public String getMessages_status() {
        return messages_status;
    }

    public void setMessages_status(String messages_status) {
        this.messages_status = messages_status;
    }

    public String getMessages_date_s() {
        return messages_date_s;
    }

    public void setMessages_date_s(String messages_date_s) {
        this.messages_date_s = messages_date_s;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_phone() {
        return driver_phone;
    }

    public void setDriver_phone(String driver_phone) {
        this.driver_phone = driver_phone;
    }

    public String getDriver_evaluation_count() {
        return driver_evaluation_count;
    }

    public void setDriver_evaluation_count(String driver_evaluation_count) {
        this.driver_evaluation_count = driver_evaluation_count;
    }

    public String getDriver_client_count() {
        return driver_client_count;
    }

    public void setDriver_client_count(String driver_client_count) {
        this.driver_client_count = driver_client_count;
    }

    public String getDriver_image() {
        return driver_image;
    }

    public void setDriver_image(String driver_image) {
        this.driver_image = driver_image;
    }

    public String getOrder_cost() {
        return order_cost;
    }

    public void setOrder_cost(String order_cost) {
        this.order_cost = order_cost;
    }

    public String getOrder_details() {
        return order_details;
    }

    public void setOrder_details(String order_details) {
        this.order_details = order_details;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public double getRate_evaluation() {
        return rate_evaluation;
    }

    public void setRate_evaluation(int rate_evaluation) {
        this.rate_evaluation = rate_evaluation;
    }

    public int getStars_evaluation() {
        return stars_evaluation;
    }

    public void setStars_evaluation(int stars_evaluation) {
        this.stars_evaluation = stars_evaluation;
    }

    public String getOrder_replay_from_minute() {
        return order_replay_from_minute;
    }

    public void setOrder_replay_from_minute(String order_replay_from_minute) {
        this.order_replay_from_minute = order_replay_from_minute;
    }
}
