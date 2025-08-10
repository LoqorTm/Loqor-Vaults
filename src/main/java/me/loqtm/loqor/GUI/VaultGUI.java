package me.loqtm.loqor.GUI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.loqtm.loqor.LoqorMain;
import me.loqtm.loqor.util.LockerSession;
import me.loqtm.loqor.util.LockerUtils;

public class VaultGUI {

    private static final Map<UUID, Map<Integer, Inventory>> lockerStorage = new HashMap<>();
    private static final Map<UUID, LockerSession> openInventories = new HashMap<>();

    public static void openLockerGUI(Player player, int page) {
        int maxLockers = LockerUtils.getMaxLockers(player);
        int lockersPerPage = 9;
        int totalPages = (int) Math.ceil(maxLockers / (double) lockersPerPage);

        Inventory gui = Bukkit.createInventory(null, 18, "Your Lockers (Page " + page + ")");

        int startIndex = (page - 1) * lockersPerPage;
        for (int i = 0; i < lockersPerPage; i++) {
            int lockerNumber = startIndex + i + 1;
            if (lockerNumber > maxLockers) break;

            ItemStack chest = new ItemStack(Material.CHEST);
            ItemMeta meta = chest.getItemMeta();
            meta.setDisplayName("Locker #" + lockerNumber);
            chest.setItemMeta(meta);
            gui.setItem(i, chest);
        }

        // Navigation
        if (page > 1) {
            ItemStack back = new ItemStack(Material.ARROW);
            ItemMeta meta = back.getItemMeta();
            meta.setDisplayName("Previous Page");
            back.setItemMeta(meta);
            gui.setItem(9, back);
        }

        if (page < totalPages) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta meta = next.getItemMeta();
            meta.setDisplayName("Next Page");
            next.setItemMeta(meta);
            gui.setItem(17, next);
        }

        player.openInventory(gui);
    }

    public static void openLocker(Player player, int lockerNumber) {
        Inventory inv = LoqorMain.getStorage().loadLocker(player, lockerNumber);
        openInventories.put(player.getUniqueId(), new LockerSession(lockerNumber, inv, player.getUniqueId()));
        player.openInventory(inv);
    }


    public static LockerSession getOpenSession(UUID uuid) {
        return openInventories.get(uuid);
    }

    public static void removeSession(UUID uuid) {
        openInventories.remove(uuid);
    }
    
    public static void setAdminViewing(UUID viewerId, LockerSession session) {
        openInventories.put(viewerId, session);
    }

    public static void handleClick(Player player, InventoryClickEvent event) {
        String title = event.getView().getTitle();

        if (title.startsWith("Your Lockers")) {
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR) return;

            if (clicked.getType() == Material.CHEST && clicked.getItemMeta() != null) {
                String name = clicked.getItemMeta().getDisplayName();
                if (name.startsWith("Locker #")) {
                    int lockerNumber = Integer.parseInt(name.replace("Locker #", ""));
                    openLocker(player, lockerNumber);
                }
            } else if (clicked.getType() == Material.ARROW) {
                String name = clicked.getItemMeta().getDisplayName();
                int currentPage = Integer.parseInt(title.replaceAll("[^0-9]", ""));
                if (name.contains("Next")) {
                    openLockerGUI(player, currentPage + 1);
                } else if (name.contains("Previous")) {
                    openLockerGUI(player, currentPage - 1);
                }
            }
        }
    }
}
