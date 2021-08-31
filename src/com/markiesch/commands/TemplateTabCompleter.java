package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TemplateTabCompleter implements TabCompleter {
    private static final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> result = new ArrayList<>();

        if (args.length == 1) {
            result.add("delete");
            result.add("edit");
            result.add("create");
            return result;
        }

        if (args.length == 2) {
            List<String> templates = new ArrayList<>(Objects.requireNonNull(plugin.getTemplateStorage().getConfig().getConfigurationSection("")).getKeys(false));
            for (String template : templates) {
                if (template.toLowerCase().startsWith(args[1].toLowerCase())) {
                    result.add(template);
                }
            }
            return result;
        }

        return null;
    }
}
