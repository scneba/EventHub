package com.alpha.team.eventhub.db;

import android.net.Uri;

import static com.alpha.team.eventhub.utils.Constants.BASE_CONTENT_URI;
import static com.alpha.team.eventhub.utils.Constants.PATH_USER;
/**
 * Created by clasence on 25,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 *Constants for User table of database
 */
public class UserEntry {

    public static final String TABLE_NAME = "user";

    public static final String COLUMN_ID = "user_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_COUNTRY_ID = "country_id";
    public static final String COLUMN_CITY_ID = "city_id";
    public static final String COLUMN_FAVOURITES = "favourites";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_CREATED_AT= "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_STATUS= "status";
    public static final String REMEMBER_ME= "remember_me";

    //base uri
    public static final Uri USER_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_USER)
            .build();

}
