package com.semicolon.Halan.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Emad on 2018-04-07.
 */

public class DurationModel implements Serializable{
    @SerializedName("text")
    private String text;

    public DurationModel() {
    }

    public DurationModel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
