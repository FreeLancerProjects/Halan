package com.semicolon.Halan.Models;

/**
 * Created by Elashry on 30/03/2018.
 */

public class UserModel {

    private String user_id;
    private String user_type;
    private String user_name;
    private String user_phone;
    private String user_email;
    private String user_token_id;
    private String user_photo;
    private String user_city;
    private String user_national_num;
    private String user_car_num     ;
    private String user_car_model   ;
    private String user_car_color   ;
    private String user_car_license ;
    private String user_car_form    ;
    private String user_car_photo   ;
    private String user_confirm_code;
    private String date_registration;
    private String user_google_lat  ;
    private String user_google_long ;
    private String user_pass        ;

    private int success;

    public UserModel() {
    }

    public UserModel(String user_id, String user_type, String user_name, String user_pass, String user_phone, String user_email, String user_token_id, String user_photo, String user_city, String user_national_num, String user_car_num, String user_car_model, String user_car_color, String user_car_license, String user_car_form, String user_car_photo, String user_confirm_code, String date_registration, String user_google_lat, String user_google_long, int success) {
        this.user_id = user_id;
        this.user_type = user_type;
        this.user_name = user_name;
        this.user_pass = user_pass;
        this.user_phone = user_phone;
        this.user_email = user_email;
        this.user_token_id = user_token_id;
        this.user_photo = user_photo;
        this.user_city = user_city;
        this.user_national_num = user_national_num;
        this.user_car_num = user_car_num;
        this.user_car_model = user_car_model;
        this.user_car_color = user_car_color;
        this.user_car_license = user_car_license;
        this.user_car_form = user_car_form;
        this.user_car_photo = user_car_photo;
        this.user_confirm_code = user_confirm_code;
        this.date_registration = date_registration;
        this.user_google_lat = user_google_lat;
        this.user_google_long = user_google_long;
        this.success = success;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_token_id() {
        return user_token_id;
    }

    public void setUser_token_id(String user_token_id) {
        this.user_token_id = user_token_id;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getUser_city() {
        return user_city;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }

    public String getUser_national_num() {
        return user_national_num;
    }

    public void setUser_national_num(String user_national_num) {
        this.user_national_num = user_national_num;
    }

    public String getUser_car_num() {
        return user_car_num;
    }

    public void setUser_car_num(String user_car_num) {
        this.user_car_num = user_car_num;
    }

    public String getUser_car_model() {
        return user_car_model;
    }

    public void setUser_car_model(String user_car_model) {
        this.user_car_model = user_car_model;
    }

    public String getUser_car_color() {
        return user_car_color;
    }

    public void setUser_car_color(String user_car_color) {
        this.user_car_color = user_car_color;
    }

    public String getUser_car_license() {
        return user_car_license;
    }

    public void setUser_car_license(String user_car_license) {
        this.user_car_license = user_car_license;
    }

    public String getUser_car_form() {
        return user_car_form;
    }

    public void setUser_car_form(String user_car_form) {
        this.user_car_form = user_car_form;
    }

    public String getUser_car_photo() {
        return user_car_photo;
    }

    public void setUser_car_photo(String user_car_photo) {
        this.user_car_photo = user_car_photo;
    }

    public String getUser_confirm_code() {
        return user_confirm_code;
    }

    public void setUser_confirm_code(String user_confirm_code) {
        this.user_confirm_code = user_confirm_code;
    }

    public String getDate_registration() {
        return date_registration;
    }

    public void setDate_registration(String date_registration) {
        this.date_registration = date_registration;
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

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
