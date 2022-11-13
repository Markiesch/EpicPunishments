package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.PaginatedMenu;
import com.markiesch.modules.infraction.InfractionController;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class InfractionsMenu extends PaginatedMenu {
    private final OfflinePlayer target;
    private final static int BACK_BUTTON_SLOT = 49;

    private final static int[] SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

    public InfractionsMenu(EpicPunishments plugin, UUID uuid, UUID target) {
        super(plugin, uuid, 54, SLOTS);

        this.target = Bukkit.getOfflinePlayer(target);
        open();
    }

    @Override
    public String getMenuName() {
        return "Infractions > " + target.getName();
    }

    @Override
    public String getRequiredPermission() {
        return null;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;

        if (event.getSlot() == BACK_BUTTON_SLOT) {
            new PlayerMenu(plugin, uuid, target.getUniqueId());
            return;
        }

        if (event.getCurrentItem().getType() == Material.PAPER && event.getAction() == InventoryAction.DROP_ONE_SLOT) {
            ItemMeta meta = event.getCurrentItem().getItemMeta();

            if (meta != null) {
                Integer id = meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "id"), PersistentDataType.INTEGER);
                new InfractionController().delete(id);
            }
            open();
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack backButton = ItemUtils.createItem(Material.OAK_SIGN, "§b§lBack", "§7Click to go back");
        getInventory().setItem(BACK_BUTTON_SLOT, backButton);

        List<InfractionModel> infractions = new InfractionController().readAll(target.getUniqueId());

        List<ItemStack> items = infractions
                .stream()
                .map(infraction -> {
                    ItemStack item = ItemUtils.createItem(
                            Material.PAPER,
                            "§b§l" + infraction.type,
                            "§bPress Q §7to revoke punishment",
                            "",
                            "§7reason: §a" + infraction.reason,
                            "§7Duration: §a" + TimeUtils.makeReadable(infraction.duration),
                            "§7Issuer: §a" + infraction.getIssuer()
                    );

                    ItemMeta meta = item.getItemMeta();

                    if (meta != null) {
                        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "id"), PersistentDataType.INTEGER, infraction.id);
                        item.setItemMeta(meta);
                    }

                    return item;
                }
            ).collect(Collectors.toList());

        setPaginatedItems(items);
    }
}
