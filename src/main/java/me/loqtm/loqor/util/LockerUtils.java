package me.loqtm.loqor.util;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class LockerUtils {

	public static int getMaxLockers(Player player) {
	    int max = 0;
	    for (PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
	        String permName = perm.getPermission();
	        if (permName.startsWith("loqor.lockers.")) {
	            try {
	                int value = Integer.parseInt(permName.substring("loqor.lockers.".length()));
	                if (value > max) max = value;
	            } catch (NumberFormatException ignored) {}
	        }
	    }
	    return max;
	}
	
}
