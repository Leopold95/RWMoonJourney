package com.reallyworld.rwmoonjourney.listeners;

import com.reallyworld.rwmoonjourney.configs.Config;
import com.reallyworld.rwmoonjourney.core.event.EventService;
import com.reallyworld.rwmoonjourney.core.event.PlayerService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;

public class WorldChangeListener implements Listener {
    private final EventService eventService;
    private final PlayerService playerService;

    public WorldChangeListener(
            @NotNull EventService eventService,
            @NotNull PlayerService playerService
    ){
        this.eventService = eventService;
        this.playerService = playerService;
    }

    /**
     * Когда игрок меняет мир с ивентового на обычный, значит он презашел или тепнулся - его статус как игрока ивнета
     * должен быть убран
     * @param event ивент захода на сервер
     */
    @EventHandler
    private void onChangeWorld(PlayerChangedWorldEvent event){
        if(!eventService.isPlayerOnEvent(event.getPlayer().getUniqueId()))
            return;

        if(!event.getFrom().getName().equals(Config.getString("world.name")))
            return;

        playerService.removeLoot(event.getPlayer());
        eventService.remove(event.getPlayer());
    }
}
