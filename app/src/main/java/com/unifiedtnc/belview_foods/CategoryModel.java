package com.unifiedtnc.belview_foods;

import java.io.Serializable;

public class CategoryModel implements Serializable {

    public String cat_key;
  public String category;
   public String category_Image;

    public String getCat_key() {
        return cat_key;
    }

    public void setCat_key(String cat_key) {
        this.cat_key = cat_key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory_Image() {
        return category_Image;
    }

    public void setCategory_Image(String category_Image) {
        this.category_Image = category_Image;
    }

    public CategoryModel(String cat_key, String category, String category_Image) {
        this.cat_key = cat_key;
        this.category = category;
        this.category_Image = category_Image;
    }


    public CategoryModel(String category, String category_Image) {
        this.category = category;
        this.category_Image = category_Image;
    }


}
