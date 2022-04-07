package com.markiesch.utils;

import com.markiesch.EpicPunishments;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public enum Translation {
    PLAYER_SELECTOR_TITLE("menus.playerSelector.title"),
    PLAYER_SELECTOR_HEAD_NAME("menus.playerSelector.headName"),
    PUNISH_TITLE("menus.punish.title"),
    TEMPLATES_TITLE("menus.templates.title");

    Translation(String configString) { path = configString; }

    private final String path;

    public String format() {
        String string = EpicPunishments.getLangConfig().getString(path);
        if (string == null) return "";

        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public List<String> formatList() {
        List<String> lines = new ArrayList<>();
        for (String string : EpicPunishments.getLangConfig().getConfig().getStringList(path)) lines.add(ChatColor.translateAlternateColorCodes('&', string));
        return lines;
    }
}
