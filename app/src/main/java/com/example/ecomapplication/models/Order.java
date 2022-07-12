package com.example.ecomapplication.models;


import java.util.Date;

public class Order {
    private String id;
    private String id_user;
    private String orderAddress;
    private Date orderDate;
    private  Date shippedDate;
    private  int quantity;

    public Order(String id, String id_user, String orderAddress, Date orderDate, Date shippedDate, int quantity) {
        this.id = id;
        this.id_user = id_user;
        this.orderAddress = orderAddress;
        this.orderDate = orderDate;
        this.shippedDate = shippedDate;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
