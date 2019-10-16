package com.alpha.team.eventhub.exceptions;

/**
 * Created by clasence on 06,December,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 * Handles errors resulting from bulk inserts
 */
public class BulkInsertException extends EventHubException {
    public BulkInsertException(String message) {
        super(message);
    }
}
