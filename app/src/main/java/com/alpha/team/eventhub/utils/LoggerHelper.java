/**
 * 
 * @author Clasence Neba Shu
 * 16th of September 2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 */
package com.alpha.team.eventhub.utils;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class LoggerHelper {

	public static void logMessage(String loggerName, String message, Level logger_level) {
		Logger logger = Logger.getLogger(loggerName);
		logger.addHandler(new ConsoleHandler());
		try {
			FileHandler fileHandler = new FileHandler("EventHub.txt");
			fileHandler.setFormatter(new SimpleFormatter());

			logger.addHandler(fileHandler);

		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		logger.log(logger_level, message);

	}

}