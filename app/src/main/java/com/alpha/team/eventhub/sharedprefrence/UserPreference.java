package com.alpha.team.eventhub.sharedprefrence;

import android.content.Context;
import android.content.SharedPreferences;

import com.alpha.team.eventhub.db.UserEntry;
import com.alpha.team.eventhub.entities.User;

import java.util.Date;

/**
 * Created by clasence on 25,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 * Save login user to SharedPreferences
 */
public class UserPreference {
    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    public UserPreference(Context context){
        pref = context.getSharedPreferences("userPref", 0);
        editor = pref.edit();
    }

    /**
     * Insert user to Preferences
     * @param user
     * @return
     */
    public boolean insert(User user){
        editor.putInt(UserEntry.COLUMN_ID, user.getId());
        editor.putInt(UserEntry.COLUMN_COUNTRY_ID, user.getCountryId());
        editor.putInt(UserEntry.COLUMN_CITY_ID, user.getCityId());
        editor.putInt(UserEntry.COLUMN_STATUS, user.getStatus());

        editor.putString(UserEntry.COLUMN_NAME, user.getName());
        editor.putString(UserEntry.COLUMN_USERNAME, user.getUsername());
        editor.putString(UserEntry.COLUMN_EMAIL, user.getEmail());
        editor.putString(UserEntry.COLUMN_FAVOURITES, user.getFavouriteCategories());
        editor.putString(UserEntry.COLUMN_PASSWORD, user.getPassword());
        editor.putString(UserEntry.COLUMN_CREATED_AT, user.getCreatedAt().toString());
        editor.putString(UserEntry.COLUMN_UPDATED_AT, user.getUpdatedAt().toString());
        if(editor.commit()){
            return true;
        }
        return false;

    }


    /**
     * Get user Id from Preferences
     * @return
     */
    public int getUserId(){
       return pref.getInt(UserEntry.COLUMN_ID,-1);
    }

    /**
     * Update status
     * @param status
     * @return
     */
    public boolean updateStatus(int status){
        editor.putInt(UserEntry.COLUMN_STATUS, status);
        return true;
    }

    /**
     * Update email
     * @param  email
     * @return
     */
    public boolean updateEmail(String email){
        editor.putString(UserEntry.COLUMN_EMAIL, email);
        return true;
    }


    /**
     * Get user from SharedPreferences
     * @return
     */
    public User getUser(){
        User user = new User();
        user.setId(pref.getInt(UserEntry.COLUMN_ID,-1));
        user.setCityId(pref.getInt(UserEntry.COLUMN_ID,-1));
        user.setCountryId(pref.getInt(UserEntry.COLUMN_COUNTRY_ID,-1));
        user.setCountryId(pref.getInt(UserEntry.COLUMN_STATUS,-1));

        user.setUsername(pref.getString(UserEntry.COLUMN_USERNAME,null));
        user.setName(pref.getString(UserEntry.COLUMN_NAME,null));
        user.setEmail(pref.getString(UserEntry.COLUMN_EMAIL,null));
        user.setPassword(pref.getString(UserEntry.COLUMN_PASSWORD,null));
        user.setFavouriteCategories(pref.getString(UserEntry.COLUMN_FAVOURITES,null));
        Date date1 = new Date(pref.getString(UserEntry.COLUMN_CREATED_AT,null));
        Date date2 = new Date(pref.getString(UserEntry.COLUMN_UPDATED_AT,null));

        user.setCreatedAt(date1);
        user.setUpdatedAt(date2);
        return user;
    }


    /**
     * Set value for remember me
     * @param value
     * @return
     */
    public boolean setRememberMe(boolean value) {
        editor.putBoolean(UserEntry.REMEMBER_ME, value);
        if (editor.commit()) {
            return true;
        }
        return false;
    }

    /**
     * Get value for remember me
     * @return
     */
    public boolean remeberMe(){
        return pref.getBoolean(UserEntry.REMEMBER_ME,false);
    }

    /**
     * Get username from Preferences
     * @return
     */
    public String getUsername(){
        return pref.getString(UserEntry.COLUMN_USERNAME,"");
    }

    /**
     * Get password from preferences
     * @return
     */
    public String getPassword(){
        return pref.getString(UserEntry.COLUMN_PASSWORD,"");
    }

    /**
     * Check if user is logged in
     * @return
     */
    public boolean isLoggedIn(){
        if(pref.contains(UserEntry.COLUMN_USERNAME)){
            return true;
        }
        return false;
    }


    /**
     * Log out user
     * @return
     */
    public boolean logoutUser(){
        editor.clear();
        editor.commit();
        return true;
    }
}
