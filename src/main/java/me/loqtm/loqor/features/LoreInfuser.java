package me.loqtm.loqor.features;

import me.loqtm.loqor.LoqorMain;
import me.loqtm.loqor.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public final class LoreInfuser {
    private static final Random RNG = new Random();

    private LoreInfuser() {}

    public static void maybeInfuse(Inventory inv) {
        FileConfiguration cfg = LoqorMain.getInstance().getConfig();
        if (!cfg.getBoolean("lore.enabled", true)) return;

        final boolean oncePerItem = cfg.getBoolean("lore.once_per_item", true); // NEW (default true)
        final int chance = cfg.getInt("lore.chance_percent", 100);              // optional (default 100)

        List<String> triggers = lowerList(cfg.getStringList("lore.trigger_names"));
        List<String> pool = cfg.getStringList("lore.line_pool");
        if (triggers.isEmpty() || pool.isEmpty()) return;

        final NamespacedKey infusedKey = new NamespacedKey(LoqorMain.getInstance(), "lore_infused");

        for (ItemStack it : inv.getContents()) {
            if (ItemUtils.isAirOrNull(it)) continue;

            ItemMeta meta = it.getItemMeta();
            if (meta == null) continue;

            // 1) Check our marker (skip if already infused)
            if (oncePerItem) {
                PersistentDataContainer pdc = meta.getPersistentDataContainer();
                if (pdc.has(infusedKey, PersistentDataType.BYTE)) continue;
            }

            // 2) Match triggers (case-insensitive, no colors)
            String display = meta.hasDisplayName() ? meta.getDisplayName() : "";
            String plainName = Optional.ofNullable(ChatColor.stripColor(display)).orElse("").toLowerCase(Locale.ROOT);

            boolean matches = false;
            for (String t : triggers) {
                if (plainName.contains(t)) { matches = true; break; }
            }
            if (!matches) continue;

            // 3) Chance gate (optional)
            if (chance < 100 && RNG.nextInt(100) >= chance) continue;

            // 4) Pick a line and avoid duplicates
            String line = pool.get(RNG.nextInt(pool.size())).replace("&", "§");
            if (meta.hasLore()) {
                List<String> existing = meta.getLore();
                String linePlain = ChatColor.stripColor(line);
                boolean dup = false;
                for (String l : existing) {
                    if (Objects.equals(ChatColor.stripColor(l), linePlain)) { dup = true; break; }
                }
                if (dup) continue; // exact same line already present
                List<String> newLore = new ArrayList<>(existing);
                newLore.add(line);
                meta.setLore(newLore);
            } else {
                meta.setLore(Collections.singletonList(line));
            }

            // 5) Mark as infused
            if (oncePerItem) {
                meta.getPersistentDataContainer().set(infusedKey, PersistentDataType.BYTE, (byte)1);
            }

            it.setItemMeta(meta);
        }
    }

    private static List<String> lowerList(List<String> input) {
        List<String> out = new ArrayList<>();
        for (String s : input) out.add(s.toLowerCase(Locale.ROOT));
        return out;
    }
}
