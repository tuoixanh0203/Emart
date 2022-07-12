package com.example.ecomapplication.models;

public class User {
    private  String address;
    private  String city;
    private String date;
    private String email;
    private String firstName;
    private String id;
    private String lastName;
    private String phone;

    public User(String address, String city, String date, String email, String firstName, String id, String lastName, String phone) {
        this.address = address;
        this.city = city;
        this.date = date;
        this.email = email;
        this.firstName = firstName;
        this.id = id;
        this.lastName = lastName;
        this.phone = phone;
    }

}
