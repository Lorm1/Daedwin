package me.george.daedwin.game.commands.player.moderation;

import me.george.daedwin.Daedwin;
import me.george.daedwin.database.DatabaseAPI;
import me.george.daedwin.game.nation.Nation;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandSetNation implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

            if (!player.isAdmin()) return true;

            if (args.length < 2) {
                p.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }

            String targetName = args[0];

            if (!DatabaseAPI.playerExists(targetName)) {
                p.sendMessage(ChatColor.RED + "That player has never played before.");
                return true;
            }

            UUID targetUUID = DatabaseAPI.getPlayerUUID(targetName);
            DaedwinPlayer target = DaedwinPlayer.getDaedwinPlayers().get(targetUUID);

            if (Nation.isValidNation(args[1].toUpperCase())) {
                Nation nation = Nation.valueOf(args[1].toUpperCase());

                Daedwin.getInstance().getNationManager().setNation(targetUUID, nation);
                p.sendMessage(ChatColor.GREEN + "Set " + ChatColor.YELLOW + targetName + ChatColor.GREEN + "'s Nation to " + target.getNation().getColor() + target.getNation().getPrefix());
                target.getPlayer().sendMessage(ChatColor.GREEN + "You are now a " + target.getNation().getColor() + target.getNation().getName());
                if (!target.getHasNickname()) {
                    target.getPlayer().setPlayerListName(target.getNation().getColor() + target.getPlayer().getName());
                }
            } else {
                p.sendMessage(ChatColor.RED + "Invalid Nation.");
                for (Nation nation1 : Nation.values()) {
                    p.sendMessage("Valid Nations: " + Nation.getColor(nation1) + nation1.name());
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }

            String targetName = args[0];

            if (!DatabaseAPI.playerExists(targetName)) {
                sender.sendMessage(ChatColor.RED + "That player has never played before.");
                return true;
            }

            UUID targetUUID = DatabaseAPI.getPlayerUUID(targetName);
            DaedwinPlayer target = DaedwinPlayer.getDaedwinPlayers().get(targetUUID);

            if (Nation.isValidNation(args[1].toUpperCase())) {
                Nation nation = Nation.valueOf(args[1].toUpperCase());

                Daedwin.getInstance().getNationManager().setNation(targetUUID, nation);
                sender.sendMessage(ChatColor.GREEN + "Set " + ChatColor.YELLOW + targetName + ChatColor.GREEN + "'s Nation to " + target.getNation().getColor() + target.getNation().getPrefix());
                target.getPlayer().sendMessage(ChatColor.GREEN + "You are now a " + target.getNation().getColor() + target.getNation().getName());
                if (!target.getHasNickname()) {
                    target.getPlayer().setPlayerListName(target.getNation().getColor() + target.getPlayer().getName());
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid Nation.");
                for (Nation nation1 : Nation.values()) {
                    sender.sendMessage("Valid Nations: " + Nation.getColor(nation1) + nation1.name());
                }
            }
        }
        return true;
    }
}
