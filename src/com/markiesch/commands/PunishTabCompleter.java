package com.markiesch.commands;

import com.markiesch.EpicPunishments;
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
    static EpicPunishments plugin = EpicPunishments.getInstance();
    static FileConfiguration config = plugin.getPlayerStorage().getConfig();

    public static List<String> onTabComplete(CommandSender sender, String[] args) {
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
            List<String> templates = new ArrayList<>(Objects.requireNonNull(plugin.getTemplateStorage().getConfig().getConfigurationSection("")).getKeys(false));
            for (String template : templates)
                if (template.toLowerCase(Locale.US).startsWith(args[1].toLowerCase(Locale.US))) result.add(template);
            return result;
        }

        return result;
    }
}
