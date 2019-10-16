package com.alpha.team.eventhub.listerners;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alpha.team.eventhub.R;
import com.alpha.team.eventhub.activities.LoginActivity;
import com.alpha.team.eventhub.activities.MainActivity;
import com.alpha.team.eventhub.db.CountryEntry;
import com.alpha.team.eventhub.db.EventEntry;
import com.alpha.team.eventhub.entities.Country;
import com.alpha.team.eventhub.entities.Event;
import com.alpha.team.eventhub.entities.User;
import com.alpha.team.eventhub.network.RetrofitClientInstance;
import com.alpha.team.eventhub.network.RetrofitService;
import com.alpha.team.eventhub.sharedprefrence.UserPreference;
import com.alpha.team.eventhub.utils.GeneralHelpers;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by clasence on 01,December,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 *
 */
public class RegisterEvent implements Strategy {
    private UserPreference userPreference;

    @Override
    public void execute(Activity context, View v, Event event) {
        userPreference = new UserPreference(context);
        if(userPreference.isLoggedIn()){
            registerToEvent(context,v,event);
        }else{
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);

        }

    }


    /**
     * Register for event
     * @param context
     * @param v
     * @param event
     */
    private void registerToEvent(final Context context, final View v, final Event event){
        //create retrofit interface
        ((Button) v).setText(context.getString(R.string.registering));

        RetrofitService retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);

        HashMap<String, Object> map = new HashMap<>();
        map.put("status", 0);
        map.put("userId", userPreference.getUserId());
        map.put("eventId", event.getEventId());
        Call<HashMap<String, Object>> call = retrofitService.registerForEvent(map);
        call.enqueue(new Callback<HashMap<String, Object>>() {
            @Override
            public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                Log.e("response", response.body().toString());
                ((Button) v).setText(context.getString(R.string.register));
                if (response.code() == 200) {
                    Double code = (Double) response.body().get("status");
                    if (code.equals(1.0)) {
                        if(saveToSQLite(event,context)) {
                            ((Button) v).setVisibility(View.GONE);

                            Toast.makeText(context, context.getString(R.string.register_succcess), Toast.LENGTH_LONG).show();
                        }

                    } else if (code.equals(2.0)) {
                        if(saveToSQLite(event,context)) {
                            ((Button) v).setVisibility(View.GONE);

                            Toast.makeText(context, context.getString(R.string.user_already_registered), Toast.LENGTH_SHORT).show();
                        }

                    }
                }


            }

            @Override
            public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                Log.e("GetCountriesService", t.toString());
            }
        });

    }


    /**
     * Save a registered user to SQLite database
     * @param event
     * @param context
     * @return
     */
    private boolean saveToSQLite(Event event, Context context){
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventEntry.COLUMN_EVENT_ID,event.getEventId());
        contentValues.put(EventEntry.COLUMN_USER_ID,userPreference.getUserId());
        contentValues.put(EventEntry.COLUMN_COUNTRY_ID,event.getCountryId());
        contentValues.put(EventEntry.COLUMN_CITY_ID, event.getCityId());
        contentValues.put(EventEntry.COLUMN_CATEGORY_ID, event.getCategoryId());
        contentValues.put(EventEntry.COLUMN_EVENT_STATUS, event.getEventStatus());

        contentValues.put(EventEntry.COLUMN_EVENT_NAME, event.getEventName());
        contentValues.put(EventEntry.COLUMN_ADDRESS, event.getAddress());
        contentValues.put(EventEntry.COLUMN_DESCRIPTION, event.getDescription());
        contentValues.put(EventEntry.COLUMN_PICTURE, event.getPicture());

        contentValues.put(EventEntry.COLUMN_LATITUDE, event.getLatitude());
        contentValues.put(EventEntry.COLUMN_LONGITUDE, event.getLongitude());

        contentValues.put(EventEntry.COLUMN_DATE_FROM, event.getDateFrom());
        contentValues.put(EventEntry.COLUMN_DATE_TO, event.getDateTo());
        contentValues.put(EventEntry.COLUMN_CREATED_AT, event.getCreatedAt());
        contentValues.put(EventEntry.COLUMN_UPDATED_AT, event.getUpdatedAt());
        contentValues.put(EventEntry.COLUMN_ACCEPT_REG, event.getAcceptOnRegistration());

        try {
            Uri uri = context.getContentResolver().insert(EventEntry.EVENT_CONTENT_URI, contentValues);
            if(uri!=null){
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
