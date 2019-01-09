package me.george.daedwin.utils;

import me.george.daedwin.Daedwin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class ChatUtils {

    private static final Map<Player, Consumer<? super AsyncPlayerChatEvent>> chatListeners = new ConcurrentHashMap<>();
    private static final Map<Player, Consumer<? super Player>> orElseListeners = new ConcurrentHashMap<>();
    public static List<String> bannedWords = new ArrayList<>(Arrays.asList(
            "shit", "fuck", "cunt", "bitch", "whore",
            "slut", "wank", "asshole", "cock", "dick",
            "clit", "homo", "fag", "faggot", "queer",
            "nigger", "n1gger", "n1gg3r", "nigga", "dike",
            "dyke", "retard", " " + "motherfucker", "vagina", "boob",
            "pussy", "rape", "gay", "penis", "cunt",
            "titty", "anus", " faggot", "blowjob", "handjob",
            "bast", "minecade", "@ss", "mystic " + "runes",
            "f@g", "d1ck", "titanrift", "wynncraft",
            "titan rift", "jigga", "autism", "jiggaboo", "hitler", "jews",
            "fucked", "mckillzone", "niger", "kys"));

    public static boolean listened(Player player) {
        return chatListeners.containsKey(player) || orElseListeners.containsKey(player);
    }

    public static void listenForMessage(Player player, Consumer<? super AsyncPlayerChatEvent> consumer) {
        listenForMessage(player, consumer, (Runnable) null);
    }

    public static void listenForMessage(Player player, Consumer<? super AsyncPlayerChatEvent> consumer, Runnable fail) {
        listenForMessage(player, consumer, p -> {
            if (fail != null)
                fail.run();
        });
    }

    public static void listenForMessage(Player player, Consumer<? super AsyncPlayerChatEvent> consumer, Consumer<? super Player> orElse) {

        if (player.getOpenInventory() != null && !player.getOpenInventory().equals(player.getInventory()) && !player.getOpenInventory().getTitle().equals("container.crafting")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Daedwin.getInstance(), () -> player.closeInventory());
        }

        if (chatListeners.remove(player) != null) {
            Consumer<? super Player> old = orElseListeners.remove(player);
            if (old != null) {
                old.accept(player);
            }
        }

        if (consumer != null) {
            chatListeners.put(player, consumer);
            if (orElse != null) orElseListeners.put(player, orElse);
        }
    }

    public static void promptPlayerConfirmation(Player player, Runnable confirm, Runnable cancel) {
        listenForMessage(player, event -> {
            String message = event.getMessage();
            if (message.equalsIgnoreCase("confirm") || message.equalsIgnoreCase("yes") || message.equalsIgnoreCase("y") || message.equalsIgnoreCase("accept")) {
                confirm.run();
            } else if (message.equalsIgnoreCase("no") || message.equalsIgnoreCase("n") || message.equalsIgnoreCase("cancel") || message.equalsIgnoreCase("deny")) {
                cancel.run();
            } else {
                player.sendMessage(ChatColor.RED + "Unknown response.");
                cancel.run();
            }
        }, p -> cancel.run());
    }

    private static String toCensor(int characters) {
        String result = "";

        for (int i = 0; i < characters; i++)
            result = result.concat("*");
        return result;
    }


    public static String checkForBannedWords(String msg) {
        String result = msg;

        result = result.replace("ð", "");


        for (String word : bannedWords) result = replaceOperation(result, word);

        StringTokenizer st = new StringTokenizer(result);

        String string = "";

        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            for (String word : bannedWords)
                if (token.contains(word)) {
                    List<Integer> positions = new ArrayList<>();

                    for (int i = 0; i < token.length(); i++)
                        if (Character.isUpperCase(token.charAt(i))) positions.add(i);

                    if (token.toLowerCase().contains(word.toLowerCase())) {
                        token = token.toLowerCase().replaceAll(word.toLowerCase(), " " + toCensor(word.length()));
                    }

                    for (int i : positions)
                        if (i < token.length()) Character.toUpperCase(token.charAt(i));
                }
            string += token + " ";
        }
        return string.trim();
    }

    public static boolean containsBannedWords(String msg) {
        String result = msg;

        result = result.replace("ð", "");

        StringTokenizer st = new StringTokenizer(result);

        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            for (String word : bannedWords)
                if (token.toLowerCase().contains(word.toLowerCase())) {
                    return true;
                }
        }
        return false;
    }


    private static String replaceOperation(String source, String search) {
        int length = search.length();
        if (length < 2) return source;

        // - Ignore the same character mutliple times in a row
        // - Ignore any non-alphabetic characters
        // - Ignore any digits and whitespaces between characters
        StringBuilder sb = new StringBuilder(4 * length - 3);
        for (int i = 0; i < length - 1; i++) {
            sb.append("([\\W\\d]*").append(Pattern.quote("" + search.charAt(i))).append(")+");
        }
        sb.append("([\\W\\d\\s]*)+");
        sb.append(search.charAt(length - 1));

        String temp = source.replaceAll("(?i)" + sb.toString(), search).trim();
        int wordCount = temp.split("\\s").length;

        String replace = source;

        if (wordCount <= 2) {
            replace = " " + source;
        }

        return replace.replaceAll("(?i)" + sb.toString(), " " + search).trim();
    }

    public static boolean containsIllegal(String s) {
        //return s.matches("\\p{L}+") || s.matches("\\w+");
        //Probably have an array of allowed characters aswell.
        return !s.replace(" ", "").matches("[\\w\\Q!\"#$%&'()*çáéíóúâêôãõàüñ¿¡+,-./:;<=>?@[\\]^_`{|}~\\E]+");
    }
}
