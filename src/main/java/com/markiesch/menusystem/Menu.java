package com.markiesch.menusystem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class Menu implements InventoryHolder {
    protected PlayerMenuUtility playerMenuUtility;
    private final String menuName;
    private final int slots;
    private Inventory inventory;

    public Menu(PlayerMenuUtility playerMenuUtility, String menuName, int slots) {
        this.playerMenuUtility = playerMenuUtility;
        this.menuName = menuName;
        this.slots = slots;
    }

    public abstract void handleMenu(InventoryClickEvent event);
    public abstract void setMenuItems();

    public void open() {
        Player player = playerMenuUtility.getOwner();
        inventory = Bukkit.createInventory(this, slots, menuName);
        this.setMenuItems();
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
