package me.loqtm.loqor.signs;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class SignStorage {
    private final File file;
    private FileConfiguration config; // was final

    public SignStorage(File pluginFolder) {
        this.file = new File(pluginFolder, "signs.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    // NEW
    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try { config.save(file); } catch (IOException e) { e.printStackTrace(); }
    }

    public void bindSign(Location loc, UUID owner, int locker) {
        String path = format(loc);
        config.set("signs." + path + ".owner", owner.toString());
        config.set("signs." + path + ".locker", locker);
        save();
    }

    public void unbindSign(Location loc) {
        String path = format(loc);
        config.set("signs." + path, null);
        save();
    }

    public Integer getLocker(Location loc) {
        String path = format(loc);
        return config.contains("signs." + path) ? config.getInt("signs." + path + ".locker") : null;
    }

    public UUID getOwner(Location loc) {
        String path = format(loc);
        return config.contains("signs." + path) ? UUID.fromString(config.getString("signs." + path + ".owner")) : null;
    }

    private String format(Location loc) {
        return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }
}
