package com.reallyworld.rwmoonjourney.listeners;


import com.reallyworld.rwmoonjourney.core.event.EventService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDeathListener implements Listener {
    private final EventService eventService;

    public PlayerDeathListener(
            @NotNull EventService eventService
    ){
        this.eventService = eventService;
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event){
        if(!eventService.isPlayerOnEvent(event.getEntity().getUniqueId()))
            return;

        eventService.remove(event.getEntity());
    }
}
