package com.reallyworld.rwmoonjourney.listeners;

import com.reallyworld.rwmoonjourney.configs.Config;
import com.reallyworld.rwmoonjourney.configs.Messages;
import com.reallyworld.rwmoonjourney.core.event.EventService;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

public class CommandListener implements Listener {
    private final EventService eventService;

    public CommandListener(
            @NotNull EventService eventService
    ){
        this.eventService = eventService;
    }

    @EventHandler
    private void onCommand(PlayerCommandPreprocessEvent event){
        var command = event.getMessage().split(" ")[0].substring(1).toLowerCase();

        if(!eventService.isPlayerOnEvent(event.getPlayer().getUniqueId()))
            return;

        if(isBlocked(command)){
            event.getPlayer().sendMessage(Messages.text("event.no-command"));
            event.setCancelled(true);
        }
    }

    private boolean isBlocked(String command){
        var bCommand = Bukkit.getPluginCommand(command);
        if(bCommand == null)
            return false;

        var blockedCommands = Config.getStringList("prevent-commands");

        return blockedCommands.contains(bCommand.getName()) || bCommand.getAliases().stream().anyMatch(blockedCommands::contains);
    }
}
