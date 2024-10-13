package ru.emrass.bossapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtil {
    public static String prefix = "§bServer §a> ";

    public static void sendMessage(String prefix, Player player, String message, Object... objects){
        player.sendMessage(alt(prefix) + alt(message,objects));
    }

    public static void sendMessage(Player player, String message, Object... objects){
        player.sendMessage(alt(prefix) + alt(message,objects));
    }
    public static void sendMessage(CommandSender sender, String message, Object... objects){
        sender.sendMessage(alt(prefix) + alt(message,objects));
    }

    public static void broad(String message){
        Bukkit.broadcastMessage(alt(message));
    }
    public static String alt(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static String replace(String message) {
        return message.replace('&', '§');
    }
    public static String alt(String message, Object... args) {
        return args != null && args.length != 0 ? alt(String.format(message, args)) : alt(message);
    }
    public String strip(String message){
        return ChatColor.stripColor(message);
    }
    public static String pc(String server, String message) {
        return alt("&8[&a%s87] &e%s", server, message);
    }

}
