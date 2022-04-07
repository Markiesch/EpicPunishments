package com.markiesch.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemUtils {
    public static ItemStack createItem(Material material, String name, String... lore) {
        return createItem(material, name, 1, lore);
    }

//    public static ItemStack createItem(Material material, String name, List<String>... lore) {
//        return createItem(material, name, 1, lore);
//    }

    public static ItemStack createItem(Material material, String name, int amount, String... lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        if (meta == null) return item;

        List<String> loreList = new ArrayList<>(Arrays.asList(lore));
        // For removing "Effects" from banner pattern
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setLore(loreList);
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
