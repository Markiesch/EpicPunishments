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
import java.util.List;

public class BanMenuUtils {
    private static final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);

    public static void generatePlayerMeta(SkullMeta meta, OfflinePlayer player, ItemStack playerHead) {
        if (meta == null) return;
        if (player == null) return;
        if (player.getName() == null) return;
        if (player.getPlayer() == null) return;

        meta.setDisplayName("§b§l" + player.getName());

        ArrayList<String> lore = new ArrayList<>();

        Date date = new Date(player.getFirstPlayed());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);

        lore.add("§bLeft Click §7to manage player");
        lore.add("§bRight Click §7to teleport");
        lore.add("");

        List<String> infractions = plugin.getPlayerStorage().getPunishments(player.getUniqueId());

        if (infractions.size() < 1) {
            lore.add("§a✔ §7didn't received any punishments yet");
        } else {
            lore.add("§6✔ §7had received " + infractions.size() + " punishments");
        }

        if (plugin.getPlayerStorage().isPlayerBanned(player.getUniqueId())) {
            lore.add("§6✔ §7" + player.getName() + " is §abanned §7on §e" + plugin.getServer().getName());
        } else {
            lore.add("§a✔ §a" + player.getName() + " §7is not §ebanned");
        }

        lore.add("");
        lore.add("§7Joined at: " + formattedDate);

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
