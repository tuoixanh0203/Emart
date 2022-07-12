package com.example.ecomapplication.models;

public class SliderData {
    private String imgUrl;
    private String productId;
    public SliderData(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getProductId(){
        return this.productId;
    }
}