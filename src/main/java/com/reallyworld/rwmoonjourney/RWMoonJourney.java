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
import com.reallyworld.rwmoonjourney.core.event.PlayerService;
import com.reallyworld.rwmoonjourney.listeners.*;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Slf4j
public final class RWMoonJourney extends JavaPlugin {
    public static RWMoonJourney plugin; //used only for keys

    private Logger logger;

    private PlayerService playerService;
    private MobService mobService;
    private EventService eventService;
    private IBreathService breathService;
    private ChestService chestService;
    private EventTimerService eventTimer;

    //di
    private Economy economy;

    @Override
    public void onEnable() {
        plugin = this;
        logger = getLogger();

        Config.init(this, "config.yml");
        Messages.init(this, "messages.yml");
        ChestsConfig.init(this, "chests.yml");

        if (!setupEconomy() ) {
            logger.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        playerService = new PlayerService();
        mobService = new MobService();
        chestService = new ChestService(logger);
        breathService = new WaterBreathServiceImpl();
        eventService = new EventService(this, logger, economy, breathService, chestService, mobService, playerService);
        eventTimer = new EventTimerService(eventService, plugin, logger);
        eventTimer.startTimer();

        printChestsCount();

        registerCommands();
        registerListeners();

        if(!checkBeforeEnable()){
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    /**
     * Проверяет некоторіе вещи перед запуском плагина.
     * @return да - можно запустить, нет - плагин будет выключен
     */
    private boolean checkBeforeEnable(){
        var eventworld = Bukkit.getWorld(Config.getString("world.name"));
        if(eventworld == null){
            logger.warning(Messages.getString("logs.no-event-world"));
            return false;
        }

        var spawnWorld = Bukkit.getWorld(Config.getString("spawn.world"));
        if(spawnWorld == null){
            logger.warning(Messages.getString("logs.no-spawn-world"));
            return false;
        }

        return true;
    }

    /**
     * Вывод количество созданных сундуков.
     */
    private void printChestsCount(){
        var message = Messages.getString("logs.total-chests")
                .replace("{count}", String.valueOf(ChestsConfig.getCount()));
        logger.info(message);
    }

    private void registerCommands(){
        getCommand(Commands.Base).setExecutor(new CommandBase(this, eventService, chestService));
        getCommand(Commands.Base).setTabCompleter(new CommandBaseCompleter());
    }

    private void registerListeners(){
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(eventService), this);
        getServer().getPluginManager().registerEvents(new MobKillListener(economy), this);
        getServer().getPluginManager().registerEvents(new CommandListener(eventService), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(eventService, playerService), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(eventService), this);
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
