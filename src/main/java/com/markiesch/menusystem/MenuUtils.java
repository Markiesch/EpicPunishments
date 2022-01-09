package com.markiesch.menusystem;

import org.bukkit.Material;

public class MenuUtils {
    public static Material getMaterialType(String currentType) {
        if ("BAN".equalsIgnoreCase(currentType)) return Material.OAK_DOOR;
        if ("KICK".equalsIgnoreCase(currentType)) return Material.ENDER_EYE;
        if ("MUTE".equalsIgnoreCase(currentType)) return Material.STRING;
        return Material.PAPER;
    }
}
