package com.alpha.team.eventhub.db;

import android.net.Uri;
import android.provider.BaseColumns;

import static com.alpha.team.eventhub.utils.Constants.BASE_CONTENT_URI;
import static com.alpha.team.eventhub.utils.Constants.PATH_COUNTRY;

/**
 * Created by clasence on 25,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 *Constants for Country table of database
 */
public class CountryEntry implements BaseColumns {

    public static final String TABLE_NAME = "countries";

    public static final String COLUMN_COUNTRY_ID= "country_id";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_NAME = "name";

    //base uri
    public static final Uri COUNTRY_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_COUNTRY)
            .build();
}
