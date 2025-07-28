package com.reallyworld.rwmoonjourney.configs;

import com.reallyworld.rwmoonjourney.api.config.BadConfigValueException;
import com.reallyworld.rwmoonjourney.api.config.ConfigBase;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class Messages extends ConfigBase {
    private static final String DEFAULT_STRING = "NOT_FOUND_VALUE";

    protected static File configFile;
    protected static FileConfiguration config;

    public static String getString(String path) {
        String value = config.getString(path);
        return value != null ? value : DEFAULT_STRING;
    }

    public static Component getText(String path) {
        String value = config.getString(path);
        return Component.text(value != null ? value : DEFAULT_STRING);
    }

    public static void init(Plugin plugin, String path) {
        configFile = new File(plugin.getDataFolder(), path);
        try {
            plugin.getLogger().info(String.valueOf(Files.readAllLines(configFile.toPath())));
        }
        catch (Exception ignored){}
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

    public static String message(String path){
        return getString(path);
    }

    public static Component text(String path){
        return getText(path);
    }

    public static Component noPerm(){
        return getText("no-permission");
    }
    public static Component badArgs(){
        return getText("bad-args");
    }
    public static Component playerOnly(){ return getText("player-only");}
}
