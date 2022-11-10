package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.Menu;
import com.markiesch.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PunishMenu extends Menu {
    private static final byte BACK_BUTTON_SLOT = 49;

    private final OfflinePlayer target;

    public PunishMenu(EpicPunishments plugin, UUID uuid, UUID target) {
        super(plugin, uuid, 54);

        this.target = Bukkit.getOfflinePlayer(target);
    }

    @Override
    public String getMenuName() {
        return "Punish > " + target.getName();
    }

    @Override
    public String getRequiredPermission() {
        return "epicpunishments.punish";
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

        switch (event.getSlot()) {
            case BACK_BUTTON_SLOT -> {
                new PlayerMenu(plugin, uuid, target.getUniqueId());
            }
        }

    }

    @Override
    public void setMenuItems() {
        ItemStack back = ItemUtils.createItem(Material.OAK_SIGN, "§b§lBack", "§7Click to go back");
        getInventory().setItem(BACK_BUTTON_SLOT, back);
    }
}
