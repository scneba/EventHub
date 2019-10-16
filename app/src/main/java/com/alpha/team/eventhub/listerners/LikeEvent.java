package com.alpha.team.eventhub.listerners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alpha.team.eventhub.R;
import com.alpha.team.eventhub.activities.EventDetailsActivity;
import com.alpha.team.eventhub.activities.LoginActivity;
import com.alpha.team.eventhub.entities.Event;
import com.alpha.team.eventhub.network.RetrofitClientInstance;
import com.alpha.team.eventhub.network.RetrofitService;
import com.alpha.team.eventhub.sharedprefrence.UserPreference;
import com.alpha.team.eventhub.utils.GeneralHelpers;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by clasence on 01,December,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 * Strategy to like an event
 */
public class LikeEvent implements Strategy {
    private UserPreference userPreference;

    @Override
    public void execute(Activity context, View v, Event event) {
        userPreference = new UserPreference(context);
        if(userPreference.isLoggedIn()){
            sendLike(context,v,event);
        }else{
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);

        }
    }


    /**
     * Send like info to the database
     * @param context
     * @param v
     * @param event
     */
    private void sendLike(final Context context, final View v, final Event event){
        //create retrofit interface
        Toast.makeText(context, context.getString(R.string.registering), Toast.LENGTH_LONG).show();

        RetrofitService retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);

        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userPreference.getUserId());
        map.put("eventId", event.getEventId());
        Call<HashMap<String, Object>> call = retrofitService.likeEvent(map);
        call.enqueue(new Callback<HashMap<String, Object>>() {
            @Override
            public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                Log.e("response", response.body().toString());
                if (response.code() == 200) {
                    Double code = (Double) response.body().get("status");
                    if (code.equals(1.0)) {
                        if(GeneralHelpers.updateLike(context,event)) {
                            ((ImageView) v).setBackground(context.getResources().getDrawable(R.color.colorBrown));
                             event.setLikeCount(event.getLikeCount()+1);
                            EventDetailsActivity.tvLike.setText(String.valueOf(event.getLikeCount()));
                            Toast.makeText(context, context.getString(R.string.like_success), Toast.LENGTH_LONG).show();
                        }

                    }else if(code.equals(2.0)){
                        if(GeneralHelpers.updateLike(context,event)) {
                            ((ImageView) v).setBackground(context.getResources().getDrawable(R.color.colorBrown));
                             Toast.makeText(context, context.getString(R.string.like_success), Toast.LENGTH_LONG).show();
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
}
