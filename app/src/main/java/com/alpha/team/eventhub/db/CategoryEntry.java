package com.alpha.team.eventhub.db;

import android.net.Uri;
import android.provider.BaseColumns;
import static com.alpha.team.eventhub.utils.Constants.BASE_CONTENT_URI;
import static com.alpha.team.eventhub.utils.Constants.PATH_CATEGORY;

/**
 * Created by clasence on 25,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 *Constants for Category table of database
 */
public class CategoryEntry implements BaseColumns{

    //cat name
    public static final String TABLE_NAME = "categories";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FAVOURITE= "favourite";

    //base uri
    public static final Uri CATEGORY_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_CATEGORY)
            .build();

}
