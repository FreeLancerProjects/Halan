package com.semicolon.Halan.Models;

import java.io.Serializable;

public class TypingModel implements Serializable{
    private String typing_value;

    public TypingModel(String typing_value) {
        this.typing_value = typing_value;
    }

    public String getTyping_value() {
        return typing_value;
    }
}
