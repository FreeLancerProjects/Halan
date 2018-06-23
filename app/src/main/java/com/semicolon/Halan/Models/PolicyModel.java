package com.semicolon.Halan.Models;

import java.io.Serializable;

public class PolicyModel implements Serializable {
    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
