package com.markiesch;

import org.jetbrains.annotations.NotNull;

public enum Permission {
    EXECUTE_BAN("ban.execute"),
    REVOKE_BAN("ban.revoke"),
    DELETE_BAN("ban.delete"),

    EXECUTE_MUTE("mute.execute"),
    REVOKE_MUTE("mute.revoke"),
    DELETE_MUTE("mute.delete"),

    EXECUTE_KICK("kick.execute"),
    DELETE_KICK("kick.delete"),

    MANAGE_PLAYERS("manage.players"),
    MANAGE_TEMPLATES("manage.templates"),

    SPY_COMMAND_BYPASS("spy.command.bypass"),
    SPY_COMMAND_NOTIFY("spy.command.notify"),

    SPY_SIGN_BYPASS("spy.sign.bypass"),
    SPY_SIGN_NOTIFY("spy.sign.notify"),

    SPY_NEW_NAME_NOTIFY("spy.new_name.notify"),

    RANDOM_PLAYER("randomplayer"),
    RANDOM_PLAYER_EXEMPT("randomplayer.exempt");

    private static final String prefix = "epicpunishments.";
    private final String node;

    Permission(@NotNull String node) {
        this.node = node;
    }

    public String getNode() {
        return prefix + node;
    }
}
