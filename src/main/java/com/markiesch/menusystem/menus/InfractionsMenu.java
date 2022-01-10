package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.PlayerStorage;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.UUID;

public class InfractionsMenu extends Menu {
    EpicPunishments plugin = EpicPunishments.getInstance();
    public OfflinePlayer target;
    private final NamespacedKey dataKey = new NamespacedKey(plugin, "infraction");
    int page;
    int maxPages;
    boolean onLastPage = true;

    public InfractionsMenu(PlayerMenuUtility playerMenuUtility, OfflinePlayer player, int page) {
        super(playerMenuUtility);
        target = player;
        this.page = page;
        open();
    }

    public String getMenuName() {
        return target.getName() + " > Infractions";
    }

    public int getSlots() {
        return 54;
    }

    public void handleMenu(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;

        if (event.getCurrentItem().getType().equals(Material.PAPER) && event.getClick().equals(ClickType.DROP)) {
            ItemMeta meta = event.getCurrentItem().getItemMeta();
            if (meta != null) {
                String data = meta.getPersistentDataContainer().get(dataKey, PersistentDataType.STRING);
                PlayerStorage.removeInfraction(target.getUniqueId(), data);
                inventory.remove(Material.PAPER);
                setMenuItems();
                return;
            }
        }

        Player player = (Player) event.getWhoClicked();
        if (event.getSlot() == 45 && page != 0) new InfractionsMenu(EpicPunishments.getPlayerMenuUtility(player), target, --page);
        if (event.getSlot() == 53 && !onLastPage) new InfractionsMenu(EpicPunishments.getPlayerMenuUtility(player), target, ++page);
        if (event.getSlot() == 49) new PunishMenu(EpicPunishments.getPlayerMenuUtility(player), target);
    }

    public void setMenuItems() {
        ItemStack back = ItemUtils.createItem(Material.OAK_SIGN, "§b§lBack", "§7Click to go back");
        inventory.setItem(49, back);

        ArrayList<String> infractions = new ArrayList<>(PlayerStorage.getConfig().getStringList(target.getUniqueId() + ".infractions"));
        if (infractions.isEmpty()) return;

        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        for (int i = 0; i < slots.length; i++) {
            int index = slots.length * page + i;
            if (index >= infractions.size()) break;
            if (infractions.get(index) != null) {
                String[] data = infractions.get(i).split(";");

                OfflinePlayer issuer = Bukkit.getOfflinePlayer(UUID.fromString(data[0]));
                String type = data[1];
                String reason = data[2];
                long duration = Long.parseLong(data[3]);

                ItemStack infraction = ItemUtils.createItem(Material.PAPER, "§9§l" + type,
                        "§bPress Q §7to revoke punishment", "", "§7reason: §a" + reason, "§7Duration: §a" + TimeUtils.makeReadable(duration), "§7Issuer: §a" + issuer.getName());
                ItemMeta meta = infraction.getItemMeta();
                if (meta != null) {
                    meta.getPersistentDataContainer().set(dataKey, PersistentDataType.STRING, infractions.get(index));
                    infraction.setItemMeta(meta);
                }

                inventory.setItem(slots[i], infraction);
            }
        }

        maxPages = infractions.size() / slots.length;

        if (page >= 1) {
            ItemStack prevPage = ItemUtils.createItem(Material.ARROW, "§cPrevious Page", "§7Click to visit page " + page);
            inventory.setItem(45, prevPage);
        }

        if (page < maxPages) {
            ItemStack nextPage = ItemUtils.createItem(Material.ARROW, "§cNext Page", "§7Click to visit page " + (page + 2));
            onLastPage = false;
            inventory.setItem(53, nextPage);
        }
    }
}
