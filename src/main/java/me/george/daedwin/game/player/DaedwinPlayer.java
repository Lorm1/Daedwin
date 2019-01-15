package me.george.daedwin.game.player;

import lombok.Getter;
import lombok.Setter;
import me.george.daedwin.Constants;
import me.george.daedwin.game.nation.Nation;
import me.george.daedwin.game.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DaedwinPlayer {

    @Getter
    static Map<UUID, DaedwinPlayer> daedwinPlayers = new ConcurrentHashMap<>();

    Player player;
    UUID uuid;

    @Getter
    @Setter
    int gold, ecash;

    @Getter
    @Setter
    Location playerLocation;

    @Getter
    @Setter
    Integer mobKills, playerKills, mobDeaths, playerDeaths;

    @Getter
    @Setter
    Integer level, health, experience;

//    @Getter
//    @Setter
//    Boolean isBanned;

    @Getter
    @Setter
    Boolean isMuted;

    @Getter
    @Setter
    Boolean isDonator;

//    @Getter
//    @Setter
//    String muteReason/*, banReason*/;

    @Getter
    @Setter
    Rank rank;

    @Getter
    @Setter
    Nation nation;

    @Getter
    @Setter
    Timestamp joinDate, lastLogin, lastLogout;

    @Getter
    @Setter
    Boolean playingStatus;

    String nickName;

    @Getter
    @Setter
    Boolean hasNickname;

//    @Getter
//    @Setter
//    String banDuration;

    public DaedwinPlayer(UUID uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(this.uuid);
    }

    public DaedwinPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
    }

    public DaedwinPlayer(OfflinePlayer player) {
        this.uuid = player.getUniqueId();
    }


    public void setNickname(String name) {
        if (name != null && name != "") hasNickname = true;
        this.nickName = name;
    }

    public String getNickname() {
        if (nickName == null) {
            hasNickname = false;
            return getPlayer().getName(); // no nickname
        }
        hasNickname = true;
        return this.nickName;
    }

    public String getName() {
        return this.getPlayer().getName();
    }
    public void addGold(int gold) {
        assert gold >= 0;
        setGold(getGold() + gold);
    }

    public void subtractGold(int gold) {
        assert gold >= 0;
        setGold(getGold() - gold);
    }

    public void addEcash(int ecash) {
        assert ecash >= 0;
        setEcash(getEcash() + ecash);
    }

    public void subtractEcash(int ecash) {
        assert ecash >= 0;
        setEcash(getEcash() - ecash);
    }

    public Player getPlayer() { // bukkit player; default methods
        return this.player;
    }

    public boolean isAdmin() {
        if (getRank().equals(Rank.ADMIN) || Constants.ADMINS.contains(getPlayer().getName())) return true;
        return false;
    }

    public boolean isStaff() {
        if (isAdmin() || getRank().equals(Rank.MOD)) return true;
        return false;
    }

    public boolean isDonator() {
        if (getRank().equals(Rank.DONATOR) || isAdmin()) return true;
        return false;
    }

    public boolean isOnline() {
        Player p = getPlayer();
        return p != null && p.isOnline();
    }

    public boolean getPlayingStatus() {
        if (isOnline()) {
            return true;
        }
        return false;
    }
}
