package me.george.daedwin;

import me.george.daedwin.database.Database;
import me.george.daedwin.game.chat.Chat;
import me.george.daedwin.game.commands.*;
import me.george.daedwin.game.maintenance.FileManager;
import me.george.daedwin.game.player.PlayerConnection;
import me.george.daedwin.game.player.Restrictions;
import me.george.daedwin.game.punishment.ban.BanManager;
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
    private BanManager banManager;

    public FileManager getFileManager() {
        return fileManager;
    }
    public BanManager getBanManager() { return banManager; }

    public static Set<Player> _hiddenPlayers = new ConcurrentSet<>();

    public void onEnable() {
        instance = this;

        setupManagers();

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
        this.getCommand("shout").setExecutor(new CommandShout());
    }

    private void setupManagers() {
        fileManager = new FileManager(this);
        banManager = new BanManager(this);

        fileManager.setupWhitelist();
    }
}
