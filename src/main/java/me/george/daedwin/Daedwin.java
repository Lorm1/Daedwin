package me.george.daedwin;

import me.george.daedwin.database.Database;
import me.george.daedwin.game.chat.Chat;
import me.george.daedwin.game.commands.entity.CommandClearMobs;
import me.george.daedwin.game.commands.entity.CommandSpawnEntity;
import me.george.daedwin.game.commands.player.CommandList;
import me.george.daedwin.game.commands.player.CommandLogout;
import me.george.daedwin.game.commands.player.CommandMessage;
import me.george.daedwin.game.commands.player.moderation.*;
import me.george.daedwin.game.commands.player.moderation.chat.CommandAlert;
import me.george.daedwin.game.commands.player.moderation.chat.CommandClearChat;
import me.george.daedwin.game.commands.player.moderation.chat.CommandMuteChat;
import me.george.daedwin.game.commands.player.moderation.chat.CommandShout;
import me.george.daedwin.game.commands.player.moderation.inventory.CommandArmorSee;
import me.george.daedwin.game.commands.player.moderation.inventory.CommandInvSee;
import me.george.daedwin.game.commands.player.moderation.item.CommandGive;
import me.george.daedwin.game.commands.player.moderation.mode.CommandFly;
import me.george.daedwin.game.commands.player.moderation.mode.CommandGameMode;
import me.george.daedwin.game.commands.player.moderation.mode.CommandGodMode;
import me.george.daedwin.game.commands.player.moderation.mode.CommandSpeed;
import me.george.daedwin.game.commands.player.moderation.punishment.CommandKick;
import me.george.daedwin.game.commands.player.moderation.punishment.ban.CommandBan;
import me.george.daedwin.game.commands.player.moderation.punishment.ban.CommandCheckBan;
import me.george.daedwin.game.commands.player.moderation.punishment.ban.CommandUnban;
import me.george.daedwin.game.commands.player.moderation.punishment.mute.CommandCheckMute;
import me.george.daedwin.game.commands.player.moderation.punishment.mute.CommandMute;
import me.george.daedwin.game.commands.player.moderation.punishment.mute.CommandUnmute;
import me.george.daedwin.game.commands.player.moderation.state.CommandFeed;
import me.george.daedwin.game.commands.player.moderation.state.CommandFreeze;
import me.george.daedwin.game.commands.player.moderation.state.CommandHeal;
import me.george.daedwin.game.commands.player.moderation.state.CommandVanish;
import me.george.daedwin.game.commands.player.moderation.teleportation.teleport.CommandTeleport;
import me.george.daedwin.game.commands.player.moderation.teleportation.teleport.CommandTeleportLocation;
import me.george.daedwin.game.commands.player.moderation.teleportation.warp.CommandDelWarp;
import me.george.daedwin.game.commands.player.moderation.teleportation.warp.CommandSetWarp;
import me.george.daedwin.game.commands.player.moderation.teleportation.warp.CommandWarp;
import me.george.daedwin.game.commands.server.CommandMaintenance;
import me.george.daedwin.game.nation.NationManager;
import me.george.daedwin.game.player.DaedwinPlayer;
import me.george.daedwin.game.player.PlayerConnection;
import me.george.daedwin.game.profession.farming.Farming;
import me.george.daedwin.game.punishment.PunishmentManager;
import me.george.daedwin.game.rank.RankManager;
import me.george.daedwin.game.world.Restrictions;
import me.george.daedwin.manager.FileManager;
import me.george.daedwin.server.Setup;
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
    private RankManager rankManager;
    private PunishmentManager punishmentManager;
    private NationManager nationManager;

    public FileManager getFileManager() {
        return fileManager;
    }
    public PunishmentManager getPunishmentManager() { return punishmentManager; }
    public RankManager getRankManager() { return rankManager; }
    public NationManager getNationManager() { return nationManager; }

    public static Set<Player> _hiddenPlayers = new ConcurrentSet<>();

    public void onEnable() {
        instance = this;

        setupServer();

        getLogger().info("Enabling Daedwin v." + Constants.SERVER_VERSION);
    }

    public void onDisable() {
        instance = null;

        closeServer();

        getLogger().info("Disabling Daedwin v." + Constants.SERVER_VERSION);
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerConnection(), this);
        getServer().getPluginManager().registerEvents(new Chat(), this);
        getServer().getPluginManager().registerEvents(new Restrictions(), this);
        getServer().getPluginManager().registerEvents(new Setup(), this);
        getServer().getPluginManager().registerEvents(new Farming(), this);
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
        this.getCommand("invsee").setExecutor(new CommandInvSee());
        this.getCommand("armorsee").setExecutor(new CommandArmorSee());
        this.getCommand("give").setExecutor(new CommandGive());
        this.getCommand("message").setExecutor(new CommandMessage());
        this.getCommand("setnation").setExecutor(new CommandSetNation());
        this.getCommand("teleportlocation").setExecutor(new CommandTeleportLocation());
        this.getCommand("professions").setExecutor(new CommandProfessions());
        this.getCommand("alert").setExecutor(new CommandAlert());
    }

    private void setupServer() {
        setupManagers();
        Database.getInstance().connect();

        // Config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Register events and commands
        registerEvents();
        registerCommands();
    }

    private void closeServer() {
        clearCache();
        // Database.getInstance().disconnect();
    }

    private void setupManagers() {
        fileManager = new FileManager(this);
        rankManager = new RankManager(this);
        punishmentManager = new PunishmentManager(this);
        nationManager = new NationManager(this);
    }

    private void clearCache() {
        DaedwinPlayer.getDaedwinPlayers().clear();
        _hiddenPlayers.clear();
    }
}
