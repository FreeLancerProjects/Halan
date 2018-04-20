package com.semicolon.Halan.Models;

/**
 * Created by Delta on 13/04/2018.
 */

public class ClientNotificationModel {
    private String message_id;
    private String order_id;
    private String driver_id_fk;
    private String client_id_fk;
    private String messages_status;
    private String driver_name;
    private String driver_phone;
    private String driver_evaluation_count;
    private String driver_client_count;
    private String order_cost;
    private String driver_image;
    private String order_details;
    private String order_date;
    private double rate_evaluation;
    private double stars_evaluation;

    public ClientNotificationModel() {
    }

    public ClientNotificationModel(String message_id, String order_id, String driver_id_fk, String client_id_fk, String messages_status, String driver_name, String driver_phone, String driver_evaluation_count, String driver_client_count, String order_cost, String driver_image, String order_details, String order_date, double rate_evaluation, double stars_evaluation) {
        this.message_id = message_id;
        this.order_id = order_id;
        this.driver_id_fk = driver_id_fk;
        this.client_id_fk = client_id_fk;
        this.messages_status = messages_status;
        this.driver_name = driver_name;
        this.driver_phone = driver_phone;
        this.driver_evaluation_count = driver_evaluation_count;
        this.driver_client_count = driver_client_count;
        this.order_cost = order_cost;
        this.driver_image = driver_image;
        this.order_details = order_details;
        this.order_date = order_date;
        this.rate_evaluation = rate_evaluation;
        this.stars_evaluation = stars_evaluation;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getOrder_id_fk() {
        return order_id;
    }

    public void setOrder_id_fk(String order_id_fk) {
        this.order_id = order_id_fk;
    }

    public String getDriver_id_fk() {
        return driver_id_fk;
    }

    public void setDriver_id_fk(String driver_id_fk) {
        this.driver_id_fk = driver_id_fk;
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

    public String getOrder_cost() {
        return order_cost;
    }

    public void setOrder_cost(String order_cost) {
        this.order_cost = order_cost;
    }

    public String getDriver_image() {
        return driver_image;
    }

    public void setDriver_image(String driver_image) {
        this.driver_image = driver_image;
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

    public void setRate_evaluation(double rate_evaluation) {
        this.rate_evaluation = rate_evaluation;
    }

    public double getStars_evaluation() {
        return stars_evaluation;
    }

    public void setStars_evaluation(double stars_evaluation) {
        this.stars_evaluation = stars_evaluation;
    }
}
