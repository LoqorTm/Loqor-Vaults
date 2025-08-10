package me.loqtm.loqor.commands;

import me.loqtm.loqor.GUI.VaultGUI;
import me.loqtm.loqor.LoqorMain;
import me.loqtm.loqor.storage.StorageHandler;
import me.loqtm.loqor.util.LockerSession;
import me.loqtm.loqor.util.LockerUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class LockerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        StorageHandler storage = LoqorMain.getStorage();

        // /locker
        if (args.length == 0) {
            VaultGUI.openLockerGUI(player, 1);
            return true;
        }

        // /locker <number>
        if (args.length == 1) {
            try {
                int lockerNumber = Integer.parseInt(args[0]);
                int max = LockerUtils.getMaxLockers(player);
                if (player.hasPermission("loqor.admin") || lockerNumber <= max) {
                    VaultGUI.openLocker(player, lockerNumber);
                } else {
                    player.sendMessage("§cYou don't have access to locker " + lockerNumber + ".");
                }
            } catch (NumberFormatException e) {
                player.sendMessage("§cUsage: /locker <number> [player]");
            }
            return true;
        }

        // /locker <number> <player>
        if (args.length == 2) {
            if (!player.hasPermission("loqor.admin")) {
                player.sendMessage("§cYou do not have permission to open other players' lockers.");
                return true;
            }

            try {
                int lockerNumber = Integer.parseInt(args[0]);
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (!target.hasPlayedBefore() && !target.isOnline()) {
                    player.sendMessage("§cPlayer '" + args[1] + "' not found.");
                    return true;
                }

                Inventory inv = storage.loadLocker(target, lockerNumber);

                // Track it for saving on close
                VaultGUI.setAdminViewing(player.getUniqueId(), new LockerSession(lockerNumber, inv, target.getUniqueId()));
                
                player.openInventory(inv);

            } catch (NumberFormatException e) {
                player.sendMessage("§cUsage: /locker <number> <player>");
            }
            return true;
        }

        // Fallback
        player.sendMessage("§cUsage: /locker <number> [player]");
        return true;
    }
}
