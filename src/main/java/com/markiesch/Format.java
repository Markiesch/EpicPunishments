package com.markiesch;

import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.infraction.PreparedInfraction;
import com.markiesch.utils.TimeUtils;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public enum Format {
    KICK("formats.kick"),
    TEMPORARILY_MUTED("formats.temporarily_muted"),
    PERMANENTLY_MUTED("formats.permanently_muted"),
    TEMPORARILY_BANNED("formats.temporarily_banned"),
    PERMANENTLY_BANNED("formats.permanently_banned");

    private final String configPath;

    Format(String configPath) {
        this.configPath = configPath;
    }

    public @NotNull String getString(InfractionModel infractionModel) {
        return getString(
                infractionModel.getReason(),
                infractionModel.getFormattedDuration(),
                infractionModel.getIssuer()
        );
    }

    public @NotNull String getString(PreparedInfraction infractionModel) {
        return getString(
                infractionModel.reason,
                TimeUtils.makeReadable(infractionModel.duration),
                infractionModel.issuer.getName()
        );
    }

    private String getString(String reason, String duration, String issuer) {
        EpicPunishments plugin = EpicPunishments.getInstance();

        String configValue = plugin.getConfig().isString(configPath) ?
                plugin.getConfig().getString(configPath) :
                String.join("\n", plugin.getConfig().getStringList(configPath));

        if (configValue == null) return "";

        return ChatColor.translateAlternateColorCodes('&', configValue
                .replace("[reason]", reason)
                .replace("[duration]", duration)
                .replace("[issuer]", issuer));
    }
}
