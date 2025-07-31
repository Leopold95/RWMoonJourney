package com.reallyworld.rwmoonjourney.core.event;

import com.reallyworld.rwmoonjourney.configs.Config;
import com.reallyworld.rwmoonjourney.configs.Messages;
import com.reallyworld.rwmoonjourney.core.Keys;
import com.reallyworld.rwmoonjourney.core.WaterBreathServiceImpl;
import com.reallyworld.rwmoonjourney.utils.TimeUtils;
import lombok.var;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Основной сервис контроля событием
 */
public class EventService {
    private final List<UUID> players = new ArrayList<>();
    private boolean isEventActive = false;
    private BukkitTask eventLoop;

    private final Logger logger;
    private final Plugin plugin;
    private final Economy economy;
    private final WaterBreathServiceImpl breathService;
    private final ChestService chestService;
    private final MobService mobService;

    private EventState eventState = EventState.Waiting;

    public EventService(
            @NotNull Plugin plugin,
            @NotNull Logger logger,
            @NotNull Economy economy,
            @NotNull WaterBreathServiceImpl breathService,
            @NotNull ChestService chestService,
            @NotNull MobService mobService
    ){
        this.logger = logger;
        this.plugin = plugin;
        this.economy = economy;
        this.breathService = breathService;
        this.chestService = chestService;
        this.mobService = mobService;
    }

    public void startJoining(){

    }

    /**
     * Начать событие
     */
    public void start(){
        logger.info(Messages.message("logs.event.start"));
        plugin.getServer().sendMessage(Messages.text("event.start"));
        players.clear();
        isEventActive = true;
        chestService.respawnAll();

        eventLoop = Bukkit.getScheduler().runTask(plugin, this::eventLoop);
    }

    /**
     * Остановить событие
     */
    public void stop(){
        logger.info(Messages.message("logs.event.stop"));
        plugin.getServer().sendMessage(Messages.text("event.stop"));
        players.clear();
        isEventActive = false;
        eventLoop.cancel();
        eventLoop = null;
    }

    /**
     * Присоединиться к событию.
     * @param player игрок
     */
    public void join(@NotNull Player player){
        if(eventState == EventState.Waiting){
            player.sendMessage(Messages.text("event.now-waiting"));
            return;
        }

        if(eventState == EventState.Running){
            player.sendMessage(Messages.text("event.now-running"));
            return;
        }

        int eventCost = Config.getInt("event-join-cost");

        EconomyResponse resp = economy.withdrawPlayer(player, eventCost);
        if(!resp.transactionSuccess()){
            player.sendMessage(Messages.text("event.no-money-join"));
            return;
        }

        player.getPersistentDataContainer().set(Keys.IS_ON_EVENT, PersistentDataType.INTEGER, 1);
        players.add(player.getUniqueId());

        teleportToLobby(player);
    }

    /**
     * Покинуть событие
     * @param player игрок
     */
    public void leave(@NotNull Player player){
        remove(player);
        player.sendMessage(Messages.getText("event.leave"));
    }

    /**
     * Выгнать игрока с события
     * @param player игрок, который кикает
     * @param targetName ник того, кого кикнуть
     * @param isSilent флаг тихого выкидывая
     */
    public void kick(@NotNull Player player, @NotNull String targetName, boolean isSilent){
        player.getPersistentDataContainer().remove(Keys.IS_ON_EVENT);
    }

    /**
     * Удалить игрока с события. Удалить его статусы, теги итд
     * @param player игрок, которого удалить
     */
    public void remove(@NotNull Player player){
        player.getPersistentDataContainer().remove(Keys.IS_ON_EVENT);
        breathService.remove(player);
    }

    public boolean isPlayerWasOnCurrentEvent(UUID playerId){
        return players.contains(playerId);
    }

    /**
     * Показать время до начала события
     * @param player игрок, который водит команду
     */
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

    /**
     * Купить дыхание
     * @param player покупатель
     */
    public void buyBreath(@NotNull Player player){
        if(breathService.has(player)){
            player.sendMessage(Messages.text("event.breath.already-has"));
            return;
        }

        var breathCost = Config.getInt("event-breath.cost");
        var resp = economy.withdrawPlayer(player, breathCost);
        if(resp.transactionSuccess()){
            player.sendMessage(Messages.text("event.breath.no-money"));
            return;
        }

        breathService.add(player);
    }

    /**
     * Телепортировать игрока в лобби
     * @param player игрок
     */
    private void teleportToLobby(@NotNull Player player){
        var world = Bukkit.getWorld("world.name");
        if(world == null)
            return;

        var x = Config.getInt("world.lobby.x");
        var y = Config.getInt("world.lobby.y");
        var z = Config.getInt("world.lobby.z");

        var location = new Location(world, x, y, z);
        player.teleportAsync(location);
    }

    private void eventLoop(){
        while (isEventActive){
            logger.info("updatings");

            for(UUID pUuid: players){
                Player player = Bukkit.getPlayer(pUuid);
                if(player == null || !player.isOnline())
                    continue;

                breathService.tryDamage(player);
            }
        }
    }
}
