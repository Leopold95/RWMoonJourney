package com.reallyworld.rwmoonjourney.core.event;

import com.reallyworld.rwmoonjourney.configs.Config;
import com.reallyworld.rwmoonjourney.core.Keys;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class MobService {
    public void spawnMobs(@NotNull Location chestLocation){
        var mobsCount = Config.getRandomInt("event-mobs.per-chest");

        for(int i = 0; i < mobsCount; i++){
            var spreadX = Config.getRandomDouble("event-mobs.spawn-spread-xz");
            var spreadZ = Config.getRandomDouble("event-mobs.spawn-spread-xz");
            var copy = chestLocation.clone().add(spreadX, 0, spreadZ);
            spawnMob(copy);
        }
    }

    public void removeAllCustomMobs(){
        var worldStr = Config.getString("world.name");
        var world = Bukkit.getWorld(worldStr);

        if(world == null)
            return;
        
        for(var eventEntity: world.getEntities()){
            if(!isEventMob(eventEntity))
                continue;

            eventEntity.remove();
        }
    }

    public boolean isEventMob(@NotNull Entity entity){
        return entity.getPersistentDataContainer().has(Keys.EVENT_MOB, PersistentDataType.INTEGER);
    }

    private void spawnMob(@NotNull Location mobLocation){
        var zomb = (LivingEntity) mobLocation.getWorld().spawnEntity(mobLocation, EntityType.ZOMBIE);
        zomb.getPersistentDataContainer().set(Keys.EVENT_MOB, PersistentDataType.INTEGER, 0);
        zomb.setAI(false);
    }
}
