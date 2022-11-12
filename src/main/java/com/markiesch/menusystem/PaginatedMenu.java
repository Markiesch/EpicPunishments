package com.markiesch.menusystem;

import com.markiesch.EpicPunishments;
import com.markiesch.locale.Locale;
import com.markiesch.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public abstract class PaginatedMenu extends Menu {
    protected final int prevPageSlot = 45;
    protected final int nextPageSlot = 53;
    protected boolean onLastPage = true;

    protected int page = 0;
    protected final int[] itemSlots;

    public PaginatedMenu(EpicPunishments plugin, UUID uuid, int slots, int[] itemSlots) {
        super(plugin, uuid, slots);
        this.itemSlots = itemSlots;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        if (event.getSlot() == prevPageSlot && page != 0) {
          page--;
          getInventory().clear();
          setMenuItems();
        }

        if (event.getSlot() == nextPageSlot && !onLastPage) {
            page++;
            getInventory().clear();
            setMenuItems();
        }
    }

    public void setPaginatedItems(List<ItemStack> items) {
        items = items.subList(itemSlots.length * page, Math.min(itemSlots.length * page + itemSlots.length, items.size()));

        for (int i = 0; i < items.size(); i++) {
            getInventory().setItem(itemSlots[i], items.get(i));
        }

        int maxPages = items.size() / itemSlots.length;

        if (page >= 1) {
            ItemStack prevPage = ItemUtils.createItem(Material.ARROW, Locale.PREVIOUS_PAGE.toString(), Locale.VISIT_PAGE.toString().replaceAll("[page]", Integer.toString(page)));
            getInventory().setItem(prevPageSlot, prevPage);
        }

        if (page < maxPages) {
            ItemStack nextPage = ItemUtils.createItem(Material.ARROW, Locale.NEXT_PAGE.toString(), Locale.VISIT_PAGE.toString().replaceAll("[page]", Integer.toString(page + 2)));
            onLastPage = false;
            getInventory().setItem(nextPageSlot, nextPage);
        }
    }
}
