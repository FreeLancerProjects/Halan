package com.semicolon.Halan.Models;

import java.io.Serializable;

public class AppRateModel implements Serializable{
    private int app_evaluation;
    private int success;

    public int getApp_evaluation() {
        return app_evaluation;
    }

    public int getSuccess() {
        return success;
    }
}
