package me.loqtm.loqor.commands;

import me.loqtm.loqor.LoqorMain;
import me.loqtm.loqor.storage.StorageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class LockerFindCommand implements CommandExecutor {
    private final StorageHandler storage = LoqorMain.getStorage();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cUsage: /lockerfind <item> [player]");
            return true;
        }

        String query = String.join(" ", Arrays.copyOfRange(args, 0, 1)).toLowerCase(Locale.ROOT);
        OfflinePlayer target = player;

        if (args.length >= 2) {
            if (!player.hasPermission("loqor.admin")) {
                player.sendMessage("§cYou do not have permission to search other players' lockers.");
                return true;
            }
            target = Bukkit.getOfflinePlayer(args[1]);
            if (!target.hasPlayedBefore() && !target.isOnline()) {
                player.sendMessage("§cPlayer '" + args[1] + "' not found.");
                return true;
            }
        }

        Set<Integer> lockers = storage.getStoredLockerNumbers(target);
        if (lockers.isEmpty()) {
            player.sendMessage("§eNo saved lockers found for " + (target == player ? "you." : target.getName() + "."));
            return true;
        }

        int hits = 0;
        for (int lockerNumber : new TreeSet<>(lockers)) {
            Inventory inv = storage.loadLocker(target, lockerNumber);
            List<Integer> slots = new ArrayList<>();

            ItemStack[] contents = inv.getContents();
            for (int i = 0; i < contents.length; i++) {
                ItemStack it = contents[i];
                if (it == null || it.getType() == Material.AIR) continue;

                Material mat = it.getType();
                String matName = mat.name().toLowerCase(Locale.ROOT).replace('_', ' ');

                boolean match = matName.contains(query);
                if (!match) {
                    ItemMeta meta = it.getItemMeta();
                    if (meta != null && meta.hasDisplayName()) {
                        String dn = meta.getDisplayName().toLowerCase(Locale.ROOT);
                        match = dn.contains(query);
                    }
                }
                if (match) slots.add(i);
            }

            if (!slots.isEmpty()) {
                hits += slots.size();
                player.sendMessage("§aFound in Locker #" + lockerNumber + " §7slots: §f" + slots);
            }
        }

        if (hits == 0) {
            player.sendMessage("§eNo items matching §f\"" + query + "\" §e… maybe you left it in your other pants?");
        } else {
            player.sendMessage("§aTotal matches: §f" + hits);
        }
        return true;
    }
}
