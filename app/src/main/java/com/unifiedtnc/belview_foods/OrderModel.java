package com.unifiedtnc.belview_foods;

import java.util.ArrayList;

public class OrderModel {

    public String orderId;
    public String userName;
    public String PhoneNo;
    public String userId;
    public String totalPrice;
    public String address;
    public String orderStatus;
    public String cardNo;
    public String cardType;
    public String cardCVV;
    public String country;
    public String radioCardType;
    public String deliveryTye;
    public ArrayList<String> arrayList=new ArrayList<>();

    public OrderModel(String userName, String phoneNo, String userId, String totalPrice, String address, String orderStatus, String cardNo, String cardType, String cardCVV, String country, String radioCardType,String deliveryTye, ArrayList<String> arrayList) {
        this.userName = userName;
        PhoneNo = phoneNo;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.address = address;
        this.orderStatus = orderStatus;
        this.cardNo = cardNo;
        this.cardType = cardType;
        this.cardCVV = cardCVV;
        this.country = country;
        this.radioCardType = radioCardType;
        this.deliveryTye = deliveryTye;
        this.arrayList = arrayList;
    }

    public OrderModel(String orderId, String userName, String phoneNo, String userId, String totalPrice, String address, String orderStatus, String cardNo, String cardType, String cardCVV, String country, String radioCardType,String deliveryTye, ArrayList<String> arrayList) {
        this.orderId = orderId;
        this.userName = userName;
        PhoneNo = phoneNo;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.address = address;
        this.orderStatus = orderStatus;
        this.cardNo = cardNo;
        this.cardType = cardType;
        this.cardCVV = cardCVV;
        this.country = country;
        this.radioCardType = radioCardType;
        this.deliveryTye = deliveryTye;
        this.arrayList = arrayList;
    }



}


