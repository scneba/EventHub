package com.alpha.team.eventhub.exceptions;

/**
 * Created by clasence on 04,December,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 * Handles date exceptions
 */
public class InvalidDateException extends EventHubException {
    public InvalidDateException(String message) {
        super(message);
    }
}
