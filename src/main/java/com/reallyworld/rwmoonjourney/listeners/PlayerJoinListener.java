package com.reallyworld.rwmoonjourney.listeners;

import com.reallyworld.rwmoonjourney.core.BreathManager;
import com.reallyworld.rwmoonjourney.core.EventManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {
    private final BreathManager breathManager;
    private final EventManager eventManager;

    public PlayerJoinListener(
            @NotNull BreathManager breathManager,
            @NotNull EventManager eventManager
            ) {
        this.breathManager = breathManager;
        this.eventManager = eventManager;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event){
        breathManager.removeBreath(event.getPlayer());
        eventManager.remove(event.getPlayer());
    }
}
