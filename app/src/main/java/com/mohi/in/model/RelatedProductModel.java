package com.mohi.in.model;

public class RelatedProductModel {

    private String product_id;
    private String product_name;
    private String image_url;
    private String product_price;
    private String specialPrice;
    private String is_wishlist;
    private String is_add_to_cart;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(String specialPrice) {
        this.specialPrice = specialPrice;
    }

    public String getIs_wishlist() {
        return is_wishlist;
    }

    public void setIs_wishlist(String is_wishlist) {
        this.is_wishlist = is_wishlist;
    }

    public String getIs_add_to_cart() {
        return is_add_to_cart;
    }

    public void setIs_add_to_cart(String is_add_to_cart) {
        this.is_add_to_cart = is_add_to_cart;
    }
}