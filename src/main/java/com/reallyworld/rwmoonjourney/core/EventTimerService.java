package com.reallyworld.rwmoonjourney.core;

import com.reallyworld.rwmoonjourney.configs.Config;
import com.reallyworld.rwmoonjourney.configs.Messages;
import com.reallyworld.rwmoonjourney.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

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
    public void startTimer(){
        long timeToStart = TimeUtils.secondsUntil(
                Config.getInt("startup-time.week-day"),
                Config.getString("startup-time.time"),
                Config.getString("startup-time.time-zone")
        );

        String mesage = Messages.message("logs.timer-set")
                        .replace("{H}", TimeUtils.secondsToTime(timeToStart));

        logger.info(mesage);

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            logger.info(Messages.message("logs.event.timer-start"));
            try {
                eventManager.start();
            }
            catch (Exception ignored){}
            startTimer();
        }, timeToStart);
    }
}
