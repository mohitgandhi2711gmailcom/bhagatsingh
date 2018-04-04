package com.mohi.in.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 11/10/17.
 */

public class FeaturedCategoryModel {

    public String cat_id;
    public String name;
    public String image;



    public FeaturedCategoryModel(String cat_id , String name, String image)
    {
        this.cat_id = cat_id;
        this.name = name;
        this.image = image;
    }
    public FeaturedCategoryModel(JSONObject jsonObj)
    {

        this.cat_id = jsonObj.optString("cat_id","");
        this.name = jsonObj.optString("name","");
        this.image = jsonObj.optString("image","");
    }

    public static ArrayList<FeaturedCategoryModel> getList(JSONArray jsonArr)
    {
        ArrayList<FeaturedCategoryModel> list = new ArrayList<>();

        try {
            for(int i=0;i<jsonArr.length();i++) {
                list.add(new FeaturedCategoryModel(jsonArr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

}
