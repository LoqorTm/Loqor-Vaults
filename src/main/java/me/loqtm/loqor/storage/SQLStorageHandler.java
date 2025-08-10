package me.loqtm.loqor.storage;

import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;

public class SQLStorageHandler implements StorageHandler {

    @Override
    public Inventory loadLocker(OfflinePlayer player, int lockerNumber) {
        throw new UnsupportedOperationException("Database support not yet implemented.");
    }

    @Override
    public boolean deleteLocker(OfflinePlayer player, int lockerNumber) {
        throw new UnsupportedOperationException("Database locker deletion not implemented.");
    }
    
    @Override
    public Set<Integer> getStoredLockerNumbers(OfflinePlayer player) {
        throw new UnsupportedOperationException("Database search not yet implemented.");
    }
    
    @Override
    public void saveLocker(OfflinePlayer player, int lockerNumber, Inventory inventory) {
        throw new UnsupportedOperationException("Database support not yet implemented.");
    }
}

