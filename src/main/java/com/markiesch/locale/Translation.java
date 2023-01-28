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
    // Words
    WORD_ONLINE("word.online"),
    WORD_OFFLINE("word.offline"),
    WORD_ALL("word.all"),
    WORD_ACTIVE("word.active"),
    WORD_EXPIRED("word.expired"),
    WORD_REVOKED("word.revoked"),
    WORD_BANNED("word.banned"),
    WORD_MUTED("word.muted"),
    WORD_KICKED("word.kicked"),
    WORD_WARNED("word.warned"),

    // Commands
    COMMAND_NO_PERMISSION("command.no_permission"),
    COMMAND_PLAYER_ONLY("command.player_only"),
    COMMAND_PLAYER_NOT_FOUND("command.player_not_found"),
    COMMAND_UNBAN_NOT_BANNED("command.unban.not_banned"),
    COMMAND_UNBAN_SUCCESS("command.unban.success"),
    COMMAND_UNMUTE_NOT_MUTED("command.unmute.not_muted"),
    COMMAND_UNMUTE_SUCCESS("command.unmute.success"),
    COMMAND_RANDOM_PLAYER_NO_PLAYERS("command.random_player.no_players"),
    COMMAND_RANDOM_PLAYER_SUCCESS("command.random_player.success"),
    COMMAND_CLEAR_CHAT_SUCCESS("command.clear_chat.success"),
    COMMAND_CLEAR_CHAT_BROADCAST("command.clear_chat.broadcast"),
    COMMAND_INFO_CONTENT("command.info.content"),
    COMMAND_INFO_CONTENT_EMPTY("command.info.empty"),
    COMMAND_INFO_CONTENT_FILLED("command.info.filled"),
    COMMAND_IM_MUTED_NOT_MUTED("command.im_muted.not_muted"),
    COMMAND_IM_MUTED_SELF("command.im_muted.self"),
    COMMAND_IM_MUTED_MESSAGE("command.im_muted.message"),
    COMMAND_IM_MUTED_SUCCESS("command.im_muted.success"),

    // Events
    EVENT_COMMAND_SPY("event.command_spy"),
    EVENT_SIGN_SPY("event.sign_spy"),
    EVENT_NEW_NAME_SPY("event.new_name_spy"),
    EVENT_KICK_SUCCESS("event.kick.success"),
    EVENT_KICK_OFFLINE("event.kick.offline"),
    EVENT_MUTE_SUCCESS("event.mute.success"),
    EVENT_MUTE_ALREADY("event.mute.already"),
    EVENT_BAN_SUCCESS("event.ban.success"),
    EVENT_BAN_ALREADY("event.ban.already"),
    EVENT_WARN_SUCCESS("event.warn.success"),
    BROADCAST_PERMANENTLY("event.broadcast.permanently"),
    BROADCAST_TEMPORARILY("event.broadcast.temporarily"),

    // General menu
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
    MENU_PLAYERS_BUTTON_NAME_FILTER_TITLE("menu.players.name_filter_button.title"),
    MENU_PLAYERS_BUTTON_NAME_FILTER_LORE("menu.players.name_filter_button.lore"),
    MENU_PLAYERS_INSERT_NAME_FILTER_TITLE("menu.players.search_players.title"),
    MENU_PLAYERS_INSERT_NAME_FILTER_SUBTITLE("menu.players.search_players.subtitle"),
    MENU_PLAYERS_BUTTON_PLAYER_TITLE("menu.players.player_button.title"),
    MENU_PLAYERS_BUTTON_PLAYER_LORE("menu.players.player_button.lore"),
    MENU_PLAYERS_BUTTON_PLAYER_LORE_PUNISHMENTS_EMPTY("menu.players.player_button.punishments_empty"),
    MENU_PLAYERS_BUTTON_PLAYER_LORE_PUNISHMENTS_NOT_EMPTY("menu.players.player_button.punishments_not_empty"),

    // Player menu
    MENU_PLAYER_TITLE("menu.player.title"),
    MENU_PLAYER_NOT_FOUND("menu.player.not_found"),
    MENU_PLAYER_DETAILS_TITLE("menu.player.player_info.title"),
    MENU_PLAYER_DETAILS_LORE("menu.player.player_info.lore"),
    MENU_PLAYER_PUNISH_TITLE("menu.player.punish_button.title"),
    MENU_PLAYER_PUNISH_LORE("menu.player.punish_button.lore"),
    MENU_PLAYER_INFRACTIONS_TITLE("menu.player.infractions_button.title"),
    MENU_PLAYER_INFRACTIONS_LORE("menu.player.infractions_button.lore"),
    MENU_PLAYER_WARNINGS_TITLE("menu.player.warnings_button.title"),
    MENU_PLAYER_WARNINGS_LORE("menu.player.warnings_button.lore"),
    MENU_PLAYER_IP_MATCHES_TITLE("menu.player.ip_matches_button.title"),
    MENU_PLAYER_IP_MATCHES_LORE("menu.player.ip_matches_button.lore"),
    MENU_PLAYER_IP_MATCHES_FORMAT("menu.player.ip_matches_button.format"),
    MENU_PLAYER_IP_MATCHES_EMPTY("menu.player.ip_matches_button.empty"),

    // Punish menu
    MENU_PUNISH_TITLE("menu.punish.title"),
    MENU_PUNISH_BUTTON_REASON_TITLE("menu.punish.reason_button.title"),
    MENU_PUNISH_BUTTON_REASON_LORE("menu.punish.reason_button.lore"),
    MENU_PUNISH_INSERT_REASON_TITLE("menu.punish.insert_reason.title"),
    MENU_PUNISH_INSERT_REASON_SUBTITLE("menu.punish.insert_reason.subtitle"),
    MENU_PUNISH_BUTTON_TYPE_TITLE("menu.punish.type_button.title"),
    MENU_PUNISH_BUTTON_TYPE_LORE("menu.punish.type_button.lore"),
    MENU_PUNISH_BUTTON_DURATION_TITLE("menu.punish.duration_button.title"),
    MENU_PUNISH_BUTTON_DURATION_LORE("menu.punish.duration_button.lore"),
    MENU_PUNISH_INSERT_DURATION_TITLE("menu.punish.insert_duration.title"),
    MENU_PUNISH_INSERT_DURATION_SUBTITLE("menu.punish.insert_duration.subtitle"),
    MENU_PUNISH_BUTTON_TEMPLATE_TITLE("menu.punish.template_button.title"),
    MENU_PUNISH_BUTTON_TEMPLATE_LORE("menu.punish.template_button.lore"),
    MENU_PUNISH_BUTTON_CONFIRM_TITLE("menu.punish.confirm_button.title"),
    MENU_PUNISH_BUTTON_CONFIRM_LORE("menu.punish.confirm_button.lore"),

    // Warnings menu
    MENU_WARNINGS_TITLE("menu.warnings.title"),
    MENU_WARNINGS_WARNING_TITLE("menu.warnings.warning_button.title"),
    MENU_WARNINGS_WARNING_LORE("menu.warnings.warning_button.lore"),

    // Infractions menu
    MENU_INFRACTIONS_TITLE("menu.infractions.title"),
    MENU_INFRACTION_PLAYER_TITLE("menu.infractions.player_info.title"),
    MENU_INFRACTION_PLAYER_LORE("menu.infractions.player_info.lore"),
    MENU_INFRACTION_PLAYER_EMPTY("menu.infractions.player_info.empty"),
    MENU_INFRACTION_PLAYER_FILLED("menu.infractions.player_info.filled"),
    MENU_INFRACTIONS_BUTTON_TITLE("menu.infractions.infraction_button.title"),
    MENU_INFRACTIONS_BUTTON_LORE("menu.infractions.infraction_button.lore"),

    // Select template menu
    MENU_SELECT_TEMPLATE_TITLE("menu.select_template.title"),
    MENU_SELECT_TEMPLATE_BUTTON_TITLE("menu.select_template.template_button.title"),
    MENU_SELECT_TEMPLATE_BUTTON_LORE("menu.select_template.template_button.lore"),

    // Templates menu
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

    // Edit template menu
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
    MENU_EDIT_TEMPLATE_SUCCESS("menu.edit_template.success"),

    // Categories menu
    MENU_CATEGORIES_TITLE("menu.categories.title"),
    MENU_CATEGORIES_MODEL_TITLE("menu.categories.category_button.title"),
    MENU_CATEGORIES_MODEL_LORE("menu.categories.category_button.lore"),
    MENU_CATEGORIES_CREATE_TITLE("menu.categories.create.title"),
    MENU_CATEGORIES_CREATE_LORE("menu.categories.create.lore"),

    // Category menu
    MENU_CATEGORY_TITLE("menu.category.title"),
    MENU_CATEGORY_INSERT_NAME_TITLE("menu.category.insert_name.title"),
    MENU_CATEGORY_INSERT_NAME_SUBTITLE("menu.category.insert_name.subtitle"),
    MENU_CATEGORY_INSERT_RULE_COUNT_TITLE("menu.category.insert_rule_count.title"),
    MENU_CATEGORY_INSERT_RULE_COUNT_SUBTITLE("menu.category.insert_rule_count.subtitle"),
    MENU_CATEGORY_INFO_TITLE("menu.category.info_button.title"),
    MENU_CATEGORY_INFO_LORE("menu.category.info_button.lore"),
    MENU_CATEGORY_RULE_TITLE("menu.category.rule_button.title"),
    MENU_CATEGORY_RULE_LORE("menu.category.rule_button.lore"),
    MENU_CATEGORY_EMPTY_TITLE("menu.category.rules_empty.title"),
    MENU_CATEGORY_EMPTY_LORE("menu.category.rules_empty.lore"),
    MENU_CATEGORY_CREATE_RULE_TITLE("menu.category.create_rule.title"),
    MENU_CATEGORY_CREATE_RULE_LORE("menu.category.create_rule.lore"),
    ;

    private final String path;
    private final Map<String, @Nullable Object> placeholders;
    private final Map<String, List<String>> listPlaceholders;

    Translation(String configPath) {
        path = configPath;
        placeholders = new HashMap<>();
        listPlaceholders = new HashMap<>();
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

        return applyPlaceholders(messages);
    }

    public Translation addListPlaceholder(String placeholder, List<String> value) {
        listPlaceholders.put("[" + placeholder + "]", value);
        return this;
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

    private List<String> applyPlaceholders(List<String> content) {
        for (Map.Entry<String, List<String>> entry : listPlaceholders.entrySet()) {
            final String placeholder = entry.getKey();

            if (content.contains(placeholder)) {
                int index = content.indexOf(placeholder);
                content.remove(placeholder);
                content.addAll(index, entry.getValue());
            }
        }

        return content.stream()
                .map(message -> applyPlaceholders(ChatColor.translateAlternateColorCodes('&', message)))
                .collect(Collectors.toList());
    }
}
