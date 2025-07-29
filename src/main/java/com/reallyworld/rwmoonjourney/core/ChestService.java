package com.reallyworld.rwmoonjourney.core;

import com.reallyworld.rwmoonjourney.configs.ChestsConfig;
import lombok.var;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChestService {
    public void respawnAll() {
        removePrevious();

        var chests = ChestsConfig.getAllChests();
        for (var chest: chests) {
            var location = chest.getLocation();
            var block = location.getBlock();
            block.setType(Material.CHEST);

            BlockFace direction = yawToCardinal(location.getYaw());

            if (block.getBlockData() instanceof Directional) {
                Directional directional = (Directional) block.getBlockData();
                directional.setFacing(direction);
                block.setBlockData(directional);
            }

            var chestState = (Chest) block.getState();
            var chestLoot = generateLoot(chest.getRarityCost()); //это надо заменить на кастомный генератор лута
            chestState.getInventory().setContents(chestLoot.toArray(new ItemStack[0]));
        }
    }

    public Set<ItemStack> generateLoot(int rarityCost){
        var items = new HashSet<ItemStack>();

        items.add(new ItemStack(Material.DIAMOND, 2));
        items.add(new ItemStack(Material.DIAMOND_SWORD, 4));
        items.add(new ItemStack(Material.DIRT, 64));

        return items;
    }

    private BlockFace yawToCardinal(float yaw) {
        yaw = (yaw % 360 + 360) % 360;

        if (yaw >= 315 || yaw < 45) {
            return BlockFace.SOUTH;
        } else if (yaw >= 45 && yaw < 135) {
            return BlockFace.WEST;
        } else if (yaw >= 135 && yaw < 225) {
            return BlockFace.NORTH;
        } else {
            return BlockFace.EAST;
        }
    }

    public void removePrevious(){
        List<Location> chests = ChestsConfig.getAllLocations();
        for(Location location: chests){
            if(location.getBlock().getType() == Material.CHEST){
                var chest = (Chest) location.getBlock().getState();
                chest.getInventory().setContents(new ItemStack[0]);

                location.getBlock().setType(Material.AIR);
            }
        }
    }
}
