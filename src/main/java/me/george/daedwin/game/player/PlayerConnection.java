package me.george.daedwin.game.player;

import me.george.daedwin.Daedwin;
import me.george.daedwin.Setup;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerConnection implements Listener {

    @EventHandler
    public void onAsyncLogin(AsyncPlayerPreLoginEvent e) {
       UUID uuid = e.getUniqueId();
       OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

       DatabaseAPI.loadPlayer(uuid, player);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        DaedwinPlayer daedwinPlayer;
        if (PlayerCache.offlineDaedwinPlayerCache.containsKey(p)) {
            daedwinPlayer = PlayerCache.offlineDaedwinPlayerCache.get(p);
        } else {
            Utils.log.info("Could not load player data. PlayerLoginEvent error for player " + p.getName());
            return;
        }

        if (Setup.MAINTENANCE_MODE && (daedwinPlayer.isAdmin() || !Daedwin.getInstance().getFileManager().getWhitelistedPlayers().contains(p.getName()))) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Daedwin is undergoing Maintenance.");
        }

        if (daedwinPlayer.isBanned) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "You have been Banned. \n" + ChatColor.AQUA + "By: " + ChatColor.WHITE + daedwinPlayer.bannedBy + ChatColor.AQUA.toString() + ChatColor.BOLD + "Reason: " + ChatColor.WHITE + daedwinPlayer.getBanReason() + "\n\n" + ChatColor.GOLD.toString() + ChatColor.UNDERLINE + "Duration: " + ChatColor.RED + daedwinPlayer.getBanDuration().toString());
        }

        if (e.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            if (daedwinPlayer.isStaff() || daedwinPlayer.isDonator()/*p.isOp() || Constants.ADMINS.contains(p.getName()) || p.hasPermission("rpg.bypass")*/) {
                e.allow();

                p.sendMessage(ChatColor.GREEN + "You were allowed to join due to your Status.");
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        PlayerCache.offlineDaedwinPlayerCache.remove(p);

        DaedwinPlayer daedwinPlayer = new DaedwinPlayer(p);
        DaedwinPlayer.getDaedwinPlayers().put(p.getUniqueId(), daedwinPlayer);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        DaedwinPlayer daedwinPlayer = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        daedwinPlayer.logout();
    }
}
