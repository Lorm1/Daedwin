package me.george.daedwin.game.player;

import me.george.daedwin.Constants;
import me.george.daedwin.Daedwin;
import me.george.daedwin.server.Setup;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.manager.LogoutManager;
import me.george.daedwin.game.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Timestamp;

public class PlayerConnection implements Listener {

    @EventHandler
    public void onAsyncLogin(AsyncPlayerPreLoginEvent e) {

    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();

        String banMessage = ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "Myths of Daedwin\n\n\n"
                + ChatColor.RED.toString() + ChatColor.UNDERLINE + "You have been Banned.\n\n"
                + ChatColor.BLUE.toString() + ChatColor.UNDERLINE + "Reason" + ChatColor.BLUE + ": " + ChatColor.GRAY + Daedwin.getInstance().getPunishmentManager().getBanManager().getReason(p.getUniqueId()) + "\n\n"
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

        e.setJoinMessage(null);

        DatabaseAPI.loadPlayer(daedwinPlayer);

        DaedwinPlayer.getDaedwinPlayers().put(p.getUniqueId(), daedwinPlayer);

        for (Player player : Bukkit.getOnlinePlayers()) {
            DaedwinPlayer pl = DaedwinPlayer.getDaedwinPlayers().get(player.getUniqueId());

            if (pl.isStaff()) {
                pl.getPlayer().sendMessage(daedwinPlayer.isStaff() ? daedwinPlayer.getRank().getPrefix() + " " + ChatColor.AQUA + p.getDisplayName() + ChatColor.GRAY + " has joined." :
                        daedwinPlayer.getRank().getPrefix() + " " + ChatColor.GRAY + p.getName() + ChatColor.GRAY + " has joined.");
            }
        }

        if (daedwinPlayer.isAdmin()) {
            Daedwin._hiddenPlayers.add(p);
            p.setInvulnerable(true);
            p.setGameMode(GameMode.CREATIVE);
            p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "VANISH - " + ChatColor.GREEN.toString() + ChatColor.BOLD + "ON");
            p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "GAMEMODE - " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "CREATIVE");
            p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "GOD MODE - " + ChatColor.GREEN.toString() + ChatColor.BOLD + "ON");
        } else {
            p.setGameMode(GameMode.SURVIVAL);
            p.setFlying(false);
            p.setInvulnerable(false);
        }

        for (Player player : Daedwin._hiddenPlayers) {
            p.hidePlayer(player);
        }

        if (!p.hasPlayedBefore()) {
            p.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "Welcome to " + ChatColor.BLUE.toString() + ChatColor.BOLD + "Myths of Daedwin.\n");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 5);

            p.teleport(daedwinPlayer.getNation().getSpawnLocation());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        DaedwinPlayer daedwinPlayer = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        e.setQuitMessage(null);

        for (Player player : Bukkit.getOnlinePlayers()) {
            DaedwinPlayer pl = DaedwinPlayer.getDaedwinPlayers().get(player.getUniqueId());

            if (pl.isStaff()) {
                pl.getPlayer().sendMessage(daedwinPlayer.isStaff() ? daedwinPlayer.getRank().getPrefix() + " " + ChatColor.AQUA + p.getDisplayName() + ChatColor.GRAY + " has quit." :
                        daedwinPlayer.getRank().getPrefix() + " " + ChatColor.GRAY + p.getName() + ChatColor.GRAY + " has quit.");
            }
        }

        daedwinPlayer.setLastLogout(new Timestamp(System.currentTimeMillis()));

        LogoutManager.handleLogout(daedwinPlayer);

        DaedwinPlayer.getDaedwinPlayers().remove(daedwinPlayer.getPlayer().getUniqueId(), daedwinPlayer);

        if (Daedwin._hiddenPlayers.contains(p)) {
            Daedwin._hiddenPlayers.remove(p);
        }
    }
}
