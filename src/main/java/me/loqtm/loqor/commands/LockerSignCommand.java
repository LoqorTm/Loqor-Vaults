package me.loqtm.loqor.commands;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.loqtm.loqor.LoqorMain;
import me.loqtm.loqor.signs.SignBinding;
import me.loqtm.loqor.signs.SignBindingManager;
import me.loqtm.loqor.signs.SignStorage;

public class LockerSignCommand implements CommandExecutor {
	// Removed local map, using SignBindingManager
    private final SignStorage signStorage = LoqorMain.getSignStorage();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (!player.hasPermission("loqor.signs.set")) {
            player.sendMessage("§cYou don't have permission to set locker signs.");
            return true;
        }

        try {
            int locker = Integer.parseInt(args[0]);
            UUID target = player.getUniqueId();

            if (args.length == 2 && player.hasPermission("loqor.admin")) {
                target = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
            }

            SignBindingManager.set(player.getUniqueId(), new SignBinding(target, locker));
            player.sendMessage("§aRight-click a sign to bind it to locker #" + locker);
        } catch (Exception e) {
            player.sendMessage("§cUsage: /lockersign <locker> [player]");
        }
        return true;
    }
}
