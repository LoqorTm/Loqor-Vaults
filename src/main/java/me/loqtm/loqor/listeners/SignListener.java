package me.loqtm.loqor.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.loqtm.loqor.LoqorMain;
import me.loqtm.loqor.signs.SignBinding;
import me.loqtm.loqor.signs.SignBindingManager;
import me.loqtm.loqor.signs.SignStorage;

public class SignListener implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
	    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
	    Block block = event.getClickedBlock();
	    if (!(block.getState() instanceof Sign sign)) return;

	    Player player = event.getPlayer();
	    SignBinding binding = SignBindingManager.remove(player.getUniqueId());
	    SignStorage signStorage = LoqorMain.getSignStorage();

	    if (binding != null) {
	        signStorage.bindSign(sign.getLocation(), binding.owner(), binding.lockerNumber());
	        sign.setLine(0, "[Locker]");
	        sign.setLine(1, "Locker #" + binding.lockerNumber());
	        sign.setLine(2, Bukkit.getOfflinePlayer(binding.owner()).getName());
	        sign.setLine(3, "Click to open");
	        sign.update();
	        player.sendMessage("§aSign successfully bound.");
	        event.setCancelled(true);
	    } else {
	        Integer locker = signStorage.getLocker(sign.getLocation());
	        UUID owner = signStorage.getOwner(sign.getLocation());

	        if (locker != null && owner != null) {
	            if (player.hasPermission("loqor.signs.use") || player.hasPermission("loqor.signs.bypass")) {
	                player.openInventory(LoqorMain.getStorage().loadLocker(Bukkit.getOfflinePlayer(owner), locker));
	            } else {
	                player.sendMessage("§cYou don't have permission to use this locker sign.");
	            }
	            event.setCancelled(true);
	        }
	    }
	}

	@EventHandler
	public void onSignBreak(BlockBreakEvent event) {
	    Block block = event.getBlock();
	    if (!(block.getState() instanceof Sign sign)) return;

	    Location loc = sign.getLocation();
	    SignStorage signStorage = LoqorMain.getSignStorage();

	    if (signStorage.getLocker(loc) != null) {
	        signStorage.unbindSign(loc);
	        event.getPlayer().sendMessage("§eLocker sign unbound.");
	    }
	}
}
