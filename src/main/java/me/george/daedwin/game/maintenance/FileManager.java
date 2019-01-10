package me.george.daedwin.game.maintenance;

import me.george.daedwin.Constants;
import me.george.daedwin.Daedwin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private File file;
    private YamlConfiguration config;

    private List<String> whitelistedPlayers = new ArrayList<>();

    public FileManager(Daedwin daedwin) {

        file = new File(daedwin.getDataFolder(), "whitelisted.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void setupWhitelist() {
        if (!Constants.ADMINS.isEmpty()) {
            for (String adminName : Constants.ADMINS) {
                addToWhitelist(adminName);
            }
        }
    }

    public void addToWhitelist(Player player) {
        if (!getWhitelistedPlayers().contains(player.getName())) {
            getWhitelistedPlayers().add(player.getName());

            config.set("Player", getWhitelistedPlayers());
            save();
        }
    }

    public void addToWhitelist(String playerName) {
        if (!getWhitelistedPlayers().contains(playerName)) {
            getWhitelistedPlayers().add(playerName);

            config.set("Player", getWhitelistedPlayers());
            save();
        }
    }

    public void removeFromWhitelist(Player player) {
        if (getWhitelistedPlayers().contains(player.getName())) {
            getWhitelistedPlayers().remove(player.getName());

            config.set("Player", getWhitelistedPlayers());
            save();
        }
    }

    public void removeFromWhitelist(String playerName) {
        if (getWhitelistedPlayers().contains(playerName)) {
            getWhitelistedPlayers().remove(playerName);

            config.set("Player", getWhitelistedPlayers());
            save();
        }
    }

    public List<String> getWhitelistedPlayers() {
        return whitelistedPlayers;
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
