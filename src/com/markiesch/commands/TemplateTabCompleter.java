package com.markiesch.commands;

import com.markiesch.utils.TemplateStorage;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TemplateTabCompleter {
    public static List<String> onTabComplete(String[] args) {
        List<String> result = new ArrayList<>();

        if (args.length == 1) {
            result.add("delete");
            result.add("edit");
            result.add("create");
            return result;
        }

        if (args.length == 2) {
            ConfigurationSection config = TemplateStorage.getConfig().getConfigurationSection("");
            if (config == null) return result;
            List<String> templates = new ArrayList<>(config.getKeys(false));
            for (String template : templates) {
                String templateName = TemplateStorage.getConfig().getString(template + ".name");
                if (templateName != null && templateName.toLowerCase(Locale.US).startsWith(args[1].toLowerCase(Locale.US))) result.add(templateName);
            }
            return result;
        }

        return result;
    }
}
