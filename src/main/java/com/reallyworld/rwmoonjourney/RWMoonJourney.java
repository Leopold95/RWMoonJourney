package com.reallyworld.rwmoonjourney;

import com.reallyworld.rwmoonjourney.commands.CommandBase;
import com.reallyworld.rwmoonjourney.commands.CommandBaseCompleter;
import com.reallyworld.rwmoonjourney.configs.Config;
import com.reallyworld.rwmoonjourney.configs.Messages;
import com.reallyworld.rwmoonjourney.constants.Commands;
import com.reallyworld.rwmoonjourney.core.BreathManager;
import com.reallyworld.rwmoonjourney.core.EventManager;
import com.reallyworld.rwmoonjourney.core.EventTimerService;
import com.reallyworld.rwmoonjourney.listeners.PlayerJoinListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class RWMoonJourney extends JavaPlugin {
    public static RWMoonJourney plugin; //used only for keys

    public EventManager eventManager;
    public BreathManager breathManager;
    private EventTimerService eventTimer;

    //di
    public Economy economy;

    @Override
    public void onEnable() {
        plugin = this;

        Config.init(this, "config.yml");
        Messages.init(this, "messages.yml");

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        breathManager = new BreathManager();
        eventManager = new EventManager(this, getLogger(), economy, breathManager);
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
        getServer().getPluginManager().registerEvents(
                new PlayerJoinListener(breathManager, eventManager), this);
    }

    //of docks github code
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
}
