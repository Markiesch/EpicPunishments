package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.PlayerStorage;
import com.markiesch.utils.TemplateStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class PunishTabCompleter {
    static FileConfiguration config = PlayerStorage.getConfig();

    public static List<String> onTabComplete(String[] args) {
        List<String> result = new ArrayList<>();

        if (args.length == 1) {
            result.add("reload");
            ConfigurationSection configSection = config.getConfigurationSection("");
            if (configSection == null) return result;
            List<String> players = new ArrayList<>();
            configSection.getKeys(false).forEach(uuid -> {
                String name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
                if (name != null && name.toLowerCase(Locale.US).startsWith(args[0].toLowerCase(Locale.US))) {
                    players.add(name);
                }
            });
            return players;
        }
        if (args.length == 2) {
            ConfigurationSection section = TemplateStorage.getConfig().getConfigurationSection("");
            if (section == null) return result;
            for (String template : section.getKeys(false)) {
                String templateName = section.getString(template + ".name");
                if (templateName != null && templateName.toLowerCase(Locale.US).startsWith(args[1].toLowerCase(Locale.US))) result.add(templateName);
            }
            return result;
        }

        return result;
    }
}
