package com.unifiedtnc.belview_foods;

public class CartModel {

    public String cartId;
    public String productName;
    public String productImage;
    public String productId;
    public String userId;
    public int quantity;
    public int price;
    public int cartStatus;

    public CartModel(String productName, String productImage, String productId, String userId, int quantity, int price,int cartStatus) {
        this.productName = productName;
        this.productImage = productImage;
        this.productId = productId;
        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
        this.cartStatus=cartStatus;
    }

    public CartModel(String cartId, String productName, String productImage, String productId, String userId, int quantity, int price,int cartStatus) {
        this.cartId = cartId;
        this.productName = productName;
        this.productImage = productImage;
        this.productId = productId;
        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
        this.cartStatus=cartStatus;
    }
}
