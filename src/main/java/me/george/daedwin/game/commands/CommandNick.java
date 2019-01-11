package me.george.daedwin.game.commands;

import me.george.daedwin.Daedwin;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandNick implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

        if (!player.isAdmin()) return true;

        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "You did not specify a nickname.");
            return false;
        } else if (args.length == 1) {
            String nick = args[0];

            nick = nick.replaceAll("&", "§");

            if (nick.equalsIgnoreCase("off")) {
                Daedwin.getInstance().getConfig().set(p.getName(), null);
                Daedwin.getInstance().saveConfig();
                player.setNickname(null);
                p.setDisplayName(p.getName());
                p.setPlayerListName(p.getName());
                p.sendMessage(ChatColor.GREEN + "You have reset your nickname.");
                return true;
            }

            p.sendMessage(ChatColor.GREEN + "You have changed your nickname to " + nick);
            player.setNickname(nick);
            p.setDisplayName(nick);
            p.setPlayerListName(nick);

            Daedwin.getInstance().getConfig().set(p.getName(), nick);
            Daedwin.getInstance().saveConfig();
        } else if(args.length >= 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                p.sendMessage(ChatColor.RED + "That player is offline.");
                return true;
            }

            String nick = args[1];

            nick = nick.replaceAll("&", "§");

            DaedwinPlayer targetPlayer = DaedwinPlayer.getDaedwinPlayers().get(target.getUniqueId());

            if (nick.equalsIgnoreCase("off")) {
                Daedwin.getInstance().getConfig().set(target.getName(), null);
                Daedwin.getInstance().saveConfig();
                targetPlayer.setNickname(null);
                target.setDisplayName(target.getName());
                target.setPlayerListName(target.getName());
                p.sendMessage(ChatColor.GREEN + "Removed " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + "'s nickname.");
                return true;
            }

            p.sendMessage(ChatColor.GREEN + "You have changed " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + "'s nickname to " + nick);
            target.sendMessage(ChatColor.GREEN + "Your nickname is now " + nick);

            targetPlayer.setNickname(nick);
            target.setDisplayName(nick);
            target.setPlayerListName(nick);

            Daedwin.getInstance().getConfig().set(target.getName(), nick);
            Daedwin.getInstance().saveConfig();
        } else {
            p.sendMessage(ChatColor.RED + "Invalid Usage.");
            return false;
        }
        return true;
    }
}
