package me.loqtm.loqor.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import me.loqtm.loqor.LoqorMain;
import me.loqtm.loqor.GUI.VaultGUI;
import me.loqtm.loqor.features.LoreInfuser;
import me.loqtm.loqor.util.LockerSession;

public class LockerListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            VaultGUI.handleClick(player, event);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        String title = event.getView().getTitle();
        if (!title.startsWith("Locker #")) return;

        // Prefer session, but fall back to the actual open inventory
        LockerSession session = VaultGUI.getOpenSession(player.getUniqueId());
        Inventory targetInv = (session != null) ? session.getInventory() : event.getInventory();
        me.loqtm.loqor.features.PersonalityManager.onOpen(player, targetInv);
    }

    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player) {
            UUID viewer = player.getUniqueId();
            LockerSession session = VaultGUI.getOpenSession(viewer);
            if (session != null) {
                // 1) Apply lore BEFORE saving
                LoreInfuser.maybeInfuse(session.getInventory());  // <-- add this

                // 2) Save to the actual owner's file
                OfflinePlayer owner = Bukkit.getOfflinePlayer(session.getOwnerId());
                LoqorMain.getStorage().saveLocker(owner, session.getLockerNumber(), session.getInventory());
                VaultGUI.removeSession(viewer);
            }
        }
    }
}
