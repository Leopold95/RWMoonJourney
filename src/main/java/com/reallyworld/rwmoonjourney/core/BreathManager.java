package com.reallyworld.rwmoonjourney.core;

import com.reallyworld.rwmoonjourney.configs.Config;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class BreathManager {
    public void addBreath(@NotNull Player player){
        player.getPersistentDataContainer().set(Keys.HAS_BREATH, PersistentDataType.INTEGER, 1);
    }

    public void removeBreath(@NotNull Player player){
        player.getPersistentDataContainer().remove(Keys.HAS_BREATH);
    }

    public void tryDamage(@NotNull Player player){
        if(!player.getPersistentDataContainer().has(Keys.HAS_BREATH, PersistentDataType.INTEGER))
            return;

        if(!player.getPersistentDataContainer().has(Keys.IS_ON_EVENT, PersistentDataType.INTEGER))
            return;

        player.damage(Config.getDouble("breath-damage"));
    }
}
