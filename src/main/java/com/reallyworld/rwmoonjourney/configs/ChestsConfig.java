package com.reallyworld.rwmoonjourney.configs;

import com.reallyworld.rwmoonjourney.api.config.ConfigBase;
import com.reallyworld.rwmoonjourney.models.ChestInfoModel;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.reallyworld.rwmoonjourney.RWMoonJourney.plugin;

public class ChestsConfig extends ConfigBase {
    public static String CHESTS_KEY = "chests";

    protected static File configFile;
    protected static FileConfiguration config;

    public static void addLocation(Location location, String rarity) {
        List<Map<?, ?>> locationList = config.getMapList(CHESTS_KEY);

        Map<String, Object> locationData = new HashMap<>();
        locationData.put("world", location.getWorld().getName());
        locationData.put("x", location.getX());
        locationData.put("y", location.getY());
        locationData.put("z", location.getZ());
        locationData.put("yaw", location.getYaw());
        locationData.put("pitch", location.getPitch());
        locationData.put("rarity", rarity);
        locationData.put("cost", rarityToCost(rarity));


        locationList.add(locationData);
        config.set(CHESTS_KEY, locationList);
        save();
    }

    public static List<ChestInfoModel> getAllChests(){
        var models = new ArrayList<ChestInfoModel>();
        var modelsList = config.getMapList(CHESTS_KEY);

        for (var data : modelsList) {
            var model = mapToChestIntoModel(data);
            if (model != null) models.add(model);
        }
        return models;
    }

    public static int getCount(){
        return config.getMapList(CHESTS_KEY).size();
    }

    public static List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();
        List<Map<?, ?>> locationList = config.getMapList(CHESTS_KEY);

        for (Map<?, ?> data : locationList) {
            Location loc = mapToLocation(data);
            if (loc != null) locations.add(loc);
        }
        return locations;
    }

    public static List<Location> getLocationsByRarity(String rarity) {
        List<Location> locations = new ArrayList<>();
        List<Map<?, ?>> locationList = config.getMapList(CHESTS_KEY);

        for (Map<?, ?> data : locationList) {
            String locRarity = (String) data.get("rarity");
            if (rarity.equalsIgnoreCase(locRarity)) {
                Location loc = mapToLocation(data);
                if (loc != null) locations.add(loc);
            }
        }
        return locations;
    }

    public static List<String> getAllRarities() {
        List<String> rarities = new ArrayList<>();
        List<Map<?, ?>> locationList = config.getMapList(CHESTS_KEY);

        for (Map<?, ?> data : locationList) {
            String rarity = (String) data.get("rarity");
            if (rarity != null && !rarities.contains(rarity)) {
                rarities.add(rarity);
            }
        }
        return rarities;
    }

    public static void clearAllLocations() {
        config.set(CHESTS_KEY, new ArrayList<>());
        save();
    }

    public static int getLocationCount() {
        return config.getMapList(CHESTS_KEY).size();
    }

    public static int getLocationCountByRarity(String rarity) {
        List<Map<?, ?>> locationList = config.getMapList(CHESTS_KEY);
        int count = 0;

        for (Map<?, ?> data : locationList) {
            String locRarity = (String) data.get("rarity");
            if (rarity.equalsIgnoreCase(locRarity)) {
                count++;
            }
        }
        return count;
    }

    private static ChestInfoModel mapToChestIntoModel(Map<?, ?> data){
        try {
            String worldName = (String) data.get("world");
            double x = ((Number) data.get("x")).doubleValue();
            double y = ((Number) data.get("y")).doubleValue();
            double z = ((Number) data.get("z")).doubleValue();
            float yaw = ((Number) data.get("yaw")).floatValue();
            float pitch = ((Number) data.get("pitch")).floatValue();
            var location = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
            String rarity = (String) data.get("rarity");
            int cost = (Integer) data.get("cost");

            return new ChestInfoModel(rarity, cost, location);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to parse location data: " + e.getMessage());
            return null;
        }
    }

    private static Location mapToLocation(Map<?, ?> data) {
        try {
            String worldName = (String) data.get("world");
            double x = ((Number) data.get("x")).doubleValue();
            double y = ((Number) data.get("y")).doubleValue();
            double z = ((Number) data.get("z")).doubleValue();
            float yaw = ((Number) data.get("yaw")).floatValue();
            float pitch = ((Number) data.get("pitch")).floatValue();

            return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to parse location data: " + e.getMessage());
            return null;
        }
    }

    public static void save(){
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save locations.yml!");
            e.printStackTrace();
        }
    }

    public static void init(Plugin plugin, String path) {
        configFile = new File(plugin.getDataFolder(), path);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(path, false);
        }
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает цифровую редкость сундука по строковой. Данные берем из конфига.
     * @param rarity строковая редкость
     * @return цифровая редкость
     */
    private static int rarityToCost(String rarity){
        var rarityCosts = Config.getStringList("chest-rarity-list");
        for(var str: rarityCosts){
            var splitted = str.split(":");

            if(splitted.length < 2)
                return 0;

            try {
                if(splitted[0].equals(rarity))
                    return Integer.parseInt(splitted[1]);
            }
            catch (Exception ex){}
        }

        return 0;
    }
}
