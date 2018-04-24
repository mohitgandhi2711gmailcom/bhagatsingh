package com.mohi.in.model;

import java.io.Serializable;

public class MediaModel implements Serializable {


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;
}