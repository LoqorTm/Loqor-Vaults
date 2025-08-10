package me.loqtm.loqor.commands;

import me.loqtm.loqor.LoqorMain;
import me.loqtm.loqor.storage.StorageHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class LockerDeleteCommand implements CommandExecutor {
    private final StorageHandler storage = LoqorMain.getStorage();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this.");
            return true;
        }

        if (args.length == 1) {
            try {
                int lockerNumber = Integer.parseInt(args[0]);
                boolean success = storage.deleteLocker(player, lockerNumber);
                player.sendMessage(success ? "§aLocker #" + lockerNumber + " deleted successfully." : "§cLocker #" + lockerNumber + " does not exist.");
            } catch (NumberFormatException e) {
                player.sendMessage("§cUsage: /lockerdel <number>");
            }
            return true;
        }

        if (args.length == 2) {
            if (!player.hasPermission("loqor.admin") && !player.hasPermission("loqor.delete")) {
                player.sendMessage("§cYou do not have permission to delete other players' lockers.");
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            if (!target.hasPlayedBefore() && !target.isOnline()) {
                player.sendMessage("§cPlayer '" + args[0] + "' not found.");
                return true;
            }

            try {
                int lockerNumber = Integer.parseInt(args[1]);
                boolean success = storage.deleteLocker(target, lockerNumber);
                player.sendMessage(success ? "§aDeleted locker #" + lockerNumber + " of " + target.getName() : "§cLocker #" + lockerNumber + " not found for " + target.getName());
            } catch (NumberFormatException e) {
                player.sendMessage("§cUsage: /lockerdel <player> <number>");
            }
            return true;
        }

        player.sendMessage("§cUsage: /lockerdel <number> [player]");
        return true;
    }
}
