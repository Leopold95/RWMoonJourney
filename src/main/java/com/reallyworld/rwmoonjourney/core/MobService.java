package com.reallyworld.rwmoonjourney.core;

import com.reallyworld.rwmoonjourney.configs.Config;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

public class MobService {
    public void spawnCustomMobs(){

    }

    public void removeCustomAllMobsFromEvent(){
        var worldStr = Config.getString("event-world");
        var world = Bukkit.getWorld(worldStr);

        if(world == null)
            return;

        for(var eventEntity: world.getEntities()){
            if(!isEventMob(eventEntity))
                return;

            eventEntity.remove();
        }
    }

    public boolean isEventMob(Entity entity){
        return entity.getPersistentDataContainer().has(Keys.EVENT_MOB, PersistentDataType.INTEGER);
    }
}
