package me.loqtm.loqor.features;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.loqtm.loqor.LoqorMain;
import me.loqtm.loqor.util.ItemUtils;

public final class PersonalityManager {
    private static final Random RNG = new Random();

    private PersonalityManager() {}

    public static boolean enabled() {
        return LoqorMain.getInstance().getConfig().getBoolean("personality.enabled", true);
    }

    public static void onOpen(Player player, Inventory lockerInv) {
        if (!enabled()) return;

        
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 0.6f, 1.2f);
        FileConfiguration cfg = LoqorMain.getInstance().getConfig();

        // 1) Vault spirit message (based on “mood”)
        String mood = computeMood(lockerInv);
        sendRandomMessage(player, "personality.messages.greeting");
        sendRandomMessage(player, "personality.messages." + mood);

        // 2) Greedy vault chance
        int chance = cfg.getInt("personality.greedy_chance_percent", 10);
        if (chance > 0 && RNG.nextInt(100) < chance) {
            stealOneJunkItem(player, lockerInv);
        }
    }

    private static String computeMood(Inventory inv) {
        ItemStack[] arr = inv.getContents();
        int filled = 0;
        int nonAir = 0;

        for (ItemStack it : arr) {
            if (!ItemUtils.isAirOrNull(it)) {
                nonAir++;
                if (it.getAmount() >= it.getMaxStackSize()) filled++;
            }
        }
        int size = arr.length;
        double fullness = nonAir / (double) size;

        if (fullness >= 0.95 || filled >= size * 0.5) return "hoarder";
        if (fullness <= 0.10) return "minimalist";

        // crude tidy/messy: if many different stacks of same material absent => messy
        // we’ll sample: if > 25% stacks are singletons, call it messy
        int singletons = 0;
        for (ItemStack it : arr) {
            if (!ItemUtils.isAirOrNull(it) && it.getAmount() == 1) singletons++;
        }
        return (singletons > size * 0.25) ? "messy" : "tidy";
    }

    private static void sendRandomMessage(Player player, String path) {
        List<String> list = LoqorMain.getInstance().getConfig().getStringList(path);
        if (list == null || list.isEmpty()) return;
        String spiritName = me.loqtm.loqor.features.SpiritNameManager.getName(player.getUniqueId());
        String msg = list.get(RNG.nextInt(list.size()))
            .replace("%player%", player.getName())
            .replace("%spirit%", spiritName);
        player.sendMessage(msg.replace("&", "§"));
    }

    private static void stealOneJunkItem(Player player, Inventory lockerInv) {
        // find one junk item in player inv
        ItemStack[] playerContents = player.getInventory().getContents();
        for (int i = 0; i < playerContents.length; i++) {
            ItemStack it = playerContents[i];
            if (ItemUtils.isJunk(it)) {
                // Try to add to locker
                Map<Integer, ItemStack> leftover = lockerInv.addItem(it.clone());
                if (leftover.isEmpty()) {
                    playerContents[i] = null; // remove from player
                    player.getInventory().setContents(playerContents);

                    // NEW: send random greedy message
                    String itemName = it.getAmount() + "x " + it.getType().name().toLowerCase().replace('_', ' ');

                 // Fetch all greedy messages from config
                 List<String> greedyLines = LoqorMain.getInstance().getConfig().getStringList("personality.messages.greedy");
                 if (!greedyLines.isEmpty()) {
                     String line = greedyLines.get(RNG.nextInt(greedyLines.size()));
                     line = line.replace("%item%", itemName);
                     player.sendMessage(line.replace("&", "§"));
                 }

                }
                return; // only try one item
            }
        }
    }
}
