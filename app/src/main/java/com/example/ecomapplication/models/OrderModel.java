package com.example.ecomapplication.models;


import java.io.Serializable;
import java.util.Date;

public class OrderModel implements Serializable {
    private String id;
    private String id_user;
    private String orderAddress;
    private Date orderDate;
    private  Date shippedDate;
    private String status;
    private  int total;
    public OrderModel(){

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

    public int getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public OrderModel(String id, String id_user, String orderAddress, Date orderDate, Date shippedDate, String status,int total) {
        this.id = id;
        this.id_user = id_user;
        this.orderAddress = orderAddress;
        this.orderDate = orderDate;
        this.shippedDate = shippedDate;
        this.total = total;
        this.status = status;
    }


}
