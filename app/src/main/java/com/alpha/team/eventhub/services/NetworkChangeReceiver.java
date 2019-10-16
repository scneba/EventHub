package com.alpha.team.eventhub.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alpha.team.eventhub.network.CheckInternet;
import com.alpha.team.eventhub.sharedprefrence.ServicePreferences;
import com.alpha.team.eventhub.utils.Constants;

/**
 * Created by clasence on 25,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("receiver", intent.getAction());

        if (intent.getAction().equalsIgnoreCase("android.net.conn.CONNECTIVITY_CHANGE") || intent.getAction().equalsIgnoreCase("android.net.wifi.WIFI_STATE_CHANGED")) {
            if (CheckInternet.isNetworkAvailable(context)) {
                ServicePreferences servicePreferences = new ServicePreferences(context);
                if (!servicePreferences.getGotCountries()) {
                    Intent mIntent = new Intent(context, UpdateService.class);
                    mIntent.setAction(Constants.COLUMN_GOT_COUNTRIES);
                    context.startService(mIntent);
                }

                if (!servicePreferences.getGotCategories()) {
                    Intent mIntent = new Intent(context, UpdateService.class);
                    mIntent.setAction(Constants.COLUMN_GOT_CATEGORIES);
                    context.startService(mIntent);
                }
            }
        }
    }
}
