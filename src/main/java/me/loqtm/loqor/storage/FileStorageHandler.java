package me.loqtm.loqor.storage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class FileStorageHandler implements StorageHandler {

    private final File dataFolder;

    public FileStorageHandler(File pluginFolder) {
        this.dataFolder = new File(pluginFolder, "playerdata");
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    @Override
    public Set<Integer> getStoredLockerNumbers(OfflinePlayer player) {
        File file = new File(dataFolder, player.getUniqueId() + ".yml");
        if (!file.exists()) return new HashSet<>();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        Set<Integer> result = new HashSet<>();
        ConfigurationSection lockers = config.getConfigurationSection("lockers");
        if (lockers == null) return result;

        for (String key : lockers.getKeys(false)) {
            try {
                result.add(Integer.parseInt(key));
            } catch (NumberFormatException ignored) {}
        }
        return result;
    }

    
    @Override
    public Inventory loadLocker(OfflinePlayer player, int lockerNumber) {
        File file = new File(dataFolder, player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        Inventory inventory = Bukkit.createInventory(null, 54, "Locker #" + lockerNumber);

        String path = "lockers." + lockerNumber;
        if (config.contains(path)) {
            List<?> items = config.getList(path);
            if (items != null) {
                ItemStack[] contents = items.toArray(new ItemStack[0]);
                inventory.setContents(contents);
            }
        }

        return inventory;
    }
    
    public boolean deleteLocker(OfflinePlayer player, int lockerNumber) {
        File file = new File(dataFolder, player.getUniqueId() + ".yml");
        if (!file.exists()) return false;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String path = "lockers." + lockerNumber;

        if (!config.contains(path)) return false;

        config.set(path, null);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void saveLocker(OfflinePlayer player, int lockerNumber, Inventory inventory) {
        File file = new File(dataFolder, player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("lockers." + lockerNumber, Arrays.asList(inventory.getContents()));
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
