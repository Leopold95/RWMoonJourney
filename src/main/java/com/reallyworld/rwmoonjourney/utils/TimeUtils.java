package com.reallyworld.rwmoonjourney.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class TimeUtils {
    /**
     * Cacls seconds to begin event
     * @param dayOfWeek day of week event must start
     * @param timeString start time
     * @param timeZone timezone
     * @return seconds to event
     */
    //chat gpt code ))
    public static long secondsUntil(int dayOfWeek, String timeString, String timeZone) {
        LocalTime targetTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("H:mm"));

        ZoneId zone = timeZone == null || timeZone.isEmpty()
                ? ZoneId.systemDefault()
                : ZoneId.of(timeZone);

        ZonedDateTime now = ZonedDateTime.now(zone);
        ZonedDateTime targetDateTime = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(dayOfWeek)))
                .withHour(targetTime.getHour())
                .withMinute(targetTime.getMinute())
                .withSecond(0)
                .withNano(0);


        if (targetDateTime.isBefore(now)) {
            targetDateTime = targetDateTime.plusWeeks(1);
        }

        return Duration.between(now, targetDateTime).getSeconds();
    }

    //chat gpt code ))
    public static String secondsToTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        return String.format("%d:%02d", hours, minutes);
    }
}
