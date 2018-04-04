package com.mohi.in.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 11/10/17.
 */

public class FeaturedProductsModel {






    public String product_id, product_name, image, product_price, new_price, date_from, date_to, is_wishlist, rating, is_add_to_cart, stock, is_product;







    public FeaturedProductsModel(JSONObject jsonObj)
    {

        this.product_id = jsonObj.optString("product_id","");
        this.product_name = jsonObj.optString("product_name","");
        this.image = jsonObj.optString("image","");
        this.product_price = jsonObj.optString("product_price","");

        this.is_wishlist = jsonObj.optString("is_wishlist","0");
        this.rating = jsonObj.optString("rating","0");
        this.is_add_to_cart = jsonObj.optString("is_add_to_cart","0");


        this.new_price = jsonObj.optString("new_price","0");
        this.date_from = jsonObj.optString("date_from","");
        this.date_to = jsonObj.optString("date_to","");
        this.is_product = jsonObj.optString("is_product","0");
        this.stock = jsonObj.optString("stock","0");




    }

    public static ArrayList<FeaturedProductsModel> getList(JSONArray jsonArr)
    {
        ArrayList<FeaturedProductsModel> list = new ArrayList<>();

        try {
            for(int i=0;i<jsonArr.length();i++) {
                list.add(new FeaturedProductsModel(jsonArr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

}
