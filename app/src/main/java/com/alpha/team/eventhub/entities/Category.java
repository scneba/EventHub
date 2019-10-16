package com.alpha.team.eventhub.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by clasence on 25,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 *Entity for Category table
 */
public class Category {


    private transient int id;

    @SerializedName("id")
    private int categoryId;
    @SerializedName("name")
    private String name;
    private String favourite;

    public Category(int id, int categoryId, String name, String favourite) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.favourite = favourite;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }


}
