package com.alpha.team.eventhub.utils;

import android.net.Uri;

/**
 * Created by clasence on 25,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 */
public class Constants {

    public static final String CONTENT_AUTHORITY = "com.alpha.team.eventhub.events";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //category paths and codes
    public static final String PATH_CATEGORY = "category";

    //event path
    public static final String PATH_EVENT = "event";

    //country path
    public static final String PATH_COUNTRY = "country";

    //USER path
    public static final String PATH_USER = "user";




    //codes for matcher
    public static final int CODE_CATEGORY= 0;
    public static final int CODE_EVENT= 1;
    public static final int CODE_COUNTRY= 2;
    public static final int CODE_USER= 3;

    public static final int EVENT_DETAILS_LOADER_ID= 200;
    public static final int REGISTERED_LOADER_ID= 201;


    public static final String COLUMN_GOT_COUNTRIES = "got_countries";
    public static final String COLUMN_GOT_CATEGORIES = "got_categories";
    public static final String CATEGORY_SAVE = "save_categories";

    public static final String EVENT_LIST = "eventList";
    public static final String CURENT_EVENT = "current_event";
    public static final String LAYOUT_MAN_STATE = "lay_man_state";


    public static final String USER_LIKES = "user_likes";
    public static final String VIEW_PICKER = "view_picker";

    public static final int MAPS_PERMISSIONS_CODE=100;
    public static final int CALENDAR_PERMISSIONS_CODE=101;

    public static final int MAIN_MAPS_PERMISSIONS_CODE=111;


    public static final String SEND_DATA_TO_ACTIVITY = "send_data";
    public static final int SEND_DATA_TO_ACTIVITY_CODE = 105;

    public static final String DATE_SOURCE = "date_source";

}
