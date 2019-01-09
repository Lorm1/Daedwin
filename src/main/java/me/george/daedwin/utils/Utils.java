package me.george.daedwin.utils;

import com.google.common.collect.Lists;
import me.george.daedwin.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Utils {

    public static Logger log = Constants.log;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, YY hh:mm aa");

    private static SimpleDateFormat timeFormat = new SimpleDateFormat("dd hh:mm aa");
    private static DecimalFormat dFormat = new DecimalFormat("#,###.##");
    private static DecimalFormat format = new DecimalFormat("#.##");

    public static String getDateString() {
        return getDateString(System.currentTimeMillis());
    }

    public static String getDateString(long time) {
        return dateFormat.format(new Date(time));
    }

    public static String getTimeString(long time) {
        return timeFormat.format(new Date(time));
    }

    public static String translate(String string) {
        if (string == null) return "";
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static <T extends Comparable> LinkedHashMap<UUID, T> sortMap(Map<UUID, T> unsortMap) {
        List<Map.Entry<UUID, T>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        // Maintaining insertion order with the help of LinkedList
        LinkedHashMap<UUID, T> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<UUID, T> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static List<String> getLoreFromLine(String description, ChatColor color, String split) {
        List<String> retr = Lists.newArrayList();
        if (!description.contains(split)) {
            retr.add(color + Utils.translate(description));
        } else {
            for (String string : description.split(split)) {
                retr.add(color + Utils.translate(string));
            }
        }
        return retr;
    }

    public static void addChatColor(List<String> toAdd, ChatColor color) {
        for (int i = 0; i < toAdd.size(); i++) {
            toAdd.set(i, color + toAdd.get(i));
        }
    }

    public static String formatCommas(double val) {
        return dFormat.format(val);
    }

    public static void printTrace() {
        StackTraceElement trace = new Exception().getStackTrace()[2];

        Constants.log.info("[Database] Class: " + trace.getClassName());
        Constants.log.info("[Database] Method: " + trace.getMethodName());
        Constants.log.info("[Database] Line: " + trace.getLineNumber());
    }

    public static Location getLocation(String loc) {
        if (loc == null || !loc.contains(",")) return null;
        String[] args = loc.split(",");

        Location retr = new Location(Bukkit.getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));

        if (args.length >= 6) {
            retr.setYaw(Float.parseFloat(args[4]));
            retr.setPitch(Float.parseFloat(args[5]));
        }
        return retr;
    }

    public static String getStringFromLocation(Location location, boolean round) {
        StringBuilder retr = new StringBuilder();
        retr.append(location.getWorld().getName()).append(",").append(round ? format.format(location.getX()) : location.getX()).append(",")
                .append(round ? format.format(location.getY()) : location.getY()).append(",").append(round ? format.format(location.getZ()) : location.getZ());

        if (location.getYaw() != 0 || location.getPitch() != 0) {
            retr.append(",").append(location.getYaw()).append(",").append(location.getPitch());
        }
        return retr.toString();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String format(int number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static boolean randChance(int chance) {
        return randInt(chance) == 0;
    }

    public static int randInt(int max) {
        return randInt(0, max - 1);
    }

    public static int randInt(int min, int max) {
        int bound = max - min + 1;
        if (bound <= 0) return 0;
        return ThreadLocalRandom.current().nextInt(bound) + min;
    }

    public static float randFloat(float min, float max) {
        return ThreadLocalRandom.current().nextFloat() * (max - min) + min;
    }

    public static List<Player> getNearbyPlayers(Location location, int radius) {
        return getNearbyPlayers(location, radius, false);
    }

    public static List<Player> getNearbyPlayers(Location location, int radius, boolean ignoreVanish) {
        return location.getWorld().getPlayers().stream().filter(player -> !ignoreVanish).filter(player -> location.distanceSquared(player.getLocation()) <= radius * radius).collect(Collectors.toList());
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean containsItem(Inventory inv, Material material) {
        return inv.contains(material);
    }

    public static boolean containsItem(Inventory inv, String itemName) {
        itemName = ChatColor.stripColor(itemName);
        for (ItemStack itemStack : inv.getContents()) {
            if (itemStack == null) continue;
            if (itemStack.getItemMeta() == null) continue;
            if (!itemStack.getItemMeta().hasDisplayName()) continue;
            if (ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).equalsIgnoreCase(itemName)) return true;
        }

        return false;
    }

    public static <T> Set<T> findDuplicates(Collection<T> list) {
        Set<T> duplicates = new HashSet<T>();
        Set<T> uniques = new HashSet<T>();

        duplicates.addAll(list.stream().filter(t -> !uniques.add(t)).collect(Collectors.toList()));
        return duplicates;
    }

    public static String capitalize(String s) {
        return ucfirst(s);
    }

    public static String ucfirst(String string) {
        return Character.toUpperCase(string.charAt(0)) + string.substring(1).toLowerCase();
    }

    public static LivingEntity getTarget(LivingEntity entity, double range) {
        List<Entity> nearbyE = entity.getNearbyEntities(range,
                range, range);
        ArrayList<LivingEntity> livingE = nearbyE.stream().filter(e -> e instanceof LivingEntity)
                .map(e -> (LivingEntity) e).collect(Collectors.toCollection(ArrayList::new));

        LivingEntity target = null;
        BlockIterator bItr = new BlockIterator(entity, (int) range);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;
        // loop through player's line of sight
        while (bItr.hasNext()) {
            block = bItr.next();
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            // check for entities near this block in the line of sight
            for (LivingEntity e : livingE) {
                loc = e.getLocation();
                ex = loc.getX();
                ey = loc.getY();
                ez = loc.getZ();
                if ((bx - .75 <= ex && ex <= bx + 1.75) && (bz - .75 <= ez && ez <= bz + 1.75) && (by - 1 <= ey && ey <= by + 2.5)) {
                    // entity is close enough, set target and stop
                    target = e;
                    break;
                }
            }
        }
        return target;
    }

    public static String getDate(Long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC-0"));
        return dateFormat.format(new Date(milliseconds));
    }

    public static String getDate() {
        return getDate(System.currentTimeMillis());
    }

    public static String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9\\._]+", "_");
    }

    /**
     * Force deletes folders / files that meet certain parameters.
     */

    public static String capitalizeWords(String sentence) {
        String formatted = "";
        for (String s : sentence.split(" "))
            formatted += " " + Utils.capitalize(s);
        return formatted.length() > 1 ? formatted.substring(1) : formatted;
    }

    public static String getItemName(ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return "NOTHING";
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName())
            return meta.getDisplayName();

        return capitalizeWords(item.getType().name().toLowerCase().replaceAll("_", " "));
    }

    public static String[] getLore(ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return new String[]{};
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasLore())
            return meta.getLore().toArray(new String[meta.getLore().size()]);

        return new String[]{};
    }

    public static Location getRandomLocationNearby(Location loc, int radius) {
        Random r = ThreadLocalRandom.current();
        return loc.clone().add((r.nextBoolean() ? -1 : 1) * (r.nextInt(radius) + 1), 0, (r.nextBoolean() ? -1 : 1) * (r.nextInt(radius) + 1));
    }

    public static List<Entity> getNearbyEntities(Location where, int range) {
        List<Entity> found = new ArrayList<>();

        for (Entity entity : where.getWorld().getEntities()) {
            if (isInBorder(where, entity.getLocation(), range)) {
                found.add(entity);
            }
        }
        return found;
    }

    public static double distanceSquared(Location first, Location second) {
        if (first.getWorld() != second.getWorld()) return Integer.MAX_VALUE;
        return first.distanceSquared(second);
    }

    public static boolean isInBorder(Location center, Location notCenter, int range) {
        int x = center.getBlockX(), z = center.getBlockZ();
        int x1 = notCenter.getBlockX(), z1 = notCenter.getBlockZ();

        if (x1 >= (x + range) || z1 >= (z + range) || x1 <= (x - range) || z1 <= (z - range)) {
            return false;
        }
        return true;
    }
}
