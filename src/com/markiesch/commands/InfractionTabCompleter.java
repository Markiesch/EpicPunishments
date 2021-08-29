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
import java.util.UUID;
import java.util.regex.Pattern;

public class InfractionTabCompleter implements TabCompleter {
    private final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);
    FileConfiguration config = plugin.getPlayerStorage().getConfig();

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> result = new ArrayList<>();

        if (args.length == 1) {
            List<String> players = new ArrayList<>();
            ConfigurationSection configSection = config.getConfigurationSection("");
            if (configSection == null) return null;
            configSection.getKeys(false).forEach(uuid -> {
                String name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
                if (name != null && name.toLowerCase().startsWith(args[0].toLowerCase()))
                    players.add(name);
            });
            return players;
        }

        if (args.length == 2) {
            if (args[1].length() > 0) {
                result.add(args[1] + "s");
                result.add(args[1] + "m");
                result.add(args[1] + "h");
                result.add(args[1] + "d");
            } else {
                result.add(args[1] + "12h");
                result.add(args[1] + "30d");
                result.add("Permanent");
            }
            return result;
        }

        return null;
    }
}
