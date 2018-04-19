package com.semicolon.Halan.Models;

import java.io.Serializable;

/**
 * Created by Delta on 19/04/2018.
 */

public class Typing implements Serializable {
    public boolean type;

    public Typing() {
    }

    public Typing(boolean type) {
        this.type = type;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
