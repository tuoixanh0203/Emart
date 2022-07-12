package com.example.ecomapplication.models;

import java.util.Date;

public class SellerOrder {
    private String id_order;
    private String id_product;
    private String id_user;
    private String user_name;
    private Date orderAccept;
    private String status;
    private Date orderDate;
    private int quantity;
    private  String id_seller;
    private String idDocument;
    public SellerOrder(){};
    public SellerOrder(String id_order, String id_product, String id_user, String user_name, Date orderAccept, Date orderDate, int quantity, String status,  String id_seller) {
        this.id_order = id_order;
        this.id_product = id_product;
        this.user_name = user_name;
        this.id_user = id_user;
        this.orderAccept = orderAccept;
        this.orderDate = orderDate;
        this.quantity = quantity;
        this.status = status;
        this.id_seller= id_seller;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public String getStatus() {
        return status;
    }

    public String getId_seller() {
        return id_seller;
    }

    public void setId_seller(String id_seller) {
        this.id_seller = id_seller;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
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

    public Date getOrderAccept() {
        return orderAccept;
    }

    public void setOrderAccept(Date orderAccept) {
        this.orderAccept = orderAccept;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
