package com.alpha.team.eventhub.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by clasence on 25,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 *Entity for Country table
 */
public class Country {

    private transient int id;

    @SerializedName("id")
    private int country_id;

    @SerializedName("code")
    private String code;

    @SerializedName("name")
    private String name;


    public Country(int id, int country_id, String code, String name) {
        this.id = id;
        this.country_id = country_id;
        this.code = code;
        this.name = name;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
