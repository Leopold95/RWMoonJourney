package com.reallyworld.rwmoonjourney.core;

import com.reallyworld.rwmoonjourney.abstraction.IBreathService;
import com.reallyworld.rwmoonjourney.configs.Config;
import com.reallyworld.rwmoonjourney.configs.Messages;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class WaterBreathServiceImpl implements IBreathService {
    private PotionEffect waterBreating = new PotionEffect(PotionEffectType.WATER_BREATHING, 99999, 1);

    public void add(@NotNull Player player){
        player.getPersistentDataContainer().set(Keys.HAS_BREATH, PersistentDataType.INTEGER, 1);
        player.sendMessage(Messages.text("event.breath.got"));
        player.addPotionEffect(waterBreating);
    }

    public void remove(@NotNull Player player){
        player.getPersistentDataContainer().remove(Keys.HAS_BREATH);
        player.removePotionEffect(PotionEffectType.WATER_BREATHING);
    }

    public boolean has(@NotNull Player player){
        return player.getPersistentDataContainer().has(Keys.HAS_BREATH, PersistentDataType.INTEGER);
    }

    public void tryDamage(@NotNull Player player){
        if(!player.getPersistentDataContainer().has(Keys.IS_ON_EVENT, PersistentDataType.INTEGER))
            return;

        if(player.getPersistentDataContainer().has(Keys.HAS_BREATH, PersistentDataType.INTEGER))
            return;

        player.damage(Config.getDouble("breath-damage"));
    }
}
