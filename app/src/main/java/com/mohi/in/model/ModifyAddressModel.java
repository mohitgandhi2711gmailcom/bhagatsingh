package com.mohi.in.model;

/**
 * Created by pankaj on 11/10/17.
 */

public class ModifyAddressModel {

    public String address_id, name, mobile, postcode, city, flat_no, street, landmark, state, country;


    public boolean isOpen, isSelected;

    public ModifyAddressModel(String address_id, String name, String mobile, String postcode, String city, String flat_no, String street, String landmark,
                              String state, String country, boolean isOpen, boolean isSelected) {
        this.address_id = address_id;
        this.name = name;
        this.mobile = mobile;
        this.postcode = postcode;
        this.city = city;
        this.flat_no = flat_no;
        this.street = street;
        this.landmark = landmark;
        this.state = state;
        this.country = country;
        this.isOpen = isOpen;
        this.isSelected = isSelected;
    }
}
