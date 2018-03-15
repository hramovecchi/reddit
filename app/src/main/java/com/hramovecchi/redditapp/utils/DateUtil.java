package com.hramovecchi.redditapp.utils;

import org.joda.time.DateTime;
import org.joda.time.Period;

public class DateUtil {

    public static String toHoursAgoFormat(long startTimeInMillis) {
        DateTime startTime = new DateTime(startTimeInMillis);
        DateTime now = DateTime.now();
        Period period = new Period(startTime, now);
        int hoursAgo = period.getHours();
        int minutesAgo = period.getMinutes();
        String hoursAgoResult = (hoursAgo == 0) ? "": "" + hoursAgo + " hours ago";
        String minutesAgoResult = (minutesAgo == 0) ? "": "" + minutesAgo + " minutes ago";
        return (!minutesAgoResult.isEmpty() && !hoursAgoResult.isEmpty()) ?
                hoursAgoResult.substring(0, hoursAgoResult.length()-4) + ", " + minutesAgoResult :
                hoursAgoResult + minutesAgoResult;
    }
}
