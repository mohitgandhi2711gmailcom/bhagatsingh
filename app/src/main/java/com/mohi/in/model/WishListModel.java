package com.mohi.in.model;

/**
 * Created by pankaj on 10/13/17.
 */

public class WishListModel {

    public String product_id;
    public String product_name;
    public String image;
    public String qty;
    public String category;
    public String price;
    public int is_add_to_cart;
    public double rating;


    public WishListModel(String product_id, String product_name, String image, String qty, String category, int is_add_to_cart, double rating, String price) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.image = image;
        this.qty = qty;
        this.category = category;
        this.is_add_to_cart = is_add_to_cart;
        this.rating = rating;
        this.price = price;
    }
}
