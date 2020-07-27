package com.jahezly.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private User restaurant;

    public User getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(User restaurant) {
        this.restaurant = restaurant;
    }

    public static class User implements Serializable {
        private int id;
        private String admin_type;
        private String name;
        private String user_name;
        private String email;
        private String phone_code;
        private String phone;
        private String parent;
        private String logo;
        private String banner;
        private String rating;
        private String latitude;
        private String longitude;
        private String address;
        private String city_id;
        private String neighbor_id;
        private String foods;
        private String min_price;
        private String max_price;
        private String child_from;
        private String child_to;
        private String work_from;
        private String work_to;
        private String session_place;
        private String session_type;
        private String days;
        private String payment_type;
        private String website_address;
        private String parking;
        private String details;
        private String firebase_token ="";
        private String token;

        public int getId() {
            return id;
        }

        public String getAdmin_type() {
            return admin_type;
        }

        public String getName() {
            return name;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone_code() {
            return phone_code;
        }

        public String getPhone() {
            return phone;
        }

        public String getLogo() {
            return logo;
        }

        public String getBanner() {
            return banner;
        }

        public String getRating() {
            return rating;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getAddress() {
            return address;
        }

        public String getCity_id() {
            return city_id;
        }

        public String getNeighbor_id() {
            return neighbor_id;
        }

        public String getFoods() {
            return foods;
        }

        public String getMin_price() {
            return min_price;
        }

        public String getMax_price() {
            return max_price;
        }

        public String getChild_from() {
            return child_from;
        }

        public String getChild_to() {
            return child_to;
        }

        public String getWork_from() {
            return work_from;
        }

        public String getWork_to() {
            return work_to;
        }

        public String getSession_place() {
            return session_place;
        }

        public String getSession_type() {
            return session_type;
        }

        public String getDays() {
            return days;
        }

        public String getPayment_type() {
            return payment_type;
        }

        public String getWebsite_address() {
            return website_address;
        }

        public String getParking() {
            return parking;
        }

        public String getDetails() {
            return details;
        }

        public String getParent() {
            return parent;
        }

        public String getFirebase_token() {
            return firebase_token;
        }

        public void setFirebase_token(String firebase_token) {
            this.firebase_token = firebase_token;
        }

        public String getToken() {
            return token;
        }
    }
}
