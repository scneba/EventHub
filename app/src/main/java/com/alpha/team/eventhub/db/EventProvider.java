package com.alpha.team.eventhub.db;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alpha.team.eventhub.entities.User;
import com.alpha.team.eventhub.sharedprefrence.UserPreference;

import static com.alpha.team.eventhub.utils.Constants.*;

/**
 * Created by clasence on 25,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 *Content Provider for all database tables in application
 */
public class EventProvider extends ContentProvider {
    private DBHelper dbHelper;
    private static final UriMatcher URI_MATCHER= Matcher.buildUriMatcher();
    UserPreference userPreference;
    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        userPreference = new UserPreference(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (URI_MATCHER.match(uri)) {
            case CODE_CATEGORY:{
                cursor = dbHelper.getReadableDatabase().query(
                        CategoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case CODE_COUNTRY:{
                cursor = dbHelper.getReadableDatabase().query(
                        CountryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case CODE_EVENT:{
                cursor = dbHelper.getReadableDatabase().query(
                        EventEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case CODE_USER:{
                String[] columns = new String[] { UserEntry.COLUMN_NAME,UserEntry.COLUMN_EMAIL,UserEntry.COLUMN_PASSWORD};

                if(userPreference.isLoggedIn()){
                    User user = userPreference.getUser();
                    MatrixCursor matrixCursor= new MatrixCursor(columns);

                    matrixCursor.addRow(new Object[] { user.getName(), user.getEmail(),user.getPassword()});
                    return matrixCursor;

                }else{
                    return null;
                }


            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
       return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        Uri returnUri;
        switch (URI_MATCHER.match(uri)) {
            case CODE_CATEGORY:{
                long id= db.insert(CategoryEntry.TABLE_NAME,null,values);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(CategoryEntry.CATEGORY_CONTENT_URI,id);
                }else {
                    throw new android.database.SQLException("Error trying to insert data to database");
                }
                break;

            }
            case CODE_COUNTRY:{
                long id= db.insert(CountryEntry.TABLE_NAME,null,values);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(CountryEntry.COUNTRY_CONTENT_URI,id);
                }else {
                    throw new android.database.SQLException("Error trying to insert data to database");
                }
                break;

            }
            case CODE_EVENT:{
                long id= db.insert(EventEntry.TABLE_NAME,null,values);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(EventEntry.EVENT_CONTENT_URI,id);
                }else {
                    throw new android.database.SQLException("Error trying to insert data to database");
                }
                break;

            }
            case CODE_USER:{
                //call
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (selection==null) selection = "1";

        int numRowsDeleted=-1;
        switch (URI_MATCHER.match(uri)) {

            case CODE_CATEGORY: {
                numRowsDeleted = dbHelper.getWritableDatabase().delete(
                        CategoryEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;
            }
            case CODE_COUNTRY:{
                numRowsDeleted = dbHelper.getWritableDatabase().delete(
                        CountryEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;
            }
            case CODE_EVENT:{
                numRowsDeleted = dbHelper.getWritableDatabase().delete(
                        EventEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;
            }
            case CODE_USER:{
                //user delete
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (selection==null) selection = "1";

        int numRowsUpdated=-1;
        switch (URI_MATCHER.match(uri)) {

            case CODE_CATEGORY: {
                numRowsUpdated = dbHelper.getWritableDatabase().update(
                        CategoryEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                break;
            }
            case CODE_COUNTRY:{
                numRowsUpdated = dbHelper.getWritableDatabase().update(
                        CountryEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                break;
            }
            case CODE_EVENT:{
                numRowsUpdated = dbHelper.getWritableDatabase().update(
                        EventEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                break;
            }
            case CODE_USER:{
                //user update
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (URI_MATCHER.match(uri)) {

            case CODE_CATEGORY: {
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(CategoryEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                 if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;
            }
            case CODE_COUNTRY:{
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(CountryEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            }

            case CODE_EVENT:{
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(EventEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            }

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        dbHelper.close();
        super.shutdown();
    }
}
