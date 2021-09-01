package com.markiesch.utils;

import com.markiesch.EpicPunishments;
import org.bukkit.Material;

public class BanMenuUtils {
    private static final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);

    public static Material getConfigItem(String path, String defaultItem) {
        String itemName = plugin.getConfig().getString(path);
        if (itemName == null || Material.matchMaterial(itemName) == null) itemName = defaultItem;
        return Material.matchMaterial(itemName.toUpperCase());
    }

    public static String getConfigItemName(String path, String defaultName) {
        String itemName = plugin.getConfig().getString(path);
        if (itemName == null) itemName = defaultName;
        return plugin.changeColor(itemName);
    }
}
