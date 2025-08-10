package me.loqtm.loqor.signs;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignBindingManager {
    private static final Map<UUID, SignBinding> pendingBindings = new HashMap<>();

    public static void set(UUID playerId, SignBinding binding) {
        pendingBindings.put(playerId, binding);
    }

    public static SignBinding remove(UUID playerId) {
        return pendingBindings.remove(playerId);
    }
}
