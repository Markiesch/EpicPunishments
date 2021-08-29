package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PunishTabCompleter implements TabCompleter {
    private static final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);
    FileConfiguration config = plugin.getPlayerStorage().getConfig();
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> result = new ArrayList<>();

        if (label.equalsIgnoreCase("punish")) {
            if (args.length == 1) {
                List<String> players = new ArrayList<>();
                ConfigurationSection configSection = config.getConfigurationSection("");
                if (configSection == null) return null;
                configSection.getKeys(false).forEach(uuid -> {
                    String name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
                    if (name != null && name.toLowerCase().startsWith(args[0].toLowerCase())) {
                        players.add(name);
                    }
                });
                return players;
            }
            if (args.length == 2) {
                List<String> templates = new ArrayList<>(Objects.requireNonNull(plugin.getConfig().getConfigurationSection("templates")).getKeys(false));
                for (String template : templates) {
                    if (template.toLowerCase().startsWith(args[1].toLowerCase())) {
                        result.add(template);
                    }
                }
                return result;
            }
        }

        if (args.length == 1) {
            result.add("templates");
            return result;
        }

        if (args[0].equalsIgnoreCase("templates")) {
            result.add("create");
            result.add("delete");

            return result;
        }
        return null;
    }
}
