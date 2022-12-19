package com.markiesch.menusystem;

import com.markiesch.EpicPunishments;
import com.markiesch.locale.Translation;
import com.markiesch.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public abstract class PaginatedModelMenu<T> extends Menu {
    private static final byte PREV_PAGE_SLOT = 45;
    private static final byte NEXT_PAGE_SLOT = 53;
    private final int[] itemSlots;

    protected boolean isEmpty = false;
    private int page = 0;

    public PaginatedModelMenu(EpicPunishments plugin, UUID uuid, int slots, int[] itemSlots) {
        super(plugin, uuid, slots);
        this.itemSlots = itemSlots;
    }

    protected abstract ItemStack modelToItemStack(T model);
    protected abstract List<T> getModels();
    protected abstract void handleModelClick(InventoryClickEvent event, T model);

    @Override
    public void setMenuItems() {
        List<T> models = getModels();

        List<T> items = models.subList(itemSlots.length * page, Math.min(itemSlots.length * page + itemSlots.length, models.size()));
        isEmpty = items.size() == 0;

        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            setButton(itemSlots[i], this.modelToItemStack(item), (event) -> handleModelClick(event, item));
        }

        setPaginationItems(items);
    }

    private void setPaginationItems(List<T> items) {
        int maxPages = items.size() / itemSlots.length;

        if (page >= 1) {
            ItemStack prevPage = ItemUtils.createItem(
                    Material.ARROW,
                    Translation.PREVIOUS_PAGE.toString(),
                    Translation.VISIT_PAGE.addPlaceholder("page", page).toString()
            );
            setButton(PREV_PAGE_SLOT, prevPage, event -> {
                page--;
                open();
            });
        }

        if (page < maxPages) {
            ItemStack nextPage = ItemUtils.createItem(
                    Material.ARROW,
                    Translation.NEXT_PAGE.toString(),
                    Translation.VISIT_PAGE.addPlaceholder("page", page + 2).toString()
            );
            setButton(NEXT_PAGE_SLOT, nextPage, event -> {
                page++;
                open();
            });
        }
    }
}
