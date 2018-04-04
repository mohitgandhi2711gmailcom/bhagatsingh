package com.mohi.in.model;

/**
 * Created by pankaj on 10/26/17.
 */

public class DealsModel {

    String product_id, product_name, image, actual_price, new_price, date_from, date_to;


    public DealsModel(String product_id, String product_name, String image, String actual_price, String new_price, String date_from, String date_to) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.image = image;
        this.actual_price = actual_price;
        this.new_price = new_price;
        this.date_from = date_from;
        this.date_to = date_to;
    }


}
