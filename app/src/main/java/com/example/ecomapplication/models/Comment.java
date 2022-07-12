package com.example.ecomapplication.models;

import com.google.firebase.database.ServerValue;

import java.util.Date;
import java.util.List;

public class Comment {
    private String content;
    private Date date;
    String id_product;
    String id_user;
    String user_img;

    public Comment(){

    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public Comment(String content, String id_product, String id_user, String user_img) {
        this.content = content;
        this.id_product = id_product;
        this.id_user = id_user;
        this.date = new Date();
        this.user_img = user_img;
    }

    public Comment(String content, Date date, String id_product, String id_user, String user_img) {
        this.content = content;
        this.date = date;
        this.id_product = id_product;
        this.id_user = id_user;
        this.user_img = user_img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

}
