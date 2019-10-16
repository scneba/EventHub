package com.alpha.team.eventhub.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alpha.team.eventhub.entities.Country;


public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "eventhub.db";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create category table sql

        final String SQL_CREATE_CATEGORY_TABLE =

                "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +

                        CategoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        CategoryEntry.COLUMN_CATEGORY_ID + " INTEGER NOT NULL," +

                        CategoryEntry.COLUMN_NAME + " TEXT NOT NULL," +
                        CategoryEntry.COLUMN_FAVOURITE + " TEXT NOT NULL)";


        //create country table sql
        final String SQL_CREATE_COUNTRY_TABLE =

                "CREATE TABLE " + CountryEntry.TABLE_NAME + " (" +

                        CountryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        CountryEntry.COLUMN_COUNTRY_ID + " INTEGER NOT NULL," +

                        CountryEntry.COLUMN_CODE + " TEXT NOT NULL," +
                        CountryEntry.COLUMN_NAME + " TEXT NOT NULL)";


        //create country table sql
        final String SQL_CREATE_EVENT_TABLE =

                "CREATE TABLE " + EventEntry.TABLE_NAME + " (" +

                        EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        EventEntry.COLUMN_EVENT_ID + " INTEGER NOT NULL," +
                        EventEntry.COLUMN_USER_ID + " INTEGER NOT NULL," +
                        EventEntry.COLUMN_CATEGORY_ID + " INTEGER NOT NULL," +
                        EventEntry.COLUMN_COUNTRY_ID + " INTEGER NOT NULL," +
                        EventEntry.COLUMN_CITY_ID + " INTEGER NOT NULL," +
                        EventEntry.COLUMN_EVENT_STATUS + " INTEGER," +
                        EventEntry.COLUMN_EVENT_NAME + " TEXT NOT NULL," +
                        EventEntry.COLUMN_WEBSITE + " TEXT," +
                        EventEntry.COLUMN_ADDRESS + " TEXT," +
                        EventEntry.COLUMN_DESCRIPTION + " TEXT," +
                        EventEntry.COLUMN_PICTURE + " TEXT NOT NULL," +
                        EventEntry.COLUMN_ACCEPT_REG + " INTEGER NOT NULL," +
                        EventEntry.COLUMN_CREATED_AT + " TEXT NOT NULL," +
                        EventEntry.COLUMN_UPDATED_AT + " TEXT NOT NULL," +
                        EventEntry.COLUMN_DATE_FROM + " TEXT," +
                        EventEntry.COLUMN_DATE_TO + " TEXT," +
                        EventEntry.COLUMN_LATITUDE + " REAL," +
                        EventEntry.COLUMN_LONGITUDE + " REAL)";




        db.execSQL(SQL_CREATE_CATEGORY_TABLE);
        db.execSQL(SQL_CREATE_COUNTRY_TABLE);
        db.execSQL(SQL_CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CountryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
        onCreate(db);

    }
}
