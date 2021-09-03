package com.markiesch.utils;

import com.markiesch.EpicPunishments;

public class BanMenuUtils {
    private static final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);

    public static String getConfigItemName(String path, String defaultName) {
        String itemName = plugin.getConfig().getString(path);
        if (itemName == null) itemName = defaultName;
        return plugin.changeColor(itemName);
    }
}
