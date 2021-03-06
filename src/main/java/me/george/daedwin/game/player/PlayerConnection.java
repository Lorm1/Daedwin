package me.george.daedwin.game.player;

import me.george.daedwin.Constants;
import me.george.daedwin.Daedwin;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.health.HealthDisplay;
import me.george.daedwin.game.nation.Nation;
import me.george.daedwin.game.rank.Rank;
import me.george.daedwin.manager.LogoutManager;
import me.george.daedwin.server.Setup;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Timestamp;
import java.util.UUID;

public class PlayerConnection implements Listener {

    @EventHandler
    public void onAsyncLogin(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();

        String banMessage = ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "Myths of Daedwin\n\n\n"
                + ChatColor.RED.toString() + ChatColor.UNDERLINE + "You have been Banned.\n\n"
                + ChatColor.BLUE.toString() + ChatColor.UNDERLINE + "Reason" + ChatColor.BLUE + ": " + ChatColor.GRAY +
                Daedwin.getInstance().getPunishmentManager().getBanManager().getReason(uuid) + "\n\n"
                + ChatColor.GOLD.toString() + ChatColor.UNDERLINE + "Expires" + ChatColor.GOLD + ": " + ChatColor.DARK_RED +
                Daedwin.getInstance().getPunishmentManager().getBanManager().getTimeLeft(uuid) + "\n\n\n"
                + ChatColor.GRAY + "Find out more at: " + ChatColor.BLUE.toString() + ChatColor.UNDERLINE + Constants.SITE_NAME;

        if (Daedwin.getInstance().getPunishmentManager().getBanManager().isBanned(uuid)) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, banMessage);
            return;
        }

//        if (e.getLoginResult() == AsyncPlayerPreLoginEvent.Result.KICK_FULL) {
//            if (Daedwin.getInstance().getRankManager().getRank(uuid).equals(Rank.ADMIN) ||
//                    Daedwin.getInstance().getRankManager().getRank(uuid).equals(Rank.MOD) ||
//                    Daedwin.getInstance().getRankManager().getRank(uuid).equals(Rank.DONATOR)/*p.isOp() || Constants.ADMINS.contains(p.getName()) || p.hasPermission("rpg.bypass")*/) {
//                e.allow();
//            }
//        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        DaedwinPlayer daedwinPlayer = DaedwinPlayer.getInstanceOfPlayer(p);

        if (e.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            if (daedwinPlayer.isStaff() || daedwinPlayer.isDonator) {
                e.allow();
                p.sendMessage(ChatColor.GREEN + "You were allowed to join because of your status.");
            }
        }

        if (Setup.MAINTENANCE_MODE && !Daedwin.getInstance().getFileManager().getWhitelistedPlayers().contains(p.getName().toLowerCase())) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.BLUE.toString() + ChatColor.BOLD.toString()
                    + ChatColor.UNDERLINE + "Myths of Daedwin\n\n\n"
                    + ChatColor.RED.toString() + ChatColor.UNDERLINE + "Maintenance Mode\n\n\n"
                    + ChatColor.GRAY + "Find out more at: " + ChatColor.BLUE.toString() + ChatColor.UNDERLINE + Constants.SITE_NAME);
            return;
        }

////        Bukkit.getScheduler().runTaskAsynchronously(Daedwin.getInstance(), () -> {
//        if (Daedwin.getInstance().getPunishmentManager().getBanManager().isBanned(p.getUniqueId())/*daedwinPlayer.isBanned*/) {
//            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, banMessage/*ChatColor.RED + "You have been Banned. \n"
//                        + ChatColor.AQUA.toString() + ChatColor.BOLD + "Reason: "
//                        + ChatColor.WHITE + Daedwin.getInstance().getBanManager().getReason(p.getUniqueId())
//                        + "\n\n" + ChatColor.GOLD.toString() + ChatColor.UNDERLINE
//                        + "Duration: " + ChatColor.RED
//                        + Daedwin.getInstance().getBanManager().getTimeLeft(p.getUniqueId())*/);
//            return;
//        }
//        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        DatabaseAPI.loadPlayer(p);

        DaedwinPlayer daedwinPlayer = DaedwinPlayer.getInstanceOfPlayer(p);

        DaedwinPlayer.getDaedwinPlayers().put(p.getUniqueId(), daedwinPlayer);

        e.setJoinMessage(null);

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
            player.setPlayerListName(null);
        }

        if (!p.hasPlayedBefore()) {
            p.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "Welcome to " + ChatColor.BLUE.toString() + ChatColor.BOLD + "Myths of Daedwin\n");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 5);

            p.sendActionBar(ChatColor.GREEN + "Welcome!");
            p.sendTitle(ChatColor.WHITE.toString() + ChatColor.BOLD + "Welcome to", ChatColor.BLUE.toString() + ChatColor.BOLD +
                    "Myths of Daedwin", 2, 3, 3);

            p.teleport(Nation.HUMAN.getSpawnLocation()); // change to a proper tutorial area at some point

            p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 5, 50));
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 35));
        } else {
            p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 5, 5);

            p.sendActionBar(ChatColor.GREEN + "Welcome!");
            p.sendTitle(ChatColor.BLUE + "Welcome back,",ChatColor.WHITE + p.getName(), 20 * 2, 20 * 3, 20 * 3);

            p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 1, 50));
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 1, 50));
        }

        HealthDisplay.setup();

        p.setPlayerListHeaderFooter(ChatColor.BLUE.toString() + ChatColor.BOLD + "Myths of Daedwin",
                ChatColor.GRAY + "Lv." + ChatColor.BLUE + "0\n" + ChatColor.GRAY + "Rank: " + Rank.getColor(daedwinPlayer.getRank()) + daedwinPlayer.getRank().name());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        DaedwinPlayer daedwinPlayer = DaedwinPlayer.getInstanceOfPlayer(p);

        e.setQuitMessage(null);

        for (Player player : Bukkit.getOnlinePlayers()) {
            DaedwinPlayer pl = DaedwinPlayer.getDaedwinPlayers().get(player.getUniqueId());

            if (pl.isStaff()) {
                pl.getPlayer().sendMessage(daedwinPlayer.isStaff() ? daedwinPlayer.getRank().getPrefix() + " " + ChatColor.AQUA + p.getDisplayName() + ChatColor.GRAY + " has quit." :
                        daedwinPlayer.getRank().getPrefix() + " " + ChatColor.GRAY + p.getName() + ChatColor.GRAY + " has quit.");
            }
        }

        daedwinPlayer.setLastLogout(new Timestamp(System.currentTimeMillis()));

        LogoutManager.handleLogout(p);

        DaedwinPlayer.getDaedwinPlayers().remove(daedwinPlayer.getPlayer().getUniqueId(), daedwinPlayer);

        if (Daedwin._hiddenPlayers.contains(p)) {
            Daedwin._hiddenPlayers.remove(p);
        }
    }
}
