package com.alpha.team.eventhub.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.alpha.team.eventhub.R;
import com.alpha.team.eventhub.activities.EventDetailsActivity;
import com.alpha.team.eventhub.activities.MainActivity;
import com.alpha.team.eventhub.entities.Event;
import com.alpha.team.eventhub.sharedprefrence.ServicePreferences;
import com.alpha.team.eventhub.utils.GeneralHelpers;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Map;

import static com.alpha.team.eventhub.utils.Constants.CURENT_EVENT;
import static com.alpha.team.eventhub.utils.Constants.DATE_SOURCE;

/**
 * Created by clasence on 04,December,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 * <p>
 * Receive and process firebase notifications
 */
public class FirebaseService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e("FirebaseService", "From: " + remoteMessage.getFrom());
        Log.e("FirebaseService", "Body: " + remoteMessage.getData());

        if (remoteMessage.getData() != null && remoteMessage.getData().containsKey("type")) {
            int type = Integer.parseInt(remoteMessage.getData().get("type"));
            switch (type) {
                case 1: {
                    Event event = GeneralHelpers.serializeToEvent(remoteMessage.getData().get("body").toString());
                    if (event != null) {
                        if (isFavourite(event))
                            sendNotification(event);
                    } else {
                        Log.e("FirebaseService", "event is null");
                    }
                    break;

                }
            }

        }
    }

    /**
     * Check if Category is favourite
     *
     * @param event
     * @return
     */
    private boolean isFavourite(Event event) {
        ServicePreferences servicePreferences = new ServicePreferences(getApplicationContext());
        String categoryString = servicePreferences.getCategories();
        Log.e("favCats", categoryString);
        if (categoryString != null && categoryString.trim().length() > 0) {
            ArrayList<Integer> categories = GeneralHelpers.getCategoryIdsFromString(categoryString);
            for (int i : categories) {
                if (i == event.getCategoryId()) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Send notification to user
     * @param event
     */
    private void sendNotification(Event event) {

        Intent intent = new Intent(this, EventDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CURENT_EVENT, event);
        bundle.putInt(DATE_SOURCE, 1);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(event.getEventName())
                .setContentText(event.getAddress())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }

}
