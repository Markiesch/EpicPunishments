package com.markiesch.utils;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemUtils {
    public static ItemStack createItem(Material material, String name, String... lore) {
        return createItem(material, name, 1, lore);
    }

    public static ItemStack createItem(Material material, String name, List<String> lore) {
        return createItem(material, name, 1, lore);
    }

    public static ItemStack createItem(Material material, String name, int amount, String... lore) {
        return createItem(material, name, amount, new ArrayList<>(Arrays.asList(lore)));
    }

    public static ItemStack createItem(Material material, String name, int amount, List<String> lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);

            // For removing "Effects" from banner pattern
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            meta.setLore(lore);

            item.setItemMeta(meta);
        }

        return item;
    }

    public static void setSkullOwner(ItemStack item, OfflinePlayer owner) {
        if (item.getType() != Material.PLAYER_HEAD) return;

        SkullMeta playerMeta = (SkullMeta) item.getItemMeta();
        if (playerMeta != null) {
            playerMeta.setOwningPlayer(owner);
            item.setItemMeta(playerMeta);
        }
    }
}
