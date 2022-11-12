package com.markiesch.menusystem;

import com.markiesch.EpicPunishments;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class PaginatedModelMenu<T> extends PaginatedMenu {
    public PaginatedModelMenu(EpicPunishments plugin, UUID uuid, int slots, int[] itemSlots) {
        super(plugin, uuid, slots, itemSlots);
    }

    protected abstract ItemStack modelToItemStack(T model);
    protected abstract List<T> getModels();
    protected abstract void handleModelClick(T model);

    @Override
    public void handleMenu(InventoryClickEvent event) {
        super.handleMenu(event);

        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        Integer index = meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "modelId"), PersistentDataType.INTEGER);
        if (index == null) return;

        handleModelClick(this.getModels().get(index));
    }

    @Override
    public void setMenuItems() {
        List<ItemStack> items = this.getModels().stream().map(this::modelToItemStack).collect(Collectors.toList());

        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta != null) {
                itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "modelId"), PersistentDataType.INTEGER, i);
                item.setItemMeta(itemMeta);
            }
        }

        setPaginatedItems(items);
    }
}
