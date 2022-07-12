package com.example.ecomapplication.models;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class UserInfo {
    private List<String>  address;
    private  String city;
    private Date date;
    private String email;
    private String firstName;
    private String id;
    private String lastName;
    private String phone;
    private boolean is_seller;
    private String documentId;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public UserInfo(){

    }
    public UserInfo(List<String> address, String city, Date date, String email, String firstName, String id, String lastName, String phone, boolean is_seller) {
        this.address = address;
        this.city = city;
        this.date = date;
        this.email = email;
        this.firstName = firstName;
        this.id = id;
        this.lastName = lastName;
        this.phone = phone;
        this.is_seller = is_seller;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public boolean isIs_seller() {
        return is_seller;
    }

    public void setIs_seller(boolean is_seller) {
        this.is_seller = is_seller;
    }
}
