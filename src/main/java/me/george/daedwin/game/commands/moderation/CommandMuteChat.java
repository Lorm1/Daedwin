package me.george.daedwin.game.commands.moderation;

import me.george.daedwin.game.chat.Chat;
import me.george.daedwin.game.player.DaedwinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMuteChat implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Check what command was just typed
        /*if (cmd.getName().equalsIgnoreCase("mutechat")) {*/
        // Check if sender (player) has permission/is op.
        if (sender instanceof Player) {
            Player p = (Player) sender;
            DaedwinPlayer player = DaedwinPlayer.getDaedwinPlayers().get(p.getUniqueId());

            if (player.isStaff()) {
                // Invert the chatEnabled boolean to the opposite
                Chat.chatEnabled = !Chat.chatEnabled;
                // See if the chatEnabled boolean is true if it is print the string 'Unmuted the chat' if not then 'Muted the chat'
                sender.sendMessage(ChatColor.GREEN + (Chat.chatEnabled ? "Unmuted the chat" : "Muted the chat"));

                Bukkit.broadcastMessage(ChatColor.BLUE + (Chat.chatEnabled ? "The chat has been unmuted." : "The chat has been muted."));
            }
        }
        /*}*/
        // Return true always since there is no actual usage other than the name of the command.
        return true;
    }
}
