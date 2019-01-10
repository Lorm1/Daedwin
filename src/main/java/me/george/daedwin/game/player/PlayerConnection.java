package me.george.daedwin.game.player;

import me.george.daedwin.Constants;
import me.george.daedwin.Daedwin;
import me.george.daedwin.Setup;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.LogoutManager;
import me.george.daedwin.game.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnection implements Listener {

    @EventHandler
    public void onAsyncLogin(AsyncPlayerPreLoginEvent e) {

    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();

        String banMessage = ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "Myths of Daedwin\n\n\n"
                + ChatColor.RED.toString() + ChatColor.UNDERLINE + "You have been Banned.\n\n"
                + ChatColor.GRAY.toString() + ChatColor.UNDERLINE + "Reason" + ChatColor.GRAY + ": " + ChatColor.WHITE + Daedwin.getInstance().getPunishmentManager().getBanManager().getReason(p.getUniqueId()) + "\n\n"
                + ChatColor.GOLD.toString() + ChatColor.UNDERLINE + "Expires" + ChatColor.GOLD + ": " + ChatColor.DARK_RED + Daedwin.getInstance().getPunishmentManager().getBanManager().getTimeLeft(p.getUniqueId()) + "\n\n\n"
                + ChatColor.GRAY + "Find out more at: " + ChatColor.BLUE.toString() + ChatColor.UNDERLINE + Constants.SITE_NAME;

//        Bukkit.getScheduler().runTaskAsynchronously(Daedwin.getInstance(), () -> {
            if (Daedwin.getInstance().getPunishmentManager().getBanManager().isBanned(p.getUniqueId())/*daedwinPlayer.isBanned*/) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, banMessage/*ChatColor.RED + "You have been Banned. \n"
                        + ChatColor.AQUA.toString() + ChatColor.BOLD + "Reason: "
                        + ChatColor.WHITE + Daedwin.getInstance().getBanManager().getReason(p.getUniqueId())
                        + "\n\n" + ChatColor.GOLD.toString() + ChatColor.UNDERLINE
                        + "Duration: " + ChatColor.RED
                        + Daedwin.getInstance().getBanManager().getTimeLeft(p.getUniqueId())*/);
                return;
            }
//        });

        if (Setup.MAINTENANCE_MODE && !Daedwin.getInstance().getFileManager().getWhitelistedPlayers().contains(p.getName().toLowerCase())) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "Myths of Daedwin\n\n\n"
                    + ChatColor.RED.toString() + ChatColor.UNDERLINE + "Maintenance Mode\n\n\n"
                    + ChatColor.GRAY + "Find out more at: " + ChatColor.BLUE.toString() + ChatColor.UNDERLINE + Constants.SITE_NAME);
            return;
        }

        if (e.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            if (Daedwin.getInstance().getRankManager().getRank(p.getUniqueId()).equals(Rank.ADMIN) ||
                    Daedwin.getInstance().getRankManager().getRank(p.getUniqueId()).equals(Rank.MOD) ||
                    p.isOp() || Daedwin.getInstance().getRankManager().getRank(p.getUniqueId()).equals(Rank.DONATOR)/*p.isOp() || Constants.ADMINS.contains(p.getName()) || p.hasPermission("rpg.bypass")*/) {
                e.allow();

                p.sendMessage(ChatColor.GREEN + "You were allowed to join due to your status.");
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        DaedwinPlayer daedwinPlayer = new DaedwinPlayer(p);

        DatabaseAPI.loadPlayer(daedwinPlayer);

        DaedwinPlayer.getDaedwinPlayers().put(p.getUniqueId(), daedwinPlayer);

        e.setJoinMessage(null);
        for (Player player : Bukkit.getOnlinePlayers()) {
            DaedwinPlayer pl = DaedwinPlayer.getDaedwinPlayers().get(player.getUniqueId());

            if (pl.isStaff()) {
                pl.getPlayer().sendMessage(daedwinPlayer.isStaff() ? daedwinPlayer.getRank().getPrefix() + " " + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " has joined the game." :
                        daedwinPlayer.getRank().getPrefix() + " " + ChatColor.GRAY + p.getName() + ChatColor.GRAY + " has joined the game.");
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        DaedwinPlayer daedwinPlayer = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        LogoutManager.handleLogout(daedwinPlayer);

        DaedwinPlayer.getDaedwinPlayers().remove(daedwinPlayer.getPlayer().getUniqueId(), daedwinPlayer);
    }
}
