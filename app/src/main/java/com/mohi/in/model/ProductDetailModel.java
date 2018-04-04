package com.mohi.in.model;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by admin on 18/10/17.
 */

public class ProductDetailModel {

    public String product_id, product_name, image, description, product_price, is_wishlist, rating, is_add_to_cart, pincode="", pincodeaddress;
    public ArrayList<ProductImgModel> mediaList= new ArrayList<>();
    public ArrayList<FeaturedProductsModel> relatedProductsList = new ArrayList<FeaturedProductsModel>();
    public JSONArray jsonArray;


   /* public ProductDetailModel(JSONObject jsonObj)
    {
        title = jsonObj.optString("title","");
        price = jsonObj.optString("price","");
        rating = jsonObj.optString("rating","");
        is_favourite = jsonObj.optString("is_favourite","");
        try {
            mProductImgList = ProductImgModel.getList(jsonObj.getJSONArray("product_images"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    public ProductDetailModel(String product_id, String product_name, String image, String description, String product_price, String is_wishlist, String rating, String is_add_to_cart, String pincode, String pincodeaddress, JSONArray jsonArray, JSONArray relatedProducts ) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.image = image;
        this.description = description;
        this.product_price = product_price;
        this.is_wishlist = is_wishlist;
        this.rating = rating;
        this.is_add_to_cart = is_add_to_cart;
        this.pincode = pincode;
        this.pincodeaddress = pincodeaddress;
        this.jsonArray = jsonArray;

        try {
            mediaList  =new ArrayList<>();
            mediaList.clear();
            mediaList = ProductImgModel.getList(jsonArray);

            relatedProductsList.clear();

            relatedProductsList = FeaturedProductsModel.getList(relatedProducts);

            Log.e("dfdfdf ","relatedProducts : "+relatedProductsList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
