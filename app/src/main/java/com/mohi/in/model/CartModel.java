package com.mohi.in.model;

/**
 * Created by pankaj on 11/1/17.
 */

public class CartModel {


    public  String product_id, item_id, product_name, product_price, image, category, qty, quote_id, stock, is_wishlist;


    public CartModel(String product_id, String item_id, String product_name, String product_price, String image, String category, String qty, String quote_id, String stock, String is_wishlist) {
        this.product_id = product_id;
        this.item_id = item_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.image = image;
        this.category = category;
        this.qty = qty;
        this.quote_id = quote_id;
        this.stock = stock;
        this.is_wishlist = is_wishlist;
    }
}
