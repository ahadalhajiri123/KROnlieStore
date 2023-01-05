package com.unifiedtnc.belview_foods;

import java.io.Serializable;

public class ProductModel implements Serializable {

    public String product_key;
    public String name;
    public String price;
    public String description;
    public String current_date;
    public String category;
    public String imageUrl;


    public ProductModel(String product_key, String name, String price, String description,String category, String imageUrl) {
        this.product_key = product_key;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public ProductModel(String name, String price, String description, String category, String imageUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public ProductModel(String name, String price, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
