package com.jahezly.models;

import java.io.Serializable;
import java.util.List;

public class FoodsModel implements Serializable {
    private int id;
    private String order_id;
    private String price;
    private String amount;
    private FoodInfoModel food_info;
    private List<SnaksModel> snaks;

    public int getId() {
        return id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getPrice() {
        return price;
    }

    public String getAmount() {
        return amount;
    }

    public FoodInfoModel getFood_info() {
        return food_info;
    }

    public List<SnaksModel> getSnaks() {
        return snaks;
    }

    public static class FoodInfoModel implements Serializable{
        private int id;
        private String title;
        private String image;
        private String restaurant_id;
        private String food_departemnt_id;
        private String food_sub_departemnt_id;
        private String price;
        private String calories;
        private String contents;
        private String have_offer;
        private String offer_type;
        private String offer_value;


        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getImage() {
            return image;
        }

        public String getRestaurant_id() {
            return restaurant_id;
        }

        public String getFood_departemnt_id() {
            return food_departemnt_id;
        }

        public String getFood_sub_departemnt_id() {
            return food_sub_departemnt_id;
        }

        public String getPrice() {
            return price;
        }

        public String getCalories() {
            return calories;
        }

        public String getContents() {
            return contents;
        }

        public String getHave_offer() {
            return have_offer;
        }

        public String getOffer_type() {
            return offer_type;
        }

        public String getOffer_value() {
            return offer_value;
        }
    }

}
