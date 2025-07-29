package com.reallyworld.rwmoonjourney.core;

import com.reallyworld.rwmoonjourney.configs.Config;
import com.reallyworld.rwmoonjourney.configs.Messages;
import com.reallyworld.rwmoonjourney.utils.TimeUtils;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class EventManager {
    private final List<UUID> players = new ArrayList<>();
    private boolean isEventActive = false;
    private BukkitTask eventLoop;

    private final Logger logger;
    private final Plugin plugin;
    private final Economy economy;
    private final BreathManager breathManager;
    private final EventChestManager chestManager;

    public EventManager(
            @NotNull Plugin plugin,
            @NotNull Logger logger,
            @NotNull Economy economy,
            @NotNull BreathManager breathManager,
            @NotNull EventChestManager chestManager
    ){
        this.logger = logger;
        this.plugin = plugin;
        this.economy = economy;
        this.breathManager = breathManager;
        this.chestManager = chestManager;
    }

    public void start(){
        logger.info(Messages.message("logs.event.start"));
        plugin.getServer().sendMessage(Messages.text("event.start"));
        players.clear();
        isEventActive = true;
        chestManager.respawnAll();

        eventLoop = Bukkit.getScheduler().runTask(plugin, this::eventLoop);
    }

    public void stop(){
        logger.info(Messages.message("logs.event.stop"));
        plugin.getServer().sendMessage(Messages.text("event.stop"));
        players.clear();
        isEventActive = false;
        eventLoop.cancel();
        eventLoop = null;
    }

    public void join(@NotNull Player player){
        int eventCost = Config.getInt("event-join-cost");

        EconomyResponse resp = economy.withdrawPlayer(player, eventCost);
        if(!resp.transactionSuccess()){
            player.sendMessage(Messages.text("event.no-money-join"));
            return;
        }

        player.getPersistentDataContainer().set(Keys.IS_ON_EVENT, PersistentDataType.INTEGER, 1);
        players.add(player.getUniqueId());
    }

    public void kick(@NotNull Player player, boolean isSilent){
        player.getPersistentDataContainer().remove(Keys.IS_ON_EVENT);
    }

    public void remove(@NotNull Player player){
        player.getPersistentDataContainer().remove(Keys.IS_ON_EVENT);
        breathManager.removeBreath(player);
    }

    public boolean isPlayerWasOnCurrentEvent(UUID playerId){
        return players.contains(playerId);
    }

    public void time(@NotNull Player player){
        long timeToStart = TimeUtils.secondsUntil(
                Config.getInt("startup-time.week-day"),
                Config.getString("startup-time.time"),
                Config.getString("startup-time.time-zone")
        );

        String message = Messages.getString("event.time")
                .replace("{time}", TimeUtils.secondsToTime(timeToStart));
        player.sendMessage(Component.text(message));
    }

    public void buyBreath(@NotNull Player player){
        if(breathManager.has(player)){
            player.sendMessage(Messages.text("event.breath.already-has"));
            return;
        }

        int breathCost = Config.getInt("event-breath.cost");
        EconomyResponse resp = economy.withdrawPlayer(player, breathCost);
        if(resp.transactionSuccess()){
            player.sendMessage(Messages.text("event.breath.no-money"));
            return;
        }

        breathManager.addBreath(player);
    }

    private void eventLoop(){
        while (isEventActive){
            logger.info("updatings");

            for(UUID pUuid: players){
                Player player = Bukkit.getPlayer(pUuid);
                if(player == null || !player.isOnline())
                    continue;

                breathManager.tryDamage(player);
            }
        }
    }
}
