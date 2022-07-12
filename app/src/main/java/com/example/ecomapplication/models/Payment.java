package com.example.ecomapplication.models;

import java.util.Date;

public class Payment {
    private String id_order;
    private String account_nb;
    private String payment_type;
    private String id_user;
    private String provider;
    private Date expired;
    public Payment(String id_order, String account_nb, String payment_type, String id_user, String provider, Date expired) {
        this.id_order = id_order;
        this.account_nb = account_nb;
        this.payment_type = payment_type;
        this.id_user = id_user;
        this.provider = provider;
        this.expired = expired;
    }
    public Payment(){

    }
    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public String getAccount_nb() {
        return account_nb;
    }

    public void setAccount_nb(String account_nb) {
        this.account_nb = account_nb;
    }



    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
