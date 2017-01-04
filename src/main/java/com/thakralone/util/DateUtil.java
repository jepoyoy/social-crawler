package com.thakralone.util;

import java.util.Date;

public class DateUtil {
	
	public static String printElapsed(Date startDate, Date endDate){

		//milliseconds
		long different = endDate.getTime() - startDate.getTime();

		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;

		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;

		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;

		long elapsedSeconds = different / secondsInMilli;

		return String.format(
			"%d hours, %d minutes, %d seconds%n",
		    elapsedHours, elapsedMinutes, elapsedSeconds);

	}
	
}
