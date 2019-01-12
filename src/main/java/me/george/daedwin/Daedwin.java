package me.george.daedwin;

import me.george.daedwin.database.Database;
import me.george.daedwin.game.chat.Chat;
import me.george.daedwin.game.commands.player.CommandList;
import me.george.daedwin.game.commands.player.CommandLogout;
import me.george.daedwin.game.commands.essentials.server.CommandMaintenance;
import me.george.daedwin.game.commands.essentials.*;
import me.george.daedwin.game.commands.essentials.modes.CommandFly;
import me.george.daedwin.game.commands.essentials.modes.CommandGameMode;
import me.george.daedwin.game.commands.essentials.modes.CommandGodMode;
import me.george.daedwin.game.commands.essentials.modes.CommandSpeed;
import me.george.daedwin.game.commands.essentials.warps.CommandDelWarp;
import me.george.daedwin.game.commands.essentials.warps.CommandSetWarp;
import me.george.daedwin.game.commands.essentials.warps.CommandWarp;
import me.george.daedwin.game.commands.moderation.*;
import me.george.daedwin.game.commands.moderation.chat.CommandClearChat;
import me.george.daedwin.game.commands.moderation.chat.CommandMuteChat;
import me.george.daedwin.game.commands.moderation.chat.CommandShout;
import me.george.daedwin.game.commands.moderation.punishment.CommandKick;
import me.george.daedwin.game.commands.moderation.punishment.ban.CommandBan;
import me.george.daedwin.game.commands.moderation.punishment.ban.CommandCheckBan;
import me.george.daedwin.game.commands.moderation.punishment.ban.CommandUnban;
import me.george.daedwin.game.commands.moderation.punishment.mute.CommandCheckMute;
import me.george.daedwin.game.commands.moderation.punishment.mute.CommandMute;
import me.george.daedwin.game.commands.moderation.punishment.mute.CommandUnmute;
import me.george.daedwin.game.maintenance.FileManager;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.game.player.PlayerConnection;
import me.george.daedwin.game.punishment.PunishmentManager;
import me.george.daedwin.game.rank.RankManager;
import me.george.daedwin.game.world.Restrictions;
import me.george.daedwin.utils.ConcurrentSet;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class Daedwin extends JavaPlugin {

    private static Daedwin instance = null;
    public static Daedwin getInstance() {
        return instance;
    }

    private FileManager fileManager;
    private PunishmentManager punishmentManager;
    private RankManager rankManager;

    public FileManager getFileManager() {
        return fileManager;
    }
    public PunishmentManager getPunishmentManager() { return punishmentManager; }
    public RankManager getRankManager() { return rankManager; }

    public static Set<Player> _hiddenPlayers = new ConcurrentSet<>();

    public void onEnable() {
        instance = this;

        setupServer();

        Database.getInstance().connect();

        getLogger().info("Enabling Daedwin v." + Constants.SERVER_VERSION);

        // Config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Register events and commands
        registerEvents();
        registerCommands();
    }

    public void onDisable() {
        instance = null;

        // Database.getInstance().disconnect(); // Doesn't really serve much, it's fine to leave it open.

        getLogger().info("Disabling Daedwin v." + Constants.SERVER_VERSION);

        clearCache();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerConnection(), this);
        getServer().getPluginManager().registerEvents(new Chat(), this);
        getServer().getPluginManager().registerEvents(new Restrictions(), this);
        getServer().getPluginManager().registerEvents(new Setup(), this);
    }

    private void registerCommands() {
        this.getCommand("freeze").setExecutor(new CommandFreeze());
        this.getCommand("logout").setExecutor(new CommandLogout());
        this.getCommand("mutechat").setExecutor(new CommandMuteChat());
        this.getCommand("clearchat").setExecutor(new CommandClearChat());
        this.getCommand("ban").setExecutor(new CommandBan());
        this.getCommand("unban").setExecutor(new CommandUnban());
        this.getCommand("checkban").setExecutor(new CommandCheckBan());
        this.getCommand("mute").setExecutor(new CommandMute());
        this.getCommand("unmute").setExecutor(new CommandUnmute());
        this.getCommand("checkmute").setExecutor(new CommandCheckMute());
        this.getCommand("shout").setExecutor(new CommandShout());
        this.getCommand("setrank").setExecutor(new CommandSetRank());
        this.getCommand("maintenance").setExecutor(new CommandMaintenance());
        this.getCommand("vanish").setExecutor(new CommandVanish());
        this.getCommand("fly").setExecutor(new CommandFly());
        this.getCommand("nick").setExecutor(new CommandNick());
        this.getCommand("kick").setExecutor(new CommandKick());
        this.getCommand("feed").setExecutor(new CommandFeed());
        this.getCommand("heal").setExecutor(new CommandHeal());
        this.getCommand("speed").setExecutor(new CommandSpeed());
        this.getCommand("whois").setExecutor(new CommandWhoIs());
        this.getCommand("gamemode").setExecutor(new CommandGameMode());
        this.getCommand("god").setExecutor(new CommandGodMode());
        this.getCommand("spawnmob").setExecutor(new CommandSpawnEntity());
        this.getCommand("clearmobs").setExecutor(new CommandClearMobs());
        this.getCommand("teleport").setExecutor(new CommandTeleport());
        this.getCommand("warp").setExecutor(new CommandWarp());
        this.getCommand("setwarp").setExecutor(new CommandSetWarp());
        this.getCommand("delwarp").setExecutor(new CommandDelWarp());
        this.getCommand("list").setExecutor(new CommandList());
    }

    private void setupServer() {
        setupManagers();
        Setup.setupTablist();
    }
    private void setupManagers() {
        fileManager = new FileManager(this);
        punishmentManager = new PunishmentManager(this);
        rankManager = new RankManager(this);
    }

    private void clearCache() {
        DaedwinPlayer.getDaedwinPlayers().clear();
        _hiddenPlayers.clear();
    }
}
