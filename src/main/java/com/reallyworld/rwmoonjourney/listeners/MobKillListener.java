package com.reallyworld.rwmoonjourney.listeners;

import com.reallyworld.rwmoonjourney.configs.Config;
import com.reallyworld.rwmoonjourney.configs.Messages;
import com.reallyworld.rwmoonjourney.core.Keys;
import lombok.var;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

public class MobKillListener implements Listener {
    private final Economy economy;

    public MobKillListener(Economy economy){
        this.economy = economy;
    }

    @EventHandler
    private void onKillEventMob(EntityDeathEvent event){
        if(!event.getEntity().getPersistentDataContainer().has(Keys.EVENT_MOB, PersistentDataType.INTEGER))
            return;

        if(!(event.getEntity().getKiller() == null))
            return;

        var killer = event.getEntity().getKiller();

        var killPrice = Config.getDouble("event-mobs.kill-money");
        var result = economy.depositPlayer(event.getEntity().getKiller(), killPrice);
        if(result.transactionSuccess()){
            killer.sendMessage(Messages.text("event.mob.kill"));
        }
    }
}
