package com.markiesch.menusystem;

import com.markiesch.EpicPunishments;
import com.markiesch.locale.Locale;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class Menu implements InventoryHolder {
    protected final UUID uuid;
    protected final EpicPunishments plugin;
    private final int slots;
    private Inventory inventory;

    public Menu(EpicPunishments plugin, UUID uuid, int slots) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.slots = slots;
    }

    public abstract String getMenuName();
    public abstract String getRequiredPermission();
    public abstract void handleMenu(InventoryClickEvent event);
    public abstract void setMenuItems();

    public void open() {
        Player player = getOwner();

        if (getRequiredPermission() != null && !player.hasPermission(getRequiredPermission())) {
            player.sendMessage(Locale.MENU_NO_PERMISSION.toString());
            player.closeInventory();
            return;
        }
        inventory = Bukkit.createInventory(this, slots, this.getMenuName());
        this.setMenuItems();
        player.openInventory(inventory);
    }

    public Player getOwner() {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}