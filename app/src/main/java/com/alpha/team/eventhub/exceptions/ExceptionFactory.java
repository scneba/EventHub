/**
 * 
 * @author Clasence Neba Shu
 * 16th of September 2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 */
package com.alpha.team.eventhub.exceptions;

/**
 * Created by clasence on 04,December,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 * Exception factory for EventHub
 */
public class ExceptionFactory {
	public static enum ExceptionType{INVALID_DATE,BULK_INSERT_ERROR, OPTION_EXIST,  OPTIONSET_EXIST, PIZZERIA_EXIST;}
	
	
	public static EventHubException getException(ExceptionType type) {

		if(type.equals(ExceptionType.INVALID_DATE)) {

			return new InvalidDateException("The date is invalid");
		}else if(type.equals(ExceptionType.BULK_INSERT_ERROR)) {

			return new BulkInsertException("Error doing bulk insert");
		}


		return (EventHubException) new Exception("Problem");
	}
	
	public String toString() {
		return "Factory for generating appropriate exception ";
	}

}
