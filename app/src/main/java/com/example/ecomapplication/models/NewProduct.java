package com.example.ecomapplication.models;

import java.io.Serializable;

public class NewProduct implements Serializable {
    String id;
    String description;
    String name;
    // String rating;
    int price;
    String img_url;

    public  NewProduct() {

    }

    public NewProduct(String id, String description, String name, int price, String img_url) {
        this.id = id;
        this.description = description;
        this.name = name;
        // this.rating = rating;
        this.price = price;
        this.img_url = img_url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getRating() {
//        return rating;
//    }
//
//    public void setRating(String rating) {
//        this.rating = rating;
//    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDescription() {
        return description;
    }
}
