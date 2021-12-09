package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class InfractionTabCompleter {
    static EpicPunishments plugin = EpicPunishments.getInstance();
    static FileConfiguration config = plugin.getPlayerStorage().getConfig();

    public static List<String> onTabComplete(String[] args, boolean addTime) {
        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            List<String> players = new ArrayList<>();
            ConfigurationSection configSection = config.getConfigurationSection("");
            if (configSection == null) return result;
            configSection.getKeys(false).forEach(uuid -> {
                String name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
                if (name != null && name.toLowerCase(Locale.US).startsWith(args[0].toLowerCase(Locale.US)))
                    players.add(name);
            });
            return players;
        }

        if (args.length == 2 && addTime) {
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

        result.add("(reason)");

        return result;
    }
}
