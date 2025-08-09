package com.reallyworld.rwmoonjourney.core.event;

import com.reallyworld.rwmoonjourney.configs.Config;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerService {
    /**
     * Телепортировать игрока на спавн
     * @param player игрок
     */
    public void teleportToSpawn(@NotNull Player player){
        var world = Bukkit.getWorld(Config.getString("spawn.world"));
        if(world == null)
            return;

        var x = Config.getInt("spawn.world.x");
        var y = Config.getInt("spawn.world.y");
        var z = Config.getInt("spawn.world.z");

        var location = new Location(world, x, y, z);

        player.teleportAsync(location);
    }

    /**
     * Телепортировать игрока в лобби
     * @param player игрок
     */
    public void teleportToLobby(@NotNull Player player){
        var world = Bukkit.getWorld(Config.getString("world.name"));
        if(world == null)
            return;

        var x = Config.getInt("world.lobby.x");
        var y = Config.getInt("world.lobby.y");
        var z = Config.getInt("world.lobby.z");

        var location = new Location(world, x, y, z);

        player.teleportAsync(location);
    }

    /**
     * Телепортирует всех игроков на точку ивента
     * @param player игрок
     */
    public void teleportToEvent(@NotNull Player player){
        var world = Bukkit.getWorld(Config.getString("world.name"));
        if(world == null)
            return;


        var x = Config.getInt("world.spawn.x");
        var y = Config.getInt("world.spawn.y");
        var z = Config.getInt("world.spawn.z");

        var location = new Location(world, x, y, z);
        player.teleportAsync(location);
    }

    /**
     * Удаляет весь лут у игрока
     * @param player игрок
     */
    public void removeLoot(@NotNull Player player){
        player.getInventory().clear();
    }

    /**
     * Дропает лет игрока и очищает его инвентарь
     * @param player игрок
     */
    public void dropLoot(@NotNull Player player){ //TODO возможно сделать прсто убийством ?
        for (var item: player.getInventory()){
            if(item == null)
                continue;

            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }

        player.getInventory().clear();
    }
}
