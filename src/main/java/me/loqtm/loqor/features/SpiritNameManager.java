package me.loqtm.loqor.features;

import me.loqtm.loqor.LoqorMain;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class SpiritNameManager {
    private static final Random RNG = new Random();
    private static YamlConfiguration data;
    private static File file;

    private SpiritNameManager() {}
    
    public static synchronized void init(File dataFolder) {
        file = new File(dataFolder, "spiritnames.yml");
        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException ignored) {}
        data = YamlConfiguration.loadConfiguration(file);
    }
    
    public static synchronized void reload(File dataFolder) {
        init(dataFolder);
    }
    
    private static void ensure() {
        if (data == null || file == null) {
            init(LoqorMain.getInstance().getDataFolder());
        }
    }

    public static synchronized String getName(UUID playerId) {
        ensure(); // <- prevents NPE
        String key = playerId.toString();
        String existing = data.getString(key);
        if (existing != null) return existing;

        List<String> pool = LoqorMain.getInstance().getConfig().getStringList("personality.spirit_names");
        String name = pool.isEmpty() ? "Vault Spirit" : pool.get(RNG.nextInt(pool.size()));

        data.set(key, name);
        try { data.save(file); } catch (IOException ignored) {}
        return name;
    }
    
    private static void save() {
        try { data.save(file); } catch (IOException e) { e.printStackTrace(); }
    }
}
