package com.reallyworld.rwmoonjourney.listeners;

import com.reallyworld.rwmoonjourney.core.WaterBreathServiceImpl;
import com.reallyworld.rwmoonjourney.core.EventService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {
    private final WaterBreathServiceImpl breathService;
    private final EventService eventService;

    public PlayerJoinListener(
            @NotNull WaterBreathServiceImpl breathService,
            @NotNull EventService eventService
            ) {
        this.breathService = breathService;
        this.eventService = eventService;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event){
        breathService.remove(event.getPlayer());
        eventService.remove(event.getPlayer());
    }
}
