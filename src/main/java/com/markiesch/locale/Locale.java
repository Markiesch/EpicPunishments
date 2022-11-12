package com.markiesch.locale;

import com.markiesch.EpicPunishments;
import org.bukkit.ChatColor;

public enum Locale {
    PREVIOUS_PAGE("menu.general.previous"),
    NEXT_PAGE("menu.general.next"),
    VISIT_PAGE("menu.general.visit"),
    PLAYER_SELECTOR_TITLE("menu.main.title");

    private final String path;

    Locale(String configPath) {
        path = configPath;
    }

    @Override
    public String toString() {
        String message = EpicPunishments.getLangConfig().getString(path);

        if (message == null) return "";
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
