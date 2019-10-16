package com.alpha.team.eventhub.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by clasence on 25,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 *Entity for City table
 */
public class City {

    private int id;

    @SerializedName("id")
    private int cityId;

    @SerializedName("countryId")
    private int countryId;

    @SerializedName("name")
    private String name;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", name='" + name + '\'' +
                '}';
    }
}
