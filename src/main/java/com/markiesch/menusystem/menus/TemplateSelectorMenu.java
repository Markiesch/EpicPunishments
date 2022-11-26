package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.chat.PlayerChat;
import com.markiesch.locale.Locale;
import com.markiesch.menusystem.PaginatedMenu;
import com.markiesch.modules.template.TemplateController;
import com.markiesch.modules.template.TemplateModel;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Locale.ROOT;

public class TemplateSelectorMenu extends PaginatedMenu {
    private static final byte SLOTS = 54;
    private static final int[] ITEM_SLOTS = { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34 };

    private static final byte NEW_TEMPLATE_SLOT = 52;
    private static final byte BACK_SLOT = 49;
    private static final byte SEARCH_NAME_SLOT = 46;
    private static final Material TEMPLATE_MATERIAL = Material.PAPER;

    private String filter = "";
    private final List<TemplateModel> templates;

    public TemplateSelectorMenu(EpicPunishments plugin, UUID uuid) {
        super(plugin, uuid, SLOTS, ITEM_SLOTS);

        templates = new TemplateController().readAll();

        open();
    }

    @Override
    public String getMenuName() {
        return Locale.MENU_TEMPLATES_TITLE.toString();
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
                new EditTemplateMenu(plugin, uuid, id);
            }

            return;
        }

        switch (event.getSlot()) {
            case SEARCH_NAME_SLOT -> {
                new PlayerChat(plugin, getOwner(), "", "", (String message) -> {
                    this.filter = message;
                    open();
                });
            }
            case NEW_TEMPLATE_SLOT -> new CreateTemplateMenu(plugin, uuid);
            case BACK_SLOT -> new PlayerSelectorMenu(plugin, uuid, 0);
        }
    }

    @Override
    public void setMenuItems() {
        String displayFilter = filter.equals("") ? "none" : filter;

        ItemStack filterNameButton = ItemUtils.createItem(Material.COMPASS,
                Locale.MENU_TEMPLATES_FILTER_TITLE
                        .addPlaceholder("current_filter", displayFilter)
                        .toString(),
                Locale.MENU_TEMPLATES_FILTER_LORE
                        .addPlaceholder("current_filter", displayFilter)
                        .toList());
        getInventory().setItem(SEARCH_NAME_SLOT, filterNameButton);

        ItemStack back = ItemUtils.createItem(Material.OAK_SIGN, Locale.MENU_BACK_BUTTON_TITLE.toString(), Locale.MENU_BACK_BUTTON_LORE.toList());
        getInventory().setItem(BACK_SLOT, back);

        ItemStack newTemplate = ItemUtils.createItem(Material.ANVIL,  Locale.MENU_TEMPLATES_CREATE_BUTTON_TITLE.toString(), Locale.MENU_TEMPLATES_CREATE_BUTTON_LORE.toList());
        getInventory().setItem(NEW_TEMPLATE_SLOT, newTemplate);

        List<ItemStack> items = templates
                .stream()
                .filter(template -> template.name.toLowerCase(ROOT).contains(filter.toLowerCase(ROOT)))
                .map(template -> {
                    ItemStack item = ItemUtils.createItem(
                            TEMPLATE_MATERIAL,
                            Locale.MENU_TEMPLATES_TEMPLATE_BUTTON_TITLE
                                    .addPlaceholder("template_name", template.name)
                                    .toString(),
                            Locale.MENU_TEMPLATES_TEMPLATE_BUTTON_LORE
                                    .addPlaceholder("template_type", template.type)
                                    .addPlaceholder("template_reason", template.reason)
                                    .addPlaceholder("template_duration", TimeUtils.makeReadable(template.duration))
                                    .toList()
                    );

                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "id"), PersistentDataType.INTEGER, template.id);
                        item.setItemMeta(meta);
                    }

                    return item;
                })
                .collect(Collectors.toList());

        if (items.isEmpty()) {
            ItemStack noTemplates = ItemUtils.createItem(Material.MAP, Locale.MENU_TEMPLATES_EMPTY_TITLE.toString(), Locale.MENU_TEMPLATES_EMPTY_LORE.toList());
            getInventory().setItem(22, noTemplates);
            return;
        }

        setPaginatedItems(items);
    }
}
