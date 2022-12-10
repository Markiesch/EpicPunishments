package com.markiesch.locale;

import com.markiesch.EpicPunishments;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum Translation {
    WORD_ONLINE("word.online"),
    WORD_OFFLINE("word.offline"),
    WORD_ALL("word.all"),

    COMMAND_NO_PERMISSION("command.no_permission"),
    COMMAND_PLAYER_ONLY("command.player_only"),
    COMMAND_PLAYER_NOT_FOUND("command.player_not_found"),

    COMMAND_UNBAN_NOT_BANNED("command.unban.not_banned"),
    COMMAND_UNBAN_SUCCESS("command.unban.success"),

    COMMAND_UNMUTE_NOT_MUTED("command.unmute.not_muted"),
    COMMAND_UNMUTE_SUCCESS("command.unmute.success"),

    EVENT_COMMAND_SPY("event.command_spy"),
    EVENT_SIGN_SPY("event.sign_spy"),

    EVENT_KICK_SUCCESS("event.kick.success"),
    EVENT_KICK_OFFLINE("event.kick.offline"),
    EVENT_KICK_MESSAGE("event.kick.message"),

    EVENT_MUTE_SUCCESS("event.mute.success"),
    EVENT_MUTE_ALREADY("event.mute.already"),
    EVENT_MUTE_TEMPORARILY_MESSAGE("event.mute.temporarily_message"),
    EVENT_MUTE_PERMANENTLY_MESSAGE("event.mute.permanently_message"),

    EVENT_BAN_SUCCESS("event.ban.success"),
    EVENT_BAN_ALREADY("event.ban.already"),


    EVENT_BAN_TEMPORARILY_MESSAGE("event.ban.temporarily_message"),
    EVENT_BAN_PERMANENTLY_MESSAGE("event.ban.permanently_message"),

    PREVIOUS_PAGE("menu.general.previous"),
    NEXT_PAGE("menu.general.next"),
    VISIT_PAGE("menu.general.visit"),
    MENU_NO_PERMISSION("menu.general.no_permission"),

    MENU_BACK_BUTTON_TITLE("menu.general.back_button.title"),
    MENU_BACK_BUTTON_LORE("menu.general.back_button.lore"),

    MENU_CLOSE_BUTTON_TITLE("menu.general.close_button.title"),
    MENU_CLOSE_BUTTON_LORE("menu.general.close_button.lore"),

    // Players menu
    MENU_PLAYERS_TITLE("menu.players.title"),

    MENU_PLAYERS_TEMPLATES_BUTTON_TITLE("menu.players.templates_button.title"),
    MENU_PLAYERS_TEMPLATES_BUTTON_LORE("menu.players.templates_button.lore"),

    MENU_PLAYERS_BUTTON_FILTER_TITLE("menu.players.filter_button.title"),
    MENU_PLAYERS_BUTTON_FILTER_LORE("menu.players.filter_button.lore"),

    MENU_PLAYERS_BUTTON_PLAYER_TITLE("menu.players.player_button.title"),
    MENU_PLAYERS_BUTTON_PLAYER_LORE("menu.players.player_button.lore"),

    MENU_PLAYERS_BUTTON_PLAYER_LORE_PUNISHMENTS_EMPTY("menu.players.player_button.punishments_empty"),
    MENU_PLAYERS_BUTTON_PLAYER_LORE_PUNISHMENTS_NOT_EMPTY("menu.players.player_button.punishments_not_empty"),

    // Player menu
    MENU_PLAYER_TITLE("menu.player.title"),
    MENU_PLAYER_NOT_FOUND("menu.player.not_found"),

    MENU_PLAYER_TELEPORT_TITLE("menu.player.teleport_button.title"),
    MENU_PLAYER_TELEPORT_LORE("menu.player.teleport_button.lore"),

    MENU_PLAYER_PUNISH_TITLE("menu.player.punish_button.title"),
    MENU_PLAYER_PUNISH_LORE("menu.player.punish_button.lore"),

    MENU_PLAYER_INFRACTIONS_TITLE("menu.player.infractions_button.title"),
    MENU_PLAYER_INFRACTIONS_LORE("menu.player.infractions_button.lore"),

    // Punish menu
    MENU_PUNISH_TITLE("menu.punish.title"),
    MENU_PUNISH_BUTTON_REASON_TITLE("menu.punish.reason_button.title"),
    MENU_PUNISH_BUTTON_REASON_LORE("menu.punish.reason_button.lore"),
    MENU_PUNISH_BUTTON_TYPE_TITLE("menu.punish.type_button.title"),
    MENU_PUNISH_BUTTON_TYPE_LORE("menu.punish.type_button.lore"),
    MENU_PUNISH_BUTTON_DURATION_TITLE("menu.punish.duration_button.title"),
    MENU_PUNISH_BUTTON_DURATION_LORE("menu.punish.duration_button.lore"),
    MENU_PUNISH_BUTTON_TEMPLATE_TITLE("menu.punish.template_button.title"),
    MENU_PUNISH_BUTTON_TEMPLATE_LORE("menu.punish.template_button.lore"),
    MENU_PUNISH_BUTTON_CONFIRM_TITLE("menu.punish.confirm_button.title"),
    MENU_PUNISH_BUTTON_CONFIRM_LORE("menu.punish.confirm_button.lore"),

    // Infractions menu
    MENU_INFRACTIONS_TITLE("menu.infractions.title"),
    MENU_INFRACTIONS_BUTTON_TITLE("menu.infractions.infraction_button.title"),
    MENU_INFRACTIONS_BUTTON_LORE("menu.infractions.infraction_button.lore"),

    // Select template menu
    MENU_SELECT_TEMPLATE_TITLE("menu.select_template.title"),
    MENU_SELECT_TEMPLATE_BUTTON_TITLE("menu.select_template.template_button.title"),
    MENU_SELECT_TEMPLATE_BUTTON_LORE("menu.select_template.template_button.lore"),

    // Template menu
    MENU_TEMPLATES_TITLE("menu.templates.title"),

    MENU_TEMPLATES_FILTER_TITLE("menu.templates.filter_button.title"),
    MENU_TEMPLATES_FILTER_LORE("menu.templates.filter_button.lore"),

    MENU_TEMPLATES_SEARCH_TITLE("menu.templates.search_by_name.title"),
    MENU_TEMPLATES_SEARCH_SUBTITLE("menu.templates.search_by_name.subtitle"),

    MENU_TEMPLATES_CREATE_BUTTON_TITLE("menu.templates.create_button.title"),
    MENU_TEMPLATES_CREATE_BUTTON_LORE("menu.templates.create_button.lore"),

    MENU_TEMPLATES_TEMPLATE_BUTTON_TITLE("menu.templates.template_button.title"),
    MENU_TEMPLATES_TEMPLATE_BUTTON_LORE("menu.templates.template_button.lore"),

    MENU_TEMPLATES_EMPTY_TITLE("menu.templates.no_templates_item.title"),
    MENU_TEMPLATES_EMPTY_LORE("menu.templates.no_templates_item.lore"),

    MENU_TEMPLATES_CREATE_SUCCESS("menu.create_template.success"),
    MENU_TEMPLATES_CREATE_TITLE("menu.create_template.title"),
    MENU_TEMPLATES_CREATE_SUBTITLE("menu.create_template.subtitle"),

    MENU_EDIT_TEMPLATE_TITLE("menu.edit_template.title"),

    MENU_EDIT_TEMPLATE_CONFIRM_TITLE("menu.edit_template.confirm_button.title"),
    MENU_EDIT_TEMPLATE_CONFIRM_LORE("menu.edit_template.confirm_button.lore"),

    MENU_EDIT_TEMPLATE_NAME_BUTTON_TITLE("menu.edit_template.name_button.title"),
    MENU_EDIT_TEMPLATE_NAME_BUTTON_LORE("menu.edit_template.name_button.lore"),

    MENU_EDIT_TEMPLATE_TYPE_BUTTON_TITLE("menu.edit_template.type_button.title"),
    MENU_EDIT_TEMPLATE_TYPE_BUTTON_LORE("menu.edit_template.type_button.lore"),

    MENU_EDIT_TEMPLATE_TIME_BUTTON_TITLE("menu.edit_template.time_button.title"),
    MENU_EDIT_TEMPLATE_TIME_BUTTON_LORE("menu.edit_template.time_button.lore"),

    MENU_EDIT_TEMPLATE_REASON_BUTTON_TITLE("menu.edit_template.reason_button.title"),
    MENU_EDIT_TEMPLATE_REASON_BUTTON_LORE("menu.edit_template.reason_button.lore"),

    MENU_EDIT_TEMPLATE_INSERT_NAME_TITLE("menu.edit_template.insert_name.title"),
    MENU_EDIT_TEMPLATE_INSERT_NAME_SUBTITLE("menu.edit_template.insert_name.subtitle"),

    MENU_EDIT_TEMPLATE_INSERT_REASON_TITLE("menu.edit_template.insert_reason.title"),
    MENU_EDIT_TEMPLATE_INSERT_REASON_SUBTITLE("menu.edit_template.insert_reason.subtitle"),

    MENU_EDIT_TEMPLATE_INSERT_DURATION_TITLE("menu.edit_template.insert_duration.title"),
    MENU_EDIT_TEMPLATE_INSERT_DURATION_SUBTITLE("menu.edit_template.insert_duration.subtitle"),
    MENU_EDIT_TEMPLATE_INSERT_DURATION_INFO("menu.edit_template.insert_duration.info"),

    MENU_EDIT_TEMPLATE_SUCCESS("menu.edit_template.success");


    private final String path;
    private final Map<String, @Nullable Object> placeholders;

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

    public Translation addPlaceholder(String placeholder, @Nullable Object value) {
        placeholders.put("[" + placeholder + "]", value);
        return this;
    }

    private String applyPlaceholders(String string) {
        for (Map.Entry<String, @Nullable Object> entry : placeholders.entrySet()) {
            string = string.replace(entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString());
        }
        return string;
    }
}
