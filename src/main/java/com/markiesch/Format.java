package com.markiesch;

import com.markiesch.modules.category.CategoryModel;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.infraction.PreparedInfraction;
import com.markiesch.modules.warning.WarningModel;
import com.markiesch.utils.TimeUtils;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public enum Format {
    KICK("formats.kick"),
    TEMPORARILY_MUTED("formats.temporarily_muted"),
    PERMANENTLY_MUTED("formats.permanently_muted"),
    TEMPORARILY_BANNED("formats.temporarily_banned"),
    PERMANENTLY_BANNED("formats.permanently_banned"),
    MESSAGE_WARNING("formats.message_warning"),
    REASON_WARNING("formats.reason_warning");

    private final String configPath;

    Format(String configPath) {
        this.configPath = configPath;
    }

    public String getString(WarningModel warning) {
        CategoryModel category = warning.getCategory();

        return getConfigValue()
            .replace("[reason]",
                warning.getReason() != null ?
                    warning.getReason() :
                    category == null ? "" : category.getName()
            )
            .replace("[message]", category == null || category.getMessage() == null ? "" : category.getMessage());
    }

    public @NotNull String getString(InfractionModel infractionModel) {
        return getString(
            infractionModel.getReason(),
            infractionModel.getFormattedDuration(),
            infractionModel.getIssuer(),
            TimeUtils.makeReadable(infractionModel.getTimeLeft())
        );
    }

    public @NotNull String getString(PreparedInfraction infractionModel) {
        return getString(
            infractionModel.reason,
            TimeUtils.makeReadable(infractionModel.duration),
            infractionModel.issuer.getName(),
            TimeUtils.makeReadable(infractionModel.duration)
        );
    }

    private @NotNull String getString(String reason, String duration, String issuer, String timeLeft) {
        return getConfigValue()
            .replace("[reason]", reason)
            .replace("[duration]", duration)
            .replace("[time_left]", timeLeft)
            .replace("[issuer]", issuer);
    }

    private String getConfigValue() {
        EpicPunishments plugin = EpicPunishments.getInstance();

        String configValue = plugin.getConfig().isString(configPath) ?
            plugin.getConfig().getString(configPath) :
            String.join("\n", plugin.getConfig().getStringList(configPath));

        if (configValue == null) return "";
        return ChatColor.translateAlternateColorCodes('&', configValue);
    }
}
