package com.example.imagesearch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName("items")
    @Expose
    private List<Image> items = null;

    public List<Image> getImages() {
        return items;
    }
}
