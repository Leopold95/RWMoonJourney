package com.reallyworld.rwmoonjourney;

import com.reallyworld.rwmoonjourney.commands.CommandBase;
import com.reallyworld.rwmoonjourney.commands.CommandBaseCompleter;
import com.reallyworld.rwmoonjourney.configs.Config;
import com.reallyworld.rwmoonjourney.configs.Messages;
import com.reallyworld.rwmoonjourney.constants.Commands;
import com.reallyworld.rwmoonjourney.core.EventManager;
import com.reallyworld.rwmoonjourney.core.EventTimerService;
import com.reallyworld.rwmoonjourney.listeners.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class RWMoonJourney extends JavaPlugin {
    public static RWMoonJourney plugin; //used only for keys

    public EventManager eventManager;
    private EventTimerService eventTimer;

    @Override
    public void onEnable() {
        plugin = this;

        Config.init(this, "config.yml");
        Messages.init(this, "messages.yml");

        eventManager = new EventManager(this, getLogger());
        eventTimer = new EventTimerService(eventManager, plugin, getLogger());
        eventTimer.starTimer();

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    private void registerCommands(){
        getCommand(Commands.Base).setExecutor(new CommandBase(eventManager));
        getCommand(Commands.Base).setTabCompleter(new CommandBaseCompleter());
    }

    private void registerListeners(){
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }
}
