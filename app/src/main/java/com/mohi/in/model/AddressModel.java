package com.mohi.in.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressModel implements Parcelable {

    private String address_id;
    private String firstname;
    private String lastname;
    private String telephone;
    private String street_1;
    private String street_2;
    private String city;
    private String region;
    private String postcode;
    private String country_id;
    private Boolean default_shipping;
    private Boolean default_billing;

    public AddressModel()
    {}

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getStreet_1() {
        return street_1;
    }

    public void setStreet_1(String street_1) {
        this.street_1 = street_1;
    }

    public String getStreet_2() {
        return street_2;
    }

    public void setStreet_2(String street_2) {
        this.street_2 = street_2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public Boolean getDefault_shipping() {
        return default_shipping;
    }

    public void setDefault_shipping(Boolean default_shipping) {
        this.default_shipping = default_shipping;
    }

    public Boolean getDefault_billing() {
        return default_billing;
    }

    public void setDefault_billing(Boolean default_billing) {
        this.default_billing = default_billing;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(address_id);
        out.writeString(firstname);
        out.writeString(lastname);
        out.writeString(telephone);
        out.writeString(street_1);
        out.writeString(street_2);
        out.writeString(city);
        out.writeString(region);
        out.writeString(postcode);
        out.writeString(country_id);
        out.writeByte((byte) (default_shipping ? 1 : 0)); //if default_shipping = true then byte = 1
        out.writeByte((byte) (default_billing ? 1 : 0)); //if default_billing = true then byte = 1
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private AddressModel(Parcel in) {
        address_id = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        telephone = in.readString();
        street_1 = in.readString();
        street_2 = in.readString();
        city = in.readString();
        region = in.readString();
        postcode = in.readString();
        country_id = in.readString();
        default_shipping= in.readByte() == 1;
        default_billing=in.readByte()==1;
//        byte tmpDefault_shipping = in.readByte();
//        default_shipping = tmpDefault_shipping == 0 ? null : tmpDefault_shipping == 1;
//        byte tmpDefault_billing = in.readByte();
//        default_billing = tmpDefault_billing == 0 ? null : tmpDefault_billing == 1;
    }

    public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel in) {
            return new AddressModel(in);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };
}
