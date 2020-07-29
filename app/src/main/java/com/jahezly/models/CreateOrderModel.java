package com.jahezly.models;

import java.io.Serializable;

public class CreateOrderModel implements Serializable {
    private String id;
    private String client_name;
    private String client_phone;
    private String number_of_person;
    private String number_of_child;
    private String details;
    private String order_date;
    private String order_time;
    private String session_place;
    private String session_type;


    public CreateOrderModel(String id, String client_name, String client_phone, String number_of_person, String number_of_child, String details, String order_date, String order_time, String session_place, String session_type) {
        this.id = id;
        this.client_name = client_name;
        this.client_phone = client_phone;
        this.number_of_person = number_of_person;
        this.number_of_child = number_of_child;
        this.details = details;
        this.order_date = order_date;
        this.order_time = order_time;
        this.session_place = session_place;
        this.session_type = session_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_phone() {
        return client_phone;
    }

    public void setClient_phone(String client_phone) {
        this.client_phone = client_phone;
    }

    public String getNumber_of_person() {
        return number_of_person;
    }

    public void setNumber_of_person(String number_of_person) {
        this.number_of_person = number_of_person;
    }

    public String getNumber_of_child() {
        return number_of_child;
    }

    public void setNumber_of_child(String number_of_child) {
        this.number_of_child = number_of_child;
    }


    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getSession_place() {
        return session_place;
    }

    public void setSession_place(String session_place) {
        this.session_place = session_place;
    }

    public String getSession_type() {
        return session_type;
    }

    public void setSession_type(String session_type) {
        this.session_type = session_type;
    }
}
