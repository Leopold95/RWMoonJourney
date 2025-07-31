package com.reallyworld.rwmoonjourney.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {
    @EventHandler
    private void onCommand(PlayerCommandPreprocessEvent event){
        System.out.println(event.getMessage());
    }
}
