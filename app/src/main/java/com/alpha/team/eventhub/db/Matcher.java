package com.alpha.team.eventhub.db;

import static com.alpha.team.eventhub.utils.Constants.*;

/**
 * Created by clasence on 25,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 * Uri matcher to match each Uri to a unique int
 */
public class Matcher {
    public static android.content.UriMatcher buildUriMatcher() {

        final android.content.UriMatcher matcher = new android.content.UriMatcher(android.content.UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;
        matcher.addURI(authority,PATH_CATEGORY,CODE_CATEGORY);
        matcher.addURI(authority,PATH_COUNTRY,CODE_COUNTRY);
        matcher.addURI(authority,PATH_USER,CODE_USER);
        matcher.addURI(authority,PATH_EVENT,CODE_EVENT);
        return matcher;
    }
}
