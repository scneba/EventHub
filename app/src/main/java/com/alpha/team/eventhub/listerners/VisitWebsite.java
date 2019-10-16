package com.alpha.team.eventhub.listerners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.alpha.team.eventhub.R;
import com.alpha.team.eventhub.entities.Event;

/**
 * Created by clasence on 30,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 */
public class VisitWebsite implements Strategy {
    @Override
    public void execute(Activity context, View v, Event event) {
        if(event.getWebsite()!=null) {
            if (!event.getWebsite().startsWith("http://") && !event.getWebsite().startsWith("https://")) {
                String newUrl = "http://" + event.getWebsite();
                event.setWebsite(newUrl);
            }
        }else{
            Toast.makeText(context,context.getString(R.string.no_website),Toast.LENGTH_SHORT).show();
        }

        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getWebsite()));
        context.startActivity(webIntent);

    }
}
