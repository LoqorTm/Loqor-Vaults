package me.loqtm.loqor.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public final class ItemUtils {
    private static final Set<Material> JUNK = EnumSet.of(
            Material.ROTTEN_FLESH, Material.DIRT, Material.GRANITE, Material.DIORITE,
            Material.ANDESITE, Material.GRAVEL, Material.SAND, Material.OAK_SAPLING,
            Material.STRING, Material.SPIDER_EYE, Material.POISONOUS_POTATO
    );

    private ItemUtils() {}

    public static boolean isAirOrNull(ItemStack it) {
        return it == null || it.getType().isAir() || it.getAmount() <= 0;
    }

    public static boolean isJunk(ItemStack it) {
        if (isAirOrNull(it)) return false;
        return JUNK.contains(it.getType());
    }

    public static void appendLoreLine(ItemStack it, String line) {
        if (isAirOrNull(it)) return;
        ItemMeta meta = it.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        lore.add(line.replace("&", "§"));
        meta.setLore(lore);
        it.setItemMeta(meta);
    }

    public static boolean displayNameContains(ItemStack it, String needle) {
        if (isAirOrNull(it)) return false;
        ItemMeta meta = it.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            String plain = ChatColor.stripColor(meta.getDisplayName());
            if (plain == null) return false;
            return plain.toLowerCase(Locale.ROOT).contains(needle.toLowerCase(Locale.ROOT));
        }
        return false;
    }
}