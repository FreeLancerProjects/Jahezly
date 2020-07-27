package com.jahezly.models;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {
    private String id;
    private String order_code;
    private String order_status;
    private String order_type;
    private String user_id;
    private String restaurant_id;
    private String branch_id;
    private String coupon_id;
    private String total_price;
    private String latitude;
    private String longitude;
    private String order_date;
    private String order_time;
    private String pay_type;
    private String number_of_person;
    private String number_of_child;
    private String session_place;
    private String session_type;
    private String barcode_image;
    private String details;
    private ClientModel client;
    private List<FoodsModel> foods;
    private List<DrinkModel> drinks;


    public String getId() {
        return id;
    }

    public String getOrder_code() {
        return order_code;
    }

    public String getOrder_status() {
        return order_status;
    }

    public String getOrder_type() {
        return order_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getOrder_date() {
        return order_date;
    }

    public String getOrder_time() {
        return order_time;
    }

    public String getPay_type() {
        return pay_type;
    }

    public String getNumber_of_person() {
        return number_of_person;
    }

    public String getNumber_of_child() {
        return number_of_child;
    }

    public String getSession_place() {
        return session_place;
    }

    public String getSession_type() {
        return session_type;
    }

    public String getBarcode_image() {
        return barcode_image;
    }

    public String getDetails() {
        return details;
    }

    public ClientModel getClient() {
        return client;
    }


    public List<FoodsModel> getFoods() {
        return foods;
    }

    public List<DrinkModel> getDrinks() {
        return drinks;
    }
}
