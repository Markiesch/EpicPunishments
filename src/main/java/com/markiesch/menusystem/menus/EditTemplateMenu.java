package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.chat.PlayerChat;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.Menu;
import com.markiesch.modules.template.TemplateController;
import com.markiesch.modules.template.TemplateModel;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class EditTemplateMenu extends Menu {
    private static final byte SLOTS = 45;

    private static final byte NAME_SLOT = 20;
    private static final byte REASON_SLOT = 21;
    private static final byte TYPE_SLOT = 23;
    private static final byte DURATION_SLOT = 24;
    private static final byte CREATE_SLOT = 40;

    private final TemplateController templateController;
    private final TemplateModel template;

    public EditTemplateMenu(EpicPunishments plugin, UUID uuid, int id) {
        super(plugin, uuid, SLOTS);

        templateController = new TemplateController();
        template = templateController.readSingle(id);

        open();
    }

    @Override
    public String getMenuName() {
        return Translation.MENU_EDIT_TEMPLATE_TITLE.toString();
    }

    @Override
    public String getRequiredPermission() {
        return null;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case NAME_SLOT -> {
                new PlayerChat(
                        plugin,
                        getOwner(),
                        Translation.MENU_EDIT_TEMPLATE_INSERT_NAME_TITLE.toString(),
                        Translation.MENU_EDIT_TEMPLATE_INSERT_NAME_SUBTITLE.toString(),
                        (String message) -> {
                            template.name = message;
                            open();
                        });
            }
            case TYPE_SLOT -> {
                template.type = template.type.getNextType();
                setMenuItems();
            }
            case DURATION_SLOT -> {
                for (String line : Translation.MENU_EDIT_TEMPLATE_INSERT_DURATION_INFO.toList()) {
                    player.sendMessage(line);
                }

                new PlayerChat(
                        plugin,
                        getOwner(),
                        Translation.MENU_EDIT_TEMPLATE_INSERT_DURATION_TITLE.toString(),
                        Translation.MENU_EDIT_TEMPLATE_INSERT_DURATION_SUBTITLE.toString(),
                        (String message) -> {
                            template.duration = TimeUtils.parseTime(message);
                            open();
                        });
            }
            case REASON_SLOT -> {
                new PlayerChat(
                        plugin,
                        getOwner(),
                        Translation.MENU_EDIT_TEMPLATE_INSERT_REASON_TITLE.toString(),
                        Translation.MENU_EDIT_TEMPLATE_INSERT_REASON_SUBTITLE.toString(),
                        (String message) -> {
                            template.reason = message;
                            open();
                        });
            }
            case CREATE_SLOT -> {
                templateController.updateTemplate(template.id, template.name, template.type, template.reason, template.duration);
                player.sendMessage(Translation.MENU_EDIT_TEMPLATE_SUCCESS.addPlaceholder("name", template.name).toString());
                new TemplateSelectorMenu(plugin, uuid);
            }
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack nameItem = ItemUtils.createItem(
                Material.PAPER,
                Translation.MENU_EDIT_TEMPLATE_NAME_BUTTON_TITLE.toString(),
                Translation.MENU_EDIT_TEMPLATE_NAME_BUTTON_LORE.addPlaceholder("name", template.name).toList()
        );
        getInventory().setItem(NAME_SLOT, nameItem);

        ItemStack typeItem = ItemUtils.createItem(
                template.type.getMaterial(),
                Translation.MENU_EDIT_TEMPLATE_TYPE_BUTTON_TITLE.toString(),
                Translation.MENU_EDIT_TEMPLATE_TYPE_BUTTON_LORE.addPlaceholder("type", template.type).toList()
        );
        getInventory().setItem(TYPE_SLOT, typeItem);

        ItemStack timeItem = ItemUtils.createItem(
                Material.CLOCK,
                Translation.MENU_EDIT_TEMPLATE_TIME_BUTTON_TITLE.toString(),
                Translation.MENU_EDIT_TEMPLATE_TIME_BUTTON_LORE.addPlaceholder("duration", TimeUtils.makeReadable(template.duration)).toList()
        );
        getInventory().setItem(DURATION_SLOT, timeItem);

        ItemStack reasonItem = ItemUtils.createItem(
                Material.WRITABLE_BOOK,
                Translation.MENU_EDIT_TEMPLATE_REASON_BUTTON_TITLE.toString(),
                Translation.MENU_EDIT_TEMPLATE_REASON_BUTTON_LORE.addPlaceholder("reason", template.reason).toList()
        );
        getInventory().setItem(REASON_SLOT, reasonItem);

        ItemStack createItem = ItemUtils.createItem(
                Material.EMERALD_BLOCK,
                Translation.MENU_EDIT_TEMPLATE_CONFIRM_TITLE.toString(),
                Translation.MENU_EDIT_TEMPLATE_CONFIRM_LORE.toList()
        );
        getInventory().setItem(CREATE_SLOT, createItem);
    }
}
