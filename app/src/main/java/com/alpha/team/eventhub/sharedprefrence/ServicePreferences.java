package com.alpha.team.eventhub.sharedprefrence;

import android.content.Context;
import android.content.SharedPreferences;

import com.alpha.team.eventhub.entities.Event;

import static com.alpha.team.eventhub.utils.Constants.CATEGORY_SAVE;
import static com.alpha.team.eventhub.utils.Constants.COLUMN_GOT_CATEGORIES;
import static com.alpha.team.eventhub.utils.Constants.COLUMN_GOT_COUNTRIES;
import static com.alpha.team.eventhub.utils.Constants.USER_LIKES;

/**
 * Created by clasence on 25,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 * SharedPreferences for persisting states of the application
 */
public class ServicePreferences {

    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    public ServicePreferences(Context context){
        pref = context.getSharedPreferences("updatePref", 0);
        editor = pref.edit();
    }


    /**
     * Update countries
     * @param value
     */
    public void updateGotCountries(boolean value){

        editor.putBoolean(COLUMN_GOT_COUNTRIES,value);
        editor.commit();
    }


    /**
     * Indicate if countries have been fetched
     */
    public boolean getGotCountries(){

        return pref.getBoolean(COLUMN_GOT_COUNTRIES,false);
    }

    /**
     * Update categories
     * @param value
     */
    public void updateGotCategories(boolean value){
        editor.putBoolean(COLUMN_GOT_CATEGORIES,value);
        editor.commit();
    }

    /**
     * Check if countries have been downloaded
     * @return
     */
    public boolean getGotCategories()
    {
        return pref.getBoolean(COLUMN_GOT_CATEGORIES,false);
    }


    /**
     * Persist like to database
     * @param event
     * @return
     */
    public boolean insertLike(Event event){
        String likes = pref.getString(USER_LIKES,"");
        likes = likes+","+event.getEventId();
        editor.putString(USER_LIKES,likes);
        if(editor.commit()){
            return true;
        }
        return false;

    }


    /**
     * Check if user has liked event
     * @param event
     * @return
     */
    public boolean hasLiked(Event event){
        String likeString = pref.getString(USER_LIKES,"");
        String[] likes = likeString.split(",");
        for(String like: likes){
            if(like!=null && like.length()>0){
                like = like.trim();
                if(like.equalsIgnoreCase(String.valueOf(event.getEventId()))){
                    return true;
                }
            }
        }
        return  false;

    }

    /**
     * Clear all data in SharedPreferences
     * @return
     */
    public boolean clearData(){
        editor.clear();
        if(editor.commit()){
            return true;
        }
        return false;
    }


    /**
     * Insert Categories
     * @param categoryString
     * @return
     */
    public boolean insertCatogories(String categoryString){
        editor.putString(CATEGORY_SAVE,categoryString);
        if(editor.commit()){
            return true;
        }
        return false;

    }

    /**
     * Get Categories
     * @return
     */
    public String getCategories(){
        return pref.getString(CATEGORY_SAVE,"");
    }
}
