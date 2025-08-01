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
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.reallyworld.rwmoonjourney.RWMoonJourney.plugin;

public class Config extends ConfigBase {
    private static final String DEFAULT_STRING = "NOT_FOUND_VALUE";
    private static final Random random = new Random();

    protected static File configFile;
    protected static FileConfiguration config;

    public static String getString(String path) {
        String value = config.getString(path);
        return value != null ? value : DEFAULT_STRING;
    }

    public static String getString(String path, String defaultValue) {
        if (config.isSet(path)) {
            String value = config.getString(path);
            return value != null ? value : defaultValue;
        }
        return defaultValue;
    }

    public static Component getText(String path) {
        String value = config.getString(path);
        return Component.text(value != null ? value : DEFAULT_STRING);
    }

    public static Component getText(String path, String defaultValue) {
        String value = config.getString(path);
        return Component.text(value != null ? value : defaultValue);
    }

    public static int getInt(String path) {
        return config.getInt(path);
    }

    public static int getInt(String path, int defaultValue) {
        if (config.isSet(path)) {
            return config.getInt(path);
        }
        return defaultValue;
    }

    public static int intRange(String path) throws BadConfigValueException {
        if (!config.isSet(path)) {
            throw new BadConfigValueException(path);
        }

        String value = getString(path);
        String[] parts = value.split("~");
        int value1 = Integer.parseInt(parts[0]);
        int value2 = Integer.parseInt(parts[1]);

        return random.nextInt(value2 - value1) + value1;
    }

    public static double getDouble(String path) {
        return config.getDouble(path);
    }

    public static double getDouble(String path, double defaultValue) {
        return config.getDouble(path, defaultValue);
    }

    public static List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public static List<String> getStringList(String path, List<String> defaultValue) {
        if (!config.isSet(path)) {
            return defaultValue;
        }
        return config.getStringList(path);
    }

    public static boolean getBoolean(String path) throws BadConfigValueException {
        if (!config.isSet(path)) {
            throw new BadConfigValueException(path);
        }
        return config.getBoolean(path);
    }

    public static boolean getBoolean(String path, boolean defaultValue) {
        return config.getBoolean(path, defaultValue);
    }

    public static long getLong(String path) {
        return config.getLong(path);
    }

    public static long getLong(String path, long defaultValue) {
        return config.getLong(path, defaultValue);
    }

    public static ConfigurationSection getSection(String path) {
        return config.getConfigurationSection(path);
    }

    public static Set<String> getRootKeys() {
        return config.getKeys(false);
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
}
