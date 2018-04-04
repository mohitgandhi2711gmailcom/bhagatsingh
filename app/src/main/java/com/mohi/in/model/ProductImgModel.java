package com.mohi.in.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 18/10/17.
 */

public class ProductImgModel {

    public String media_id, thumbnail, image;
    public boolean isSelected=false;

    public ProductImgModel(JSONObject jsonObj)
    {
        this.media_id = jsonObj.optString("media_id","");
        this.thumbnail = jsonObj.optString("thumbnail","");
        this.image = jsonObj.optString("base","");


        isSelected = false;

    }


    public static ArrayList<ProductImgModel> getList(JSONArray jsonArr)
    {
        ArrayList<ProductImgModel> list = new ArrayList<>();

        try {
            for(int i=0;i<jsonArr.length();i++) {
                list.add(new ProductImgModel(jsonArr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

}
