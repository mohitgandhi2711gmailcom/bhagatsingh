package com.mohi.in.model;

/**
 * Created by admin on 11/10/17.
 */

public class HomeProductModel {

    public String id;
    public String name;
    public String img_url;
    public int is_wishlist;
    public double rating;
    public int is_add_to_cart;


    public HomeProductModel(String id, String name, String img_url, int is_wishlist, double rating, int is_add_to_cart) {
        this.id = id;
        this.name = name;
        this.img_url = img_url;
        this.is_wishlist = is_wishlist;
        this.rating = rating;
        this.is_add_to_cart = is_add_to_cart;
    }
}
