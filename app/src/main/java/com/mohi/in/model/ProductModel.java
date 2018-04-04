package com.mohi.in.model;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ProductModel {
    public ArrayList<FeaturedProductsModel> productList = new ArrayList<>();
    public String category, isProduct, type;

    public ProductModel(String category, String isProduct, String type, JSONArray jsonArray) {
        this.category = category;
        this.isProduct = isProduct;
        this.type = type;
        try {
            productList.clear();
            productList = getList(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<FeaturedProductsModel> getList(JSONArray jsonArr) {
        ArrayList<FeaturedProductsModel> list = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArr.length(); i++) {
                list.add(new FeaturedProductsModel(jsonArr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}
