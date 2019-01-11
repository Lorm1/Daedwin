package me.george.daedwin;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private File whitelistFile;
    private File nicknameFile;

    private YamlConfiguration whitelistConfig;
    private YamlConfiguration nicknameConfig;

    private List<String> whitelistedPlayers = new ArrayList<>();

    public FileManager(Daedwin daedwin) {

        whitelistFile = new File(daedwin.getDataFolder(), "whitelisted.yml");

        if (!whitelistFile.exists()) {
            try {
                whitelistFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        whitelistConfig = YamlConfiguration.loadConfiguration(whitelistFile);

        setupWhitelist();
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

            whitelistConfig.set("Player", getWhitelistedPlayers());
            saveWhitelist();
        }
    }

    public void addToWhitelist(String playerName) {
        if (!getWhitelistedPlayers().contains(playerName)) {
            getWhitelistedPlayers().add(playerName);

            whitelistConfig.set("Player", getWhitelistedPlayers());
            saveWhitelist();
        }
    }

    public void removeFromWhitelist(Player player) {
        if (getWhitelistedPlayers().contains(player.getName())) {
            getWhitelistedPlayers().remove(player.getName());

            whitelistConfig.set("Player", getWhitelistedPlayers());
            saveWhitelist();
        }
    }

    public void removeFromWhitelist(String playerName) {
        if (getWhitelistedPlayers().contains(playerName)) {
            getWhitelistedPlayers().remove(playerName);

            whitelistConfig.set("Player", getWhitelistedPlayers());
            saveWhitelist();
        }
    }

    public List<String> getWhitelistedPlayers() {
        return whitelistedPlayers;
    }

    private void saveWhitelist() {
        try {
            whitelistConfig.save(whitelistFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
