package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.PaginatedMenu;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.menusystem.PlayerSelectorSearchType;
import com.markiesch.modules.template.TemplateController;
import com.markiesch.modules.template.TemplateModel;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.*;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.stream.Collectors;

public class TemplateSelectorMenu extends PaginatedMenu {
    private static final byte SLOTS = 54;
    private static final int[] ITEM_SLOTS = { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34 };

    private static final byte NEW_TEMPLATE_SLOT = 52;
    private static final byte BACK_SLOT = 49;
    private static final Material TEMPLATE_MATERIAL = Material.PAPER;

    public TemplateSelectorMenu(EpicPunishments plugin, PlayerMenuUtility playerMenuUtility) {
        super(plugin, playerMenuUtility, SLOTS, ITEM_SLOTS);

        open();
    }

    @Override
    public String getMenuName() {
        return "Templates";
    }

    @Override
    public String getRequiredPermission() {
        return "epicpunishments.templates";
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        super.handleMenu(event);

        if (event.getCurrentItem() != null && event.getCurrentItem().getType().equals(TEMPLATE_MATERIAL)) {
            ItemMeta meta = event.getCurrentItem().getItemMeta();
            if (meta == null) return;

            Integer id = meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "id"), PersistentDataType.INTEGER);
            if (id == null) return;

            if (event.getAction().equals(InventoryAction.DROP_ONE_SLOT)) {
                new TemplateController().delete(id);
                open();
            } else {
                new EditTemplateMenu(plugin, playerMenuUtility, id);
            }

            return;
        }

        switch (event.getSlot()) {
            case NEW_TEMPLATE_SLOT -> new CreateTemplateMenu(plugin, playerMenuUtility);
            case BACK_SLOT -> new PlayerSelectorMenu(plugin, playerMenuUtility, 0, PlayerSelectorSearchType.ALL);
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack back = ItemUtils.createItem(Material.OAK_SIGN, "§b§lBack", "§7Click to go back");
        getInventory().setItem(BACK_SLOT, back);

        ItemStack newTemplate = ItemUtils.createItem(Material.ANVIL, "§b§lNew template", "§7Click to create a new template");
        getInventory().setItem(NEW_TEMPLATE_SLOT, newTemplate);

        List<TemplateModel> templates = new TemplateController().readAll();
        if (templates.isEmpty()) {
            ItemStack noTemplates = ItemUtils.createItem(Material.MAP, "§6§lNo Templates!", "§7There are no templates yet!");
            getInventory().setItem(22, noTemplates);
            return;
        }

        List<ItemStack> items = new TemplateController().readAll()
                .stream()
                .map(template -> {
                    ItemStack item = ItemUtils.createItem(
                            TEMPLATE_MATERIAL,
                            "§b§l" + template.name,
                            "§bLeft Click §7to manage template",
                            "§bPress Q §7to delete template",
                            "",
                            "§7Type: §e" + template.type,
                            "§7Reason: §e" + (template.reason != null ? template.reason : "None"),
                            "§7Duration: §e" + TimeUtils.makeReadable(template.duration)
                    );

                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "id"), PersistentDataType.INTEGER, template.id);
                        item.setItemMeta(meta);
                    }

                    return item;
                })
                .collect(Collectors.toList());

        setPaginatedItems(items);
    }
}
