package com.example.ecomapplication.models;

public class SellerInfo {
    private String address;
    private String email;
    private String id;
    private String phone;
    private String shopName;

    public SellerInfo(){

    }

    public SellerInfo(String address, String email, String id, String phone, String shopName) {
        this.address = address;
        this.email = email;
        this.id = id;
        this.phone = phone;
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
