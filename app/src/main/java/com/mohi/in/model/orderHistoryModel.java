package com.mohi.in.model;

/**
 * Created by pankaj on 10/13/17.
 */

public class orderHistoryModel {

    public String order_id, status, customer_id, tax_amount, subtotal, discount_amount, grand_total, total_qty_ordered
    , payment_method, product_id, product_name, image, status_text;


    public orderHistoryModel(String order_id, String status, String customer_id, String tax_amount, String subtotal,
                             String discount_amount, String grand_total, String total_qty_ordered, String payment_method,
                             String product_id, String product_name, String image, String status_text) {

        this.order_id = order_id;
        this.status = status;
        this.customer_id = customer_id;
        this.tax_amount = tax_amount;
        this.subtotal = subtotal;
        this.discount_amount = discount_amount;
        this.grand_total = grand_total;
        this.total_qty_ordered = total_qty_ordered;
        this.payment_method = payment_method;
        this.product_id = product_id;
        this.product_name = product_name;
        this.image = image;
        this.status_text = status_text;
    }
}
