package com.reallyworld.rwmoonjourney.core;

import com.reallyworld.rwmoonjourney.configs.Config;
import com.reallyworld.rwmoonjourney.configs.Messages;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.logging.Logger;

/**
 * Service for delayed event stating
 */
public class EventTimerService {
    private final EventManager eventManager;
    private final Plugin plugin;
    private final Logger logger;

    public EventTimerService(EventManager eventManager, Plugin plugin, Logger logger){
        this.eventManager = eventManager;
        this.logger = logger;
        this.plugin = plugin;
    }

    /**
     * Start event timer
     */
    public void starTimer(){
        long timeToStart = secondsUntil(
                Config.getInt("startup-time.week-day"),
                Config.getString("startup-time.time"),
                Config.getString("startup-time.time-zone")
        );

        String mesage = Messages.message("logs.timer-set")
                        .replace("{H}", secondsToTime(timeToStart));

        logger.info(mesage);

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            logger.info(Messages.message("logs.event.timer-start"));
            try {
                eventManager.start();
            }
            catch (Exception ignored){}
            starTimer();
        }, timeToStart);
    }

    /**
     * Cacls seconds to begin event
     * @param dayOfWeek day of week event must start
     * @param timeString start time
     * @param timeZone timezone
     * @return seconds to event
     */
    //chat gpt code ))
    private long secondsUntil(int dayOfWeek, String timeString, String timeZone) {
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
    private String secondsToTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        return String.format("%d:%02d", hours, minutes);
    }
}
