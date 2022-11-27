package com.markiesch.locale;

import com.markiesch.EpicPunishments;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum Translation {
    COMMAND_NO_PERMISSION("command.no_permission"),
    COMMAND_PLAYER_ONLY("command.player_only"),

    EVENT_KICK_SUCCESS("event.kick.success"),
    EVENT_KICK_OFFLINE("event.kick.offline"),
    EVENT_KICK_MESSAGE("event.kick.message"),

    EVENT_MUTE_SUCCESS("event.mute.success"),
    EVENT_MUTE_ALREADY("event.mute.already"),
    EVENT_MUTE_MESSAGE("event.mute.message"),

    EVENT_BAN_SUCCESS("event.ban.success"),
    EVENT_BAN_ALREADY("event.ban.already"),


    PREVIOUS_PAGE("menu.general.previous"),
    NEXT_PAGE("menu.general.next"),
    VISIT_PAGE("menu.general.visit"),
    MENU_NO_PERMISSION("menu.general.no_permission"),

    MENU_BACK_BUTTON_TITLE("menu.general.back_button.title"),
    MENU_BACK_BUTTON_LORE("menu.general.back_button.lore"),

    MENU_CLOSE_BUTTON_TITLE("menu.general.close_button.title"),
    MENU_CLOSE_BUTTON_LORE("menu.general.close_button.lore"),

    // Players menu
    MENU_PLAYERS_TITLE("menu.main.title"),

    MENU_PLAYERS_TEMPLATES_BUTTON_TITLE("menu.players.templates_button.title"),
    MENU_PLAYERS_TEMPLATES_BUTTON_LORE("menu.players.templates_button.lore"),

    MENU_PLAYERS_BUTTON_FILTER_TITLE("menu.players.filter_button.title"),
    MENU_PLAYERS_BUTTON_FILTER_LORE("menu.players.filter_button.lore"),

    MENU_PLAYERS_BUTTON_PLAYER_TITLE("menu.players.player_button.title"),
    MENU_PLAYERS_BUTTON_PLAYER_LORE("menu.players.player_button.lore"),

    MENU_PLAYERS_BUTTON_PLAYER_LORE_PUNISHMENTS_EMPTY("menu.players.player_button.punishments_empty"),
    MENU_PLAYERS_BUTTON_PLAYER_LORE_PUNISHMENTS_NOT_EMPTY("menu.players.player_button.punishments_not_empty"),

    // Template menu
    MENU_TEMPLATES_TITLE("menu.templates.title"),

    MENU_TEMPLATES_FILTER_TITLE("menu.templates.filter_button.title"),
    MENU_TEMPLATES_FILTER_LORE("menu.templates.filter_button.lore"),

    MENU_TEMPLATES_CREATE_BUTTON_TITLE("menu.templates.create_button.title"),
    MENU_TEMPLATES_CREATE_BUTTON_LORE("menu.templates.create_button.lore"),

    MENU_TEMPLATES_TEMPLATE_BUTTON_TITLE("menu.templates.template_button.title"),
    MENU_TEMPLATES_TEMPLATE_BUTTON_LORE("menu.templates.template_button.lore"),

    MENU_TEMPLATES_EMPTY_TITLE("menu.templates.no_templates_item.title"),
    MENU_TEMPLATES_EMPTY_LORE("menu.templates.no_templates_item.lore");



    private final String path;
    private final Map<String, Object> placeholders;

    Translation(String configPath) {
        path = configPath;
        placeholders = new HashMap<>();
    }

    @Override
    public String toString() {
        String message = getRawString();

        if (message == null) return "";
        return applyPlaceholders(ChatColor.translateAlternateColorCodes('&', message));
    }

    public String getRawString() {
        return EpicPunishments.getLangConfig().getString(path);
    }

    public List<String> toList() {
        List<String> messages = EpicPunishments.getLangConfig().getStringList(path);

        if (messages == null) return new ArrayList<>();

        return messages
                .stream()
                .map(message -> applyPlaceholders(ChatColor.translateAlternateColorCodes('&', message)))
                .collect(Collectors.toList());
    }

    public Translation addPlaceholder(String placeholder, Object value) {
        placeholders.put("[" + placeholder + "]", value);
        return this;
    }

    private String applyPlaceholders(String string) {
        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            string = string.replace(entry.getKey(), entry.getValue().toString());
        }
        return string;
    }
}
