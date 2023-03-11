package com.markiesch;

import org.jetbrains.annotations.NotNull;

public enum Permission {
    // Command permissions
    COMMAND_CLEARCHAT_EXECUTE("clear_chat.execute"),
    COMMAND_CLEARCHAT_BYPASS("clear_chat.bypass"),
    COMMAND_RANDOMPLAYER_EXECUTE("randomplayer.execute"),
    COMMAND_RANDOMPLAYER_EXEMPT("randomplayer.exempt"),
    IM_MUTED_EXECUTE("im_muted.execute"),

    // Punish permissions
    EXECUTE_BAN("ban.execute"),
    REVOKE_BAN("ban.revoke"),
    DELETE_BAN("ban.delete"),
    EXECUTE_MUTE("mute.execute"),
    REVOKE_MUTE("mute.revoke"),
    DELETE_MUTE("mute.delete"),
    EXECUTE_KICK("kick.execute"),
    DELETE_KICK("kick.delete"),
    EXECUTE_WARN("warn.execute"),
    REVOKE_WARN("warn.revoke"),
    REVOKE_WARNINGS("warn.revoke.all"),

    // Spy permissions
    SPY_COMMAND_BYPASS("spy.command.bypass"),
    SPY_COMMAND_NOTIFY("spy.command.notify"),
    SPY_SIGN_BYPASS("spy.sign.bypass"),
    SPY_SIGN_NOTIFY("spy.sign.notify"),
    SPY_NEW_NAME_NOTIFY("spy.new_name.notify"),

    ADMIN_RELOAD("admin.reload"),
    ADMIN_NOTIFY_NEW_VERSION("admin.new-version.notify"),
    ADMIN_HELP("admin.help"),
    EXECUTE_INFO("info.execute"),
    BROADCAST_RECEIVE("broadcast.receive"),

    MANAGE_PLAYERS("manage.players"),
    MANAGE_TEMPLATES("manage.templates"),
    MANAGE_CATEGORIES("manage.categories"),

    HISTORY_MENU("history.menu");

    private static final String prefix = "epicpunishments.";
    private final String node;

    Permission(@NotNull String node) {
        this.node = node;
    }

    public String getNode() {
        return prefix + node;
    }
}
