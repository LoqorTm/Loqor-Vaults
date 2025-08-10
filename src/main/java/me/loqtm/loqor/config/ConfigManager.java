package me.loqtm.loqor.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public final class ConfigManager {
    public static final String VERSION_KEY = "config-version";

    private final JavaPlugin plugin;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /** Merge defaults into the live config, bump version if bundled default is newer, then reload. */
    public void ensureUpToDate() {
        // Make sure a config file exists on disk
        plugin.saveDefaultConfig();

        // Load current from disk
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        YamlConfiguration current = YamlConfiguration.loadConfiguration(configFile);

        // Load defaults from jar
        YamlConfiguration defaults = loadDefaults();
        if (defaults == null) {
            plugin.getLogger().warning("No default config.yml found in the JAR. Skipping config update.");
            return;
        }

        String currentVer = current.getString(VERSION_KEY, "0");
        String bundledVer = defaults.getString(VERSION_KEY, "0");

        // Recursively copy any missing keys from defaults
        mergeMissing(defaults, current);

        // If versions differ, set to bundled (we treat bundled as the source of truth)
        if (!safeEquals(currentVer, bundledVer)) {
            current.set(VERSION_KEY, bundledVer);
            plugin.getLogger().info("Config updated: " + VERSION_KEY + " " + currentVer + " → " + bundledVer);
        }

        // Save and reload into plugin memory
        try {
            current.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save merged config.yml: " + e.getMessage());
            e.printStackTrace();
        }
        plugin.reloadConfig();
    }

    private YamlConfiguration loadDefaults() {
        try (InputStream in = plugin.getResource("config.yml")) {
            if (in == null) return null;
            InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(reader);
            // also set as defaults so get*Default works if needed
            return yml;
        } catch (IOException e) {
            plugin.getLogger().severe("Error reading default config.yml from JAR: " + e.getMessage());
            return null;
        }
    }

    /** Recursively copy keys from src into dst ONLY if missing in dst. */
    private void mergeMissing(ConfigurationSection src, ConfigurationSection dst) {
        Set<String> keys = src.getKeys(false);
        for (String key : keys) {
            Object srcVal = src.get(key);
            if (srcVal instanceof ConfigurationSection) {
                ConfigurationSection srcChild = (ConfigurationSection) srcVal;
                ConfigurationSection dstChild = dst.getConfigurationSection(key);
                if (dstChild == null) dstChild = dst.createSection(key);
                mergeMissing(srcChild, dstChild);
            } else {
                if (!dst.contains(key)) {
                    dst.set(key, srcVal);
                }
            }
        }
    }

    private boolean safeEquals(String a, String b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }
}
