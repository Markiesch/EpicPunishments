package com.markiesch.utils;

import org.bukkit.ChatColor;

public abstract class ChatUtils {
    public static String changeColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
