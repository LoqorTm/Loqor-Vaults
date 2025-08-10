package me.loqtm.loqor.commands;

import me.loqtm.loqor.LoqorMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LoqorCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§eLoqor §7- Available subcommands: reload");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("loqor.reload")) {
                sender.sendMessage("§cYou do not have permission to reload Loqor.");
                return true;
            }
            try {
                LoqorMain.getInstance().reloadLoqor();
                sender.sendMessage("§aLoqor reloaded: config, storage, and signs.");
            } catch (Exception e) {
                sender.sendMessage("§cReload failed. Check console for details.");
                e.printStackTrace();
            }
            return true;
        }

        sender.sendMessage("§cUnknown subcommand. Try: /loqor reload");
        return true;
    }
}
