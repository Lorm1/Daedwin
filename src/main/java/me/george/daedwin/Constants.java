package me.george.daedwin;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Constants {

    public static String SERVER_VERSION = Daedwin.getInstance().getDescription().getVersion();

    public static Logger log = Logger.getLogger("RPG");
    public static boolean debug = false;

    public static List<String> ADMINS = Arrays.asList("George_Angel");

    public static String MOTD = "                   &5&lMyths of Daedwin &r\n                  &f&lDevelopment Stage";
    public static String setMOTD = null;
    public static String MAINTENANCE_MOTD = "                   &5&lMyths of Daedwin &r\n                  &c&lMaintenance Mode &f&l";

    public static int PLAYER_SLOTS = 10;
}
