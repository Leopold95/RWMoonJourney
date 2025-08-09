package com.reallyworld.rwmoonjourney.listeners;

import com.reallyworld.rwmoonjourney.core.event.EventService;
import com.reallyworld.rwmoonjourney.core.event.PlayerService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerLeaveListener implements Listener {
    private final EventService eventService;
    private final PlayerService playerService;

    public PlayerLeaveListener(
            @NotNull EventService eventService,
            @NotNull PlayerService playerService
    ){
        this.eventService = eventService;
        this.playerService = playerService;
    }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event){
        if(!eventService.isPlayerOnEvent(event.getPlayer().getUniqueId()))
            return;

        playerService.dropLoot(event.getPlayer());
        eventService.remove(event.getPlayer());
    }
}
