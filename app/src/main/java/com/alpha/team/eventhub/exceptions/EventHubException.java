package com.alpha.team.eventhub.exceptions;

import com.alpha.team.eventhub.utils.LoggerHelper;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by clasence on 04,December,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 * Exception superclass for EventHub
 */
public class EventHubException extends Exception {

    public EventHubException(String message){
        super(message);
        LoggerHelper.logMessage(getClass().getName(), getClass().getSimpleName() + " thrown", Level.SEVERE);
    }
}
