package com.alpha.team.eventhub.listerners;

import android.app.Activity;
import android.view.View;

import com.alpha.team.eventhub.entities.Event;

/**
 * Created by clasence on 30,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 */
public interface Strategy {
    void execute(Activity context, View v, Event event);
}
