package com.alpha.team.eventhub.db;

import android.net.Uri;
import android.provider.BaseColumns;

import static com.alpha.team.eventhub.utils.Constants.BASE_CONTENT_URI;
import static com.alpha.team.eventhub.utils.Constants.PATH_EVENT;

/**
 * Created by clasence on 25,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 *Constants for Event table of database
 */
public class EventEntry implements BaseColumns {


    public static final String TABLE_NAME = "event";

    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_COUNTRY_ID = "country_id";
    public static final String COLUMN_CITY_ID = "city_id";
    public static final String COLUMN_EVENT_NAME = "event_name";
    public static final String COLUMN_WEBSITE = "website";
    public static final String COLUMN_ADDRESS= "address";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PICTURE = "picture";
    public static final String COLUMN_ACCEPT_REG= "accept_registration";

    public static final String COLUMN_LATITUDE= "latitude";
    public static final String COLUMN_LONGITUDE= "longitude";
    public static final String COLUMN_EVENT_STATUS = "event_status";
    public static final String COLUMN_CREATED_AT= "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_DATE_FROM = "date_from";
    public static final String COLUMN_DATE_TO = "date_to";


    //base uri
    public static final Uri EVENT_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_EVENT)
            .build();
}
