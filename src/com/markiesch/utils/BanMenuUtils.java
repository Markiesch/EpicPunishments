package com.markiesch.utils;

import com.markiesch.EpicPunishments;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BanMenuUtils {
    private static final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);

    public static void generatePlayerMeta(SkullMeta meta, OfflinePlayer player, ItemStack playerHead) {
        if (meta == null) return;
        if (player == null) return;
        if (player.getName() == null) return;
        if (player.getPlayer() == null) return;

        meta.setDisplayName(plugin.changeColor(plugin.getConfig().getString("mainMenu.headName").replace("[playerName]", player.getName())));
        ArrayList<String> lore = new ArrayList<>();

        Date date = new Date(player.getFirstPlayed());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);

        if (player.getName() == null) return;

        for (String i : plugin.getConfig().getStringList("mainMenu.headLore")) {
            lore.add(plugin.changeColor(i
                    .replace("[playerName]", player.getName())
                    .replace("[playerDisplayName]", player.getPlayer().getDisplayName())
                    .replace("[playerUUID]", player.getUniqueId().toString())
                    .replace("[playerFirstJoin]", formattedDate)
            ));
        }
        meta.setLore(lore);
        meta.setOwningPlayer(player);
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "uuid"), PersistentDataType.STRING, player.getUniqueId().toString());
        playerHead.setItemMeta(meta);
    }

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
