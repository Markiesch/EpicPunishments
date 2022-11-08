package com.markiesch.menusystem;

import com.markiesch.EpicPunishments;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class Menu implements InventoryHolder {
    protected PlayerMenuUtility playerMenuUtility;
    protected final EpicPunishments plugin;
    private final int slots;
    private Inventory inventory;

    public Menu(EpicPunishments plugin, PlayerMenuUtility playerMenuUtility, int slots) {
        this.plugin = plugin;
        this.playerMenuUtility = playerMenuUtility;
        this.slots = slots;
    }

    public abstract String getMenuName();
    public abstract String getRequiredPermission();
    public abstract void handleMenu(InventoryClickEvent event);
    public abstract void setMenuItems();

    public void open() {
        Player player = playerMenuUtility.getOwner();

        if (getRequiredPermission() != null && !player.hasPermission(getRequiredPermission())) {
            player.sendMessage("§7You do not have§c permissions§7 to view templates");
            player.closeInventory();
            return;
        }
        inventory = Bukkit.createInventory(this, slots, this.getMenuName());
        this.setMenuItems();
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}