package com.alpha.team.eventhub.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by clasence on 25,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 *Entity for Event table
 */

public class Event implements Parcelable {

    private transient int id;

    @SerializedName("id")
    private int eventId;

    @SerializedName("userId")
    private int userId;

    @SerializedName("categoryId")
    private int categoryId;

    @SerializedName("countryId")
    private int countryId;

    @SerializedName("cityId")
    private int cityId;

    @SerializedName("eventName")
    private String eventName;

    @SerializedName("website")
    private String website;

    @SerializedName("address")
    private String address;

    @SerializedName("description")
    private String description;

    @SerializedName("picture")
    private String picture;

    @SerializedName("acceptacceptOnRegistration")
    private int acceptOnRegistration;

    @SerializedName("eventStatus")
    private int eventStatus;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("dateFrom")
    private String dateFrom;

    @SerializedName("dateTo")
    private String dateTo;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("likeCount")
    private int likeCount;

    private boolean alarmSet, calendarSet, liked, favourite;

    public Event(){}

    public Event(Parcel in) {
        eventId = in.readInt();
        userId = in.readInt();
        categoryId = in.readInt();
        countryId = in.readInt();
        cityId = in.readInt();
        eventName = in.readString();
        website = in.readString();
        address = in.readString();
        description = in.readString();
        picture = in.readString();
        acceptOnRegistration = in.readInt();
        eventStatus = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        dateFrom = in.readString();
        dateTo = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        likeCount = in.readInt();
        alarmSet = in.readByte() != 0;
        calendarSet = in.readByte() != 0;
        liked = in.readByte() != 0;
        favourite = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(eventId);
        dest.writeInt(userId);
        dest.writeInt(categoryId);
        dest.writeInt(countryId);
        dest.writeInt(cityId);
        dest.writeString(eventName);
        dest.writeString(website);
        dest.writeString(address);
        dest.writeString(description);
        dest.writeString(picture);
        dest.writeInt(acceptOnRegistration);
        dest.writeInt(eventStatus);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(dateFrom);
        dest.writeString(dateTo);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeInt(likeCount);
        dest.writeByte((byte) (alarmSet ? 1 : 0));
        dest.writeByte((byte) (calendarSet ? 1 : 0));
        dest.writeByte((byte) (liked ? 1 : 0));
        dest.writeByte((byte) (favourite ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getAcceptOnRegistration() {
        return acceptOnRegistration;
    }

    public void setAcceptOnRegistration(int acceptOnRegistration) {
        this.acceptOnRegistration = acceptOnRegistration;
    }

    public int getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(int eventStatus) {
        this.eventStatus = eventStatus;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isAlarmSet() {
        return alarmSet;
    }

    public void setAlarmSet(boolean alarmSet) {
        this.alarmSet = alarmSet;
    }

    public boolean isCalendarSet() {
        return calendarSet;
    }

    public void setCalendarSet(boolean calendarSet) {
        this.calendarSet = calendarSet;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", countryId=" + countryId +
                ", cityId=" + cityId +
                ", eventName='" + eventName + '\'' +
                ", website='" + website + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", picture='" + picture + '\'' +
                ", acceptOnRegistration=" + acceptOnRegistration +
                ", eventStatus=" + eventStatus +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", likeCount=" + likeCount +
                ", alarmSet=" + alarmSet +
                ", calendarSet=" + calendarSet +
                ", liked=" + liked +
                ", favourite=" + favourite +
                '}';
    }
}
