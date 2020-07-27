package com.jahezly.models;

import java.io.Serializable;

public class SnaksModel implements Serializable {
    private int id;
    private String order_id;
    private String detail_id;
    private String snak_id;
    private String snak_name;
    private String amount;
    private String price;

    public int getId() {
        return id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getDetail_id() {
        return detail_id;
    }

    public String getSnak_id() {
        return snak_id;
    }

    public String getSnak_name() {
        return snak_name;
    }

    public String getAmount() {
        return amount;
    }

    public String getPrice() {
        return price;
    }
}
