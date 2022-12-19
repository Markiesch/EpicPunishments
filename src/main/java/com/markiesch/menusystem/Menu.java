package com.markiesch.menusystem;

import com.markiesch.EpicPunishments;
import com.markiesch.locale.Translation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class Menu implements InventoryHolder {
    private final HashMap<Integer, Consumer<InventoryClickEvent>> clickMap = new HashMap<>();
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
            player.sendMessage(Translation.MENU_NO_PERMISSION.toString());
            player.closeInventory();
            return;
        }
        inventory = Bukkit.createInventory(this, slots, this.getMenuName());
        this.setMenuItems();
        player.openInventory(inventory);
    }


    protected void setButton(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> callback) {
        getInventory().setItem(slot, itemStack);
        clickMap.put(slot, callback);
    }

    public void handleButtonClick(InventoryClickEvent event) {
        if (clickMap.containsKey(event.getSlot())) {
            clickMap.get(event.getSlot()).accept(event);
        }
    }

    public Player getOwner() {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}