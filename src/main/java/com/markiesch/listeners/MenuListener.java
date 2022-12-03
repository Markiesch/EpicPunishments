package com.markiesch.listeners;

import com.markiesch.menusystem.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof Menu)) return;
        event.setCancelled(true);
        Menu menu = (Menu) holder;
        menu.handleMenu(event);
    }
}