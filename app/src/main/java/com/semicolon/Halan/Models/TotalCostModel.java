package com.semicolon.Halan.Models;

import java.io.Serializable;

/**
 * Created by Delta on 11/04/2018.
 */

public class TotalCostModel implements Serializable {
    private String total_cost;

    public TotalCostModel() {
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }
}
