package com.markiesch.menusystem;

import com.markiesch.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class PaginatedMenu extends Menu {

    protected final int prevPageSlot = 45;
    protected final int nextPageSlot = 53;
    protected boolean onLastPage = true;

    protected int page = 0;
    protected final int[] itemSlots;

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility, String menuName, int slots, int[] itemSlots) {
        super(playerMenuUtility, menuName, slots);
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

        int maxPages = items.size() / itemSlots.length;

        if (page >= 1) {
            ItemStack prevPage = ItemUtils.createItem(Material.ARROW, "§cPrevious Page", "§7Click to visit page " + page);
            getInventory().setItem(prevPageSlot, prevPage);
        }

        if (page < maxPages) {
            ItemStack nextPage = ItemUtils.createItem(Material.ARROW, "§cNext Page", "§7Click to visit page " + (page + 2));
            onLastPage = false;
            getInventory().setItem(nextPageSlot, nextPage);
        }
    }
}
