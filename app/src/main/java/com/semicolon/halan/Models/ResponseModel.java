package com.semicolon.halan.Models;

/**
 * Created by Delta on 03/04/2018.
 */

public class ResponseModel {
    private String user_id;
    private String user_type;
    private int success;
    private String message;

    public ResponseModel() {
    }

    public ResponseModel(String user_id, String user_type, int success, String message) {
        this.user_id = user_id;
        this.user_type = user_type;
        this.success = success;
        this.message = message;
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

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
