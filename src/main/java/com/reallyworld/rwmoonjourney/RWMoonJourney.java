package com.reallyworld.rwmoonjourney;

import com.reallyworld.rwmoonjourney.abstraction.IBreathService;
import com.reallyworld.rwmoonjourney.commands.CommandBase;
import com.reallyworld.rwmoonjourney.commands.CommandBaseCompleter;
import com.reallyworld.rwmoonjourney.configs.ChestsConfig;
import com.reallyworld.rwmoonjourney.configs.Config;
import com.reallyworld.rwmoonjourney.configs.Messages;
import com.reallyworld.rwmoonjourney.constants.Commands;
import com.reallyworld.rwmoonjourney.core.EventTimerService;
import com.reallyworld.rwmoonjourney.core.WaterBreathServiceImpl;
import com.reallyworld.rwmoonjourney.core.event.ChestService;
import com.reallyworld.rwmoonjourney.core.event.EventService;
import com.reallyworld.rwmoonjourney.core.event.MobService;
import com.reallyworld.rwmoonjourney.listeners.CommandListener;
import com.reallyworld.rwmoonjourney.listeners.MobKillListener;
import com.reallyworld.rwmoonjourney.listeners.PlayerJoinListener;
import lombok.var;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class RWMoonJourney extends JavaPlugin {
    public static RWMoonJourney plugin; //used only for keys

    public MobService mobService;
    public EventService eventService;
    public IBreathService breathService;
    public ChestService chestService;
    private EventTimerService eventTimer;

    //di
    public Economy economy;

    @Override
    public void onEnable() {
        plugin = this;

        Config.init(this, "config.yml");
        Messages.init(this, "messages.yml");
        ChestsConfig.init(this, "chests.yml");

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        mobService = new MobService();
        chestService = new ChestService(getLogger());
        breathService = new WaterBreathServiceImpl();
        eventService = new EventService(this, getLogger(), economy, breathService, chestService, mobService);
        eventTimer = new EventTimerService(eventService, plugin, getLogger());
        eventTimer.startTimer();

        printChestsCount();

        registerCommands();
        registerListeners();

        if(!checkBeforeStart()){
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    private boolean checkBeforeStart(){
        var world = Bukkit.getWorld(Config.getString("world.name"));
        if(world == null){
            getLogger().info("event world is null: ");
            return false;
        }

        return true;
    }

    private void printChestsCount(){
        var message = Messages.getString("logs.total-chests")
                .replace("{count}", String.valueOf(ChestsConfig.getCount()));
        getLogger().info(message);
    }

    private void registerCommands(){
        getCommand(Commands.Base).setExecutor(new CommandBase(this, eventService, chestService));
        getCommand(Commands.Base).setTabCompleter(new CommandBaseCompleter());
    }

    private void registerListeners(){
        getServer().getPluginManager().registerEvents(
                new PlayerJoinListener(eventService), this);
        getServer().getPluginManager().registerEvents(new MobKillListener(economy), this);
        getServer().getPluginManager().registerEvents(new CommandListener(eventService), this);
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
