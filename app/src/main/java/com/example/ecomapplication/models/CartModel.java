package com.example.ecomapplication.models;


import java.util.List;

public class CartModel {
    private String id;
    private String id_product;
    private String id_user;
    private int quantity;
    private Product product;
    public CartModel(){}

    public CartModel(String id, String id_product, String id_user, int quantity, Product product) {
        this.id = id;
        this.id_product = id_product;
        this.id_user = id_user;
        this.quantity = quantity;
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product productList) {
        this.product = productList;
    }
}
