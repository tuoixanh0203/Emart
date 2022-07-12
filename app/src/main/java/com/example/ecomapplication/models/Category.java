package com.example.ecomapplication.models;

public class Category {
    String img_url;
    String name;
    String type;
    String id_category;

    public Category() {
    }

    public Category(String img_url, String name, String type, String id_category) {
        this.img_url = img_url;
        this.name = name;
        this.type = type;
        this.id_category = id_category;
    }
    public Category(String img_url, String name, String type) {
        this.img_url = img_url;
        this.name = name;
        this.type = type;
    }
    public String getId_category() {
        return id_category;
    }

    public void setId_category(String id_category) {
        this.id_category = id_category;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
