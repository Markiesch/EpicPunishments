package com.markiesch.utils;

import org.bukkit.ChatColor;

public final class ChatUtils {
    private ChatUtils() {}

    public static String changeColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
