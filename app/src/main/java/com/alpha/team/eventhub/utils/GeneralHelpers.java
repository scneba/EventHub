package com.alpha.team.eventhub.utils;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.alpha.team.eventhub.entities.Event;
import com.alpha.team.eventhub.entities.User;
import com.alpha.team.eventhub.exceptions.EventHubException;
import com.alpha.team.eventhub.exceptions.ExceptionFactory;
import com.alpha.team.eventhub.sharedprefrence.ServicePreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by clasence on 28,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 */
public class GeneralHelpers {

    private Context context;

    public GeneralHelpers(Context context)
    {
        this.context = context;
    }

    /**
     * Change String to User Object
     * @param object
     * @return
     */
    public static User serializeToUser(Object object){
        Gson gson = new Gson();
        String jsonString = gson.toJson(object);

        Type type = new TypeToken<User>(){}.getType();
        User user = gson.fromJson(jsonString, type);

        return user;

    }

    /**
     * Change String to Event Object
     * @param jsonString
     * @return
     */
    public static Event serializeToEvent(String jsonString){
        Gson gson = new Gson();
        Type type = new TypeToken<Event>(){}.getType();
        Event event = gson.fromJson(jsonString, type);

        return event;

    }

    /**
     * Get text from edittext
     * @param editText
     * @return
     */
    public static String getEditTextText(EditText editText) {
        return editText.getText().toString();
    }


    /**
     * Check if user had liked event.
     * @param context
     * @param event
     * @return
     */
    public static boolean isLiked(Context context, Event event){
        ServicePreferences servicePreferences = new ServicePreferences(context);
        if(servicePreferences.hasLiked(event)){
            return true;
        }
        return false;

    }


    /**
     * Update like in SharedPreference
     * @param context
     * @param event
     * @return
     */
    public static boolean updateLike(Context context, Event event){
        ServicePreferences servicePreferences = new ServicePreferences(context);
        if(servicePreferences.insertLike(event)){
            return true;
        }
        return false;
    }


    /**
     * Change date from String to Date Object
     * @param dateString
     * @return
     * @throws EventHubException
     */
    public static Date formatDate(String dateString) throws EventHubException {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {

            e.printStackTrace();
            Long longDate = Long.parseLong(dateString);
            if(longDate!=null){
                Date date = new Date(longDate);
                if(date!=null && date.toString().length()>6){
                    return date;
                }
            }else{
                throw  ExceptionFactory.getException(ExceptionFactory.ExceptionType.INVALID_DATE);
            }

        }
     return null;

    }


    /**
     * Get int IDs from String
     * @param inCategories
     * @return
     */
    public static ArrayList<Integer> getCategoryIdsFromString(String inCategories){

        ArrayList<Integer> outCategories = new ArrayList<>();


        String[] cats = inCategories.split(",");

        for(String cat: cats){
            if(cat!=null && cat.trim().length()>0) {
                outCategories.add(Integer.parseInt(cat));
            }
        }

        return outCategories;

    }

}
