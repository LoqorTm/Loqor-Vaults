package me.loqtm.loqor;

import org.bukkit.plugin.java.JavaPlugin;

import me.loqtm.loqor.commands.LockerCommand;
import me.loqtm.loqor.commands.LockerDeleteCommand;
import me.loqtm.loqor.commands.LockerDepositCommand;
import me.loqtm.loqor.commands.LockerFindCommand;
import me.loqtm.loqor.commands.LockerSignCommand;
import me.loqtm.loqor.commands.LoqorCommand;
import me.loqtm.loqor.config.ConfigManager;
import me.loqtm.loqor.features.SpiritNameManager;
import me.loqtm.loqor.listeners.LockerListener;
import me.loqtm.loqor.listeners.SignListener;
import me.loqtm.loqor.signs.SignStorage;
import me.loqtm.loqor.storage.FileStorageHandler;
import me.loqtm.loqor.storage.SQLStorageHandler;
import me.loqtm.loqor.storage.StorageHandler;

public class LoqorMain extends JavaPlugin {

    private static LoqorMain instance;
    private static StorageHandler storageHandler;
    private static SignStorage signStorage;
    private ConfigManager configManager;


@Override
public void onEnable() {
    instance = this;
    configManager = new ConfigManager(this);

    // Ensure config exists and is merged to latest
    configManager.ensureUpToDate();
    SpiritNameManager.init(getDataFolder());

    initStorageHandler();
    signStorage = new SignStorage(getDataFolder());

    getCommand("locker").setExecutor(new LockerCommand());
    getCommand("lockerdel").setExecutor(new LockerDeleteCommand());
    getCommand("lockersign").setExecutor(new LockerSignCommand());
    getCommand("lockerfind").setExecutor(new LockerFindCommand());
    getCommand("lockerdeposit").setExecutor(new LockerDepositCommand());
    getCommand("loqor").setExecutor(new LoqorCommand());

    getServer().getPluginManager().registerEvents(new LockerListener(), this);
    getServer().getPluginManager().registerEvents(new SignListener(), this);
}

    private void initStorageHandler() {
        boolean useDatabase = getConfig().getBoolean("use-database", false);
        storageHandler = useDatabase ? new SQLStorageHandler() : new FileStorageHandler(getDataFolder());
    }


    public void reloadLoqor() {
        reloadConfig();
        initStorageHandler();
        if (signStorage != null) signStorage.reload();

        // re-load spirit names file too (in case it changed on disk)
        SpiritNameManager.reload(getDataFolder());

        getLogger().info("Loqor reloaded.");
    }
    
    public static SignStorage getSignStorage() { return signStorage; }
    public static StorageHandler getStorage() { return storageHandler; }
    public static LoqorMain getInstance() { return instance; }

    @Override
    public void onDisable() {
        getLogger().info("Loqor disabled.");
    }
}
