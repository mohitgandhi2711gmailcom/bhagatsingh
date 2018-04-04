package com.mohi.in.model;

/**
 * Created by pankaj on 12/8/17.
 */

public class SelectListModel {

   public String name, colorCode, cat_id;
    public boolean isChecked;


    public SelectListModel(String cat_id, String name, String colorCode, boolean isChecked) {
        this.cat_id = cat_id;
        this.name = name;
        this.colorCode = colorCode;
        this.isChecked = isChecked;
    }
}
