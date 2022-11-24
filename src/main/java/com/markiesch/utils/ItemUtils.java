package com.markiesch.utils;

import com.markiesch.modules.infraction.InfractionList;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.profile.ProfileController;
import com.markiesch.modules.profile.ProfileModel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.*;

public class ItemUtils {
    public static ItemStack createItem(Material material, String name, String... lore) {
        return createItem(material, name, 1, lore);
    }

    public static ItemStack createItem(Material material, String name, int amount, String... lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);

            // For removing "Effects" from banner pattern
            List<String> loreList = new ArrayList<>(Arrays.asList(lore));
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            meta.setLore(loreList);

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createPlayerInfoHead(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        Date date = new Date(player.getFirstPlayed());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String formattedDate = sdf.format(date);

        InfractionList infractionList = InfractionManager.getInstance().getPlayer(uuid);

        ItemStack playerHead = ItemUtils.createItem(
                Material.PLAYER_HEAD,
                "§b§l" + player.getName(),
                (infractionList.isEmpty() ? "§a✔ §7didn't received any punishments yet" : "§6✔ §7had received " + infractionList.size() + " punishments"),
                (infractionList.isBanned() ? "§6✔ §7" + player.getName() + " is§a banned" : "§a✔ §a" + player.getName() + " §7is not§e banned"),
                "",
                "§7Joined at: " + formattedDate);

        SkullMeta playerMeta = (SkullMeta) playerHead.getItemMeta();
        if (playerMeta != null) playerMeta.setOwningPlayer(player);
        playerHead.setItemMeta(playerMeta);

        return playerHead;
    }
}
