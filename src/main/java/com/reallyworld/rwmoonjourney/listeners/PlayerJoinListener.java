package com.reallyworld.rwmoonjourney.listeners;

import com.reallyworld.rwmoonjourney.core.event.EventService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {
    private final EventService eventService;

    public PlayerJoinListener(
            @NotNull EventService eventService
    ) {
        this.eventService = eventService;
    }

    /**
     * Когда игрок заходит, значит он презашел - его статус как игрока ивнета
     * должен быть убран
     * @param event ивент захода на сервер
     */
    @EventHandler
    private void onJoin(PlayerJoinEvent event){
        eventService.remove(event.getPlayer());
    }
}
