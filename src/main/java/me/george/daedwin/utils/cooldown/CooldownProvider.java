package me.george.daedwin.utils.cooldown;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class CooldownProvider extends CachedClientProvider<Cooldown> {

    @Override
    protected Cooldown cache(OfflinePlayer player, Object... params) {
        return getCache().put(player.getUniqueId(), new Cooldown((Long) params[0]));
    }

    /**
     * Cooldown submitter
     *
     * @param player
     * @param cooldown
     */
    public void submitCooldown(OfflinePlayer player, long cooldown) {
        cache(player, cooldown);
    }

    /**
     * Cooldown checker
     *
     * @param uuid UUID
     * @return If player has a cooldown
     */
    public boolean isCooldown(UUID uuid) {
        return getCache().containsKey(uuid) && !getCache().get(uuid).isOver();
    }
}
