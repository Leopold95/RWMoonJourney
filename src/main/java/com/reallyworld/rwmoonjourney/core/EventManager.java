package com.reallyworld.rwmoonjourney.core;

import com.reallyworld.rwmoonjourney.configs.Messages;
import com.reallyworld.rwmoonjourney.constants.Commands;
import com.reallyworld.rwmoonjourney.constants.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class EventManager {
    private Logger logger;
    private Plugin plugin;

    public EventManager(Plugin plugin, Logger logger){
        this.logger = logger;
        this.plugin = plugin;
    }

    public void start(){
        logger.info(Messages.message("logs.event.start"));
        plugin.getServer().sendMessage(Messages.text("event.start"));
    }

    public void stop(){
        logger.info(Messages.message("logs.event.stop"));
        plugin.getServer().sendMessage(Messages.text("event.stop"));
    }

    public void join(@NotNull Player player){

    }
}
