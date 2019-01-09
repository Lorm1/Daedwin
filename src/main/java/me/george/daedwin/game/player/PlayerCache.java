package me.george.daedwin.game.player;

import org.bukkit.OfflinePlayer;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerCache {

    public static ConcurrentHashMap<OfflinePlayer, DaedwinPlayer> offlineDaedwinPlayerCache = new ConcurrentHashMap<>();
}
