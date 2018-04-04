package com.mohi.in.model;

/**
 * Created by pankaj on 10/13/17.
 */

public class SubCategoriesModel {

    public String product_id;
    public String product_name;
    public String image;
    public String product_price;
    public int is_wishlist;
    public double rating;
    public int is_add_to_cart;

    public SubCategoriesModel(){

    }
    public SubCategoriesModel(String product_id, String product_name, String image, String product_price, int is_wishlist, double rating, int is_add_to_cart) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.image = image;
        this.product_price = product_price;
        this.is_wishlist = is_wishlist;
        this.rating = rating;
        this.is_add_to_cart = is_add_to_cart;
    }
}
