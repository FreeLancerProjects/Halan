package com.semicolon.Halan.Models;

import java.io.Serializable;

public class DriverAcceptModel implements Serializable{
    String user_id;

    public DriverAcceptModel() {
    }

    public DriverAcceptModel(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
