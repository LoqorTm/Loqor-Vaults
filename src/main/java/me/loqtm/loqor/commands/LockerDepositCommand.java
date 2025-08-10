package me.loqtm.loqor.commands;

import me.loqtm.loqor.LoqorMain;
import me.loqtm.loqor.storage.StorageHandler;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LockerDepositCommand implements CommandExecutor {
    private final StorageHandler storage = LoqorMain.getStorage();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this.");
            return true;
        }

        int lockerNumber = 1;
        OfflinePlayer target = player;

        if (args.length >= 1) {
            try {
                lockerNumber = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage("§cUsage: /lockerdeposit [locker] [player]");
                return true;
            }
        }

        if (args.length >= 2) {
            if (!player.hasPermission("loqor.admin")) {
                player.sendMessage("§cYou do not have permission to deposit to other players' lockers.");
                return true;
            }
            target = Bukkit.getOfflinePlayer(args[1]);
            if (!target.hasPlayedBefore() && !target.isOnline()) {
                player.sendMessage("§cPlayer '" + args[1] + "' not found.");
                return true;
            }
        }

        ItemStack inHand = player.getInventory().getItemInMainHand();
        if (inHand == null || inHand.getType().isAir() || inHand.getAmount() <= 0) {
            player.sendMessage("§eHold the item you want to deposit in your main hand.");
            return true;
        }

        Inventory locker = storage.loadLocker(target, lockerNumber);

        // Try to add; check if it fits fully
        ItemStack toAdd = inHand.clone();
        HashMap<Integer, ItemStack> leftover = locker.addItem(toAdd);

        boolean fullyAdded = leftover.isEmpty();
        if (!fullyAdded) {
            // Revert any partial add
            locker.removeItem(toAdd);
            player.sendMessage("§cLocker #" + lockerNumber + " is full.");
            return true;
        }

        // Remove from hand and save
        player.getInventory().setItemInMainHand(null);
        storage.saveLocker(target, lockerNumber, locker);

        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage("§aDeposited into Locker #" + lockerNumber + "!");
        } else {
            player.sendMessage("§aDeposited into " + target.getName() + "'s Locker #" + lockerNumber + "!");
        }
        return true;
    }
}
