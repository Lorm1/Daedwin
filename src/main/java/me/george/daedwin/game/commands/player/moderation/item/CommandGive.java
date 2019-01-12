package me.george.daedwin.game.commands.player.moderation.item;

import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandGive implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

            if (!player.isAdmin()) return true;

            if (args.length == 0) { // /give
                p.sendMessage(ChatColor.RED + "Invalid Usage");
                return false;
            } else if (args.length == 1) { // /give <item>
                Material material;
                try {
                    material = Material.valueOf(args[0].toUpperCase());
                } catch (Exception e) {
                    p.sendMessage(ChatColor.RED + "Invalid Item.");
                    return true;
                }

                ItemStack item = new ItemStack(material);
                item.setAmount(64); // since he hasnt specified anything, default to this.
                p.getInventory().addItem(item);
                p.sendMessage(ChatColor.GREEN + "Added " + ChatColor.GOLD + "64 " + ChatColor.RED + (item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name().replace("_", "").toLowerCase())
                        + ChatColor.GREEN + " to your inventory.");
            } else if (args.length == 2) { // give <item> <amount>
                Material material;
                int amount;
                try {
                    material = Material.valueOf(args[0].toUpperCase());
                } catch (Exception e) {
                    p.sendMessage(ChatColor.RED + "Invalid Item.");
                    return true;
                }

                try {
                    amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    p.sendMessage(ChatColor.RED + "Invalid Number.");
                    return true;
                }

                ItemStack item = new ItemStack(material);
                item.setAmount(amount);
                p.getInventory().addItem(item);
                p.sendMessage(ChatColor.GREEN + "Added " + ChatColor.GOLD + amount + " " + ChatColor.RED + (item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name().replace("_", "").toLowerCase()));
            } else {
                p.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0 || args.length == 1) {
                sender.sendMessage(ChatColor.RED + "Invalid Usage.");
                return false;
            } else if (args.length == 2) { // /give <player> <item>
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "That player is offline.");
                    return true;
                }
                Material material;
                try {
                    material = Material.valueOf(args[1].toUpperCase());
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Invalid Item.");
                    return true;
                }

                ItemStack item = new ItemStack(material);
                item.setAmount(64);
                target.getInventory().addItem(item);
                target.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "CONSOLE " + ChatColor.GREEN + " has given you " + ChatColor.GOLD + "64 " + ChatColor.RED + (item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name().replace("_", "").toLowerCase()));
                sender.sendMessage(ChatColor.GREEN + "Gave " + ChatColor.GOLD + "64 " + ChatColor.RED + (item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name().replace("_", "").toLowerCase()) + ChatColor.GREEN + " to " + ChatColor.YELLOW + target.getDisplayName());
            } else if (args.length == 3) { // /give <player> <item> <amount>
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "That player is offline.");
                }
                Material material;
                int amount;
                try {
                    material = Material.valueOf(args[1].toUpperCase());
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Invalid Item.");
                    return true;
                }

                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid Number");
                    return true;
                }

                ItemStack item = new ItemStack(material);
                item.setAmount(amount);
                target.getInventory().addItem(item);
                target.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "CONSOLE " + ChatColor.GREEN + " has given you " + ChatColor.GOLD + amount + " " + ChatColor.RED + (item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name().replace("_", "").toLowerCase()));
                sender.sendMessage(ChatColor.GREEN + "Gave " + ChatColor.GOLD + amount + " " + ChatColor.RED + (item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name().replace("_", "").toLowerCase()) + ChatColor.GREEN + " to " + ChatColor.YELLOW + target.getDisplayName());
            }
        }
        return true;
    }
}
