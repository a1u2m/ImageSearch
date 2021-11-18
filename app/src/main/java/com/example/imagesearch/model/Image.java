package com.example.imagesearch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("link")
    @Expose
    private String link;

    public String getLink() {
        return link;
    }

}
