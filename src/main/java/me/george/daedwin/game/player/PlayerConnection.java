package me.george.daedwin.game.player;

import me.george.daedwin.Daedwin;
import me.george.daedwin.Setup;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.LogoutManager;
import me.george.daedwin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerConnection implements Listener {

    @EventHandler
    public void onAsyncLogin(AsyncPlayerPreLoginEvent e) {

    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(Daedwin.getInstance(), () -> {
            if (Daedwin.getInstance().getBanManager().isBanned(p.getUniqueId())/*daedwinPlayer.isBanned*/) {
                e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You have been Banned. \n"
                        + ChatColor.AQUA.toString() + ChatColor.BOLD + "Reason: "
                        + ChatColor.WHITE + Daedwin.getInstance().getBanManager().getReason(p.getUniqueId())
                        + "\n\n" + ChatColor.GOLD.toString() + ChatColor.UNDERLINE
                        + "Duration: " + ChatColor.RED
                        + Daedwin.getInstance().getBanManager().getTimeLeft(p.getUniqueId()));
            }
        });

        DaedwinPlayer daedwinPlayer = new DaedwinPlayer(p);

        if (daedwinPlayer == null) {
            Utils.log.info("Could not load Player " + p.getName());
            return;
        }

        if (Setup.MAINTENANCE_MODE && (daedwinPlayer.isAdmin() || !Daedwin.getInstance().getFileManager().getWhitelistedPlayers().contains(p.getName()))) {
            e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, ChatColor.RED + "Daedwin is undergoing Maintenance.");
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

        DaedwinPlayer daedwinPlayer = new DaedwinPlayer(p);

        DatabaseAPI.loadPlayer(daedwinPlayer);

        DaedwinPlayer.getDaedwinPlayers().put(p.getUniqueId(), daedwinPlayer);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        DaedwinPlayer daedwinPlayer = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        LogoutManager.handleLogout(daedwinPlayer);

        DaedwinPlayer.getDaedwinPlayers().remove(daedwinPlayer.getPlayer().getUniqueId(), daedwinPlayer);
    }
}
