package me.loqtm.loqor.util;

import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class LockerSession {
    private final int lockerNumber;
    private final Inventory inventory;
    private final UUID ownerId; // NEW: owner of the locker

    public LockerSession(int lockerNumber, Inventory inventory, UUID ownerId) {
        this.lockerNumber = lockerNumber;
        this.inventory = inventory;
        this.ownerId = ownerId;
    }

    public int getLockerNumber() {
        return lockerNumber;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public UUID getOwnerId() {
        return ownerId;
    }
}
