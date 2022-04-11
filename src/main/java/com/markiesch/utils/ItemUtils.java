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

//    public static ItemStack createPlayerHead(PlayerModel playerModel) {
//        EpicPunishments plugin = EpicPunishments.getInstance();
//
//        List<InfractionModel> infractionsList = playerModel.getInfractions();
//        OfflinePlayer player = playerModel.getPlayer();
//
//        long date = player.getFirstPlayed();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
//        String formattedDate = sdf.format(date);
//
//        ItemStack playerHead = createItem(
//                Material.PLAYER_HEAD,
//                "§9§l" + player.getName(),
//                "§bLeft Click §7to manage player",
//                "§bRight Click §7to teleport",
//                "",
//                (infractionsList.isEmpty() ? "§a✔ §7didn't received any punishments yet" : "§6✔ §7had received " + infractionsList.size() + " punishments"),
//                (playerModel.isBanned() ? "§6✔ §7" + player.getName() + " is§a banned §7on §e" + plugin.getServer().getName() : "§a✔ §a" + player.getName() + " §7is not§e banned"),
//                "",
//                "§7Joined at: " + formattedDate
//        );
//
//        SkullMeta playerMeta = (SkullMeta) playerHead.getItemMeta();
//        if (playerMeta != null) {
//            playerMeta.setOwningPlayer(player);
//            playerMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "uuid"), PersistentDataType.STRING, player.getUniqueId().toString());
//            playerHead.setItemMeta(playerMeta);
//        }
//
//        return playerHead;
//    }
}
