package me.loqtm.loqor.storage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.Set;

public interface StorageHandler {
    Inventory loadLocker(OfflinePlayer player, int lockerNumber);
    void saveLocker(OfflinePlayer player, int lockerNumber, Inventory inventory);
    boolean deleteLocker(OfflinePlayer player, int lockerNumber);

    // NEW: which locker numbers currently exist in storage
    default Set<Integer> getStoredLockerNumbers(OfflinePlayer player) {
        return Collections.emptySet();
    }

    // Shortcuts
    default Inventory loadLocker(Player player, int lockerNumber) {
        return loadLocker((OfflinePlayer) player, lockerNumber);
    }
    default void saveLocker(Player player, int lockerNumber, Inventory inventory) {
        saveLocker((OfflinePlayer) player, lockerNumber, inventory);
    }
}
