package com.reallyworld.rwmoonjourney.core;

import com.reallyworld.rwmoonjourney.configs.ChestsConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.util.Vector;

import java.util.List;

public class EventChestManager {
    public void spawn() {
        removePrevious();

        List<Location> chests = ChestsConfig.getAllLocations();
        for (Location location : chests) {
            Block block = location.getBlock();
            block.setType(Material.CHEST);

            BlockFace direction = yawToCardinal(location.getYaw());

            if (block.getBlockData() instanceof Directional) {
                Directional directional = (Directional) block.getBlockData();
                directional.setFacing(direction);
                block.setBlockData(directional);
            }
        }
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
            location.getBlock().setType(Material.AIR);
        }
    }
}
