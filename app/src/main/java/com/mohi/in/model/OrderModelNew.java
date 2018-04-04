package com.mohi.in.model;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderModelNew implements Serializable {
    private String imagedata;
    private int orderNumber;
    private String brandName;
    private String brandDetails;
    private String deliveryStatus;
    private int amount;
    private ArrayList<DetailOrderModel> list;
    private String paymentMode;
    private String Address;
    private String trackingCode;

    public ArrayList<DetailOrderModel> getList() {
        return list;
    }

    public void setList(ArrayList<DetailOrderModel> list) {
        this.list = list;
    }

    public OrderModelNew() {
        list=new ArrayList<>();
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public String getImagedata() {
        return imagedata;
    }

    public void setImagedata(String imagedata) {
        this.imagedata = imagedata;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandDetails() {
        return brandDetails;
    }

    public void setBrandDetails(String brandDetails) {
        this.brandDetails = brandDetails;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}