package com.markiesch.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

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

    public static void setSkullTexture(ItemStack itemStack, String url) {
        if (url == null || url.equals("null") || !itemStack.hasItemMeta() || !(itemStack.getItemMeta() instanceof SkullMeta)) return;

        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder()
                .encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
        itemStack.setItemMeta(skullMeta);
    }
}
