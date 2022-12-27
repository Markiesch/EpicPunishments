package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.Permission;
import com.markiesch.chat.PlayerChat;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.PaginatedModelMenu;
import com.markiesch.modules.infraction.InfractionType;
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

public class TemplateSelectorMenu extends PaginatedModelMenu<TemplateModel> {
    private static final byte SLOTS = 54;
    private static final byte[] ITEM_SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

    private static final byte NEW_TEMPLATE_SLOT = 52;
    private static final byte BACK_SLOT = 49;
    private static final byte SEARCH_NAME_SLOT = 46;
    private static final Material TEMPLATE_MATERIAL = Material.PAPER;

    private static final String DEFAULT_REASON = "None";
    private static final long DEFAULT_DURATION = 0L;
    private static final InfractionType DEFAULT_TYPE = InfractionType.BAN;

    private String filter = "";

    public TemplateSelectorMenu(EpicPunishments plugin, UUID uuid) {
        super(plugin, uuid, SLOTS, ITEM_SLOTS);
        open();
    }

    @Override
    public String getMenuName() {
        return Translation.MENU_TEMPLATES_TITLE.toString();
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.MANAGE_TEMPLATES;
    }

    @Override
    protected ItemStack modelToItemStack(TemplateModel template) {
        ItemStack item = ItemUtils.createItem(
                TEMPLATE_MATERIAL,
                Translation.MENU_TEMPLATES_TEMPLATE_BUTTON_TITLE
                        .addPlaceholder("template_name", template.name)
                        .toString(),
                Translation.MENU_TEMPLATES_TEMPLATE_BUTTON_LORE
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
    }

    @Override
    protected List<TemplateModel> getModels() {
        return new TemplateController()
                .readAll()
                .stream()
                .filter(template -> template.name.toLowerCase(ROOT).contains(filter.toLowerCase(ROOT)))
                .collect(Collectors.toList());
    }

    @Override
    protected void handleModelClick(InventoryClickEvent event, TemplateModel template) {
        if (event.getAction().equals(InventoryAction.DROP_ONE_SLOT)) {
            new TemplateController().delete(template.id);
            open();
            return;
        }

        new EditTemplateMenu(plugin, uuid, template.id);
    }

    @Override
    public void setMenuItems() {
        super.setMenuItems();

        final String displayFilter = filter.equals("") ? "none" : filter;

        ItemStack filterNameButton = ItemUtils.createItem(
                Material.COMPASS,
                Translation.MENU_TEMPLATES_FILTER_TITLE.addPlaceholder("current_filter", displayFilter).toString(),
                Translation.MENU_TEMPLATES_FILTER_LORE.addPlaceholder("current_filter", displayFilter).toList()
        );
        setButton(SEARCH_NAME_SLOT, filterNameButton, (event) -> {
            new PlayerChat(plugin, getOwner(), Translation.MENU_TEMPLATES_SEARCH_TITLE.toString(), Translation.MENU_TEMPLATES_SEARCH_SUBTITLE.toString(), (String message) -> {
                this.filter = message;
                open();
            });
        });

        ItemStack back = ItemUtils.createItem(
                Material.OAK_SIGN,
                Translation.MENU_BACK_BUTTON_TITLE.toString(),
                Translation.MENU_BACK_BUTTON_LORE.toList()
        );
        setButton(BACK_SLOT, back, (event) -> new PlayerSelectorMenu(plugin, uuid));

        ItemStack newTemplate = ItemUtils.createItem(
                Material.ANVIL,
                Translation.MENU_TEMPLATES_CREATE_BUTTON_TITLE.toString(),
                Translation.MENU_TEMPLATES_CREATE_BUTTON_LORE.toList()
        );
        setButton(NEW_TEMPLATE_SLOT, newTemplate, event -> {
            new PlayerChat(
                    plugin,
                    getOwner(),
                    Translation.MENU_TEMPLATES_CREATE_TITLE.toString(),
                    Translation.MENU_TEMPLATES_CREATE_SUBTITLE.toString(),
                    (String name) -> {
                        new TemplateController().create(name, DEFAULT_REASON, DEFAULT_TYPE, DEFAULT_DURATION);
                        getOwner().sendMessage(Translation.MENU_TEMPLATES_CREATE_SUCCESS.addPlaceholder("name", name).toString());
                        new TemplateSelectorMenu(plugin, uuid);
                    });
        });

        if (isEmpty) {
            ItemStack noTemplates = ItemUtils.createItem(Material.MAP, Translation.MENU_TEMPLATES_EMPTY_TITLE.toString(), Translation.MENU_TEMPLATES_EMPTY_LORE.toList());
            getInventory().setItem(22, noTemplates);
        }
    }
}
