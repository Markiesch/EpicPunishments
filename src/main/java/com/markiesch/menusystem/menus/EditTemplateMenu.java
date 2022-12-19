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
    public void setMenuItems() {
        ItemStack nameItem = ItemUtils.createItem(
                Material.PAPER,
                Translation.MENU_EDIT_TEMPLATE_NAME_BUTTON_TITLE.toString(),
                Translation.MENU_EDIT_TEMPLATE_NAME_BUTTON_LORE.addPlaceholder("name", template.name).toList()
        );
        setButton(NAME_SLOT, nameItem, (event) -> {
            new PlayerChat(
                    plugin,
                    getOwner(),
                    Translation.MENU_EDIT_TEMPLATE_INSERT_NAME_TITLE.toString(),
                    Translation.MENU_EDIT_TEMPLATE_INSERT_NAME_SUBTITLE.toString(),
                    (String message) -> {
                        template.name = message;
                        open();
                    });
        });

        ItemStack typeItem = ItemUtils.createItem(
                template.type.getMaterial(),
                Translation.MENU_EDIT_TEMPLATE_TYPE_BUTTON_TITLE.toString(),
                Translation.MENU_EDIT_TEMPLATE_TYPE_BUTTON_LORE.addPlaceholder("type", template.type).toList()
        );
        setButton(TYPE_SLOT, typeItem, event -> {
            template.type = template.type.getNextType();
            setMenuItems();
        });

        ItemStack timeItem = ItemUtils.createItem(
                Material.CLOCK,
                Translation.MENU_EDIT_TEMPLATE_TIME_BUTTON_TITLE.toString(),
                Translation.MENU_EDIT_TEMPLATE_TIME_BUTTON_LORE.addPlaceholder("duration", TimeUtils.makeReadable(template.duration)).toList()
        );
        setButton(DURATION_SLOT, timeItem, event -> {
            for (String line : Translation.MENU_EDIT_TEMPLATE_INSERT_DURATION_INFO.toList()) {
                event.getWhoClicked().sendMessage(line);
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
        });

        ItemStack reasonItem = ItemUtils.createItem(
                Material.WRITABLE_BOOK,
                Translation.MENU_EDIT_TEMPLATE_REASON_BUTTON_TITLE.toString(),
                Translation.MENU_EDIT_TEMPLATE_REASON_BUTTON_LORE.addPlaceholder("reason", template.reason).toList()
        );
        setButton(REASON_SLOT, reasonItem, event -> {
            new PlayerChat(
                    plugin,
                    getOwner(),
                    Translation.MENU_EDIT_TEMPLATE_INSERT_REASON_TITLE.toString(),
                    Translation.MENU_EDIT_TEMPLATE_INSERT_REASON_SUBTITLE.toString(),
                    (String message) -> {
                        template.reason = message;
                        open();
                    });
        });

        ItemStack createItem = ItemUtils.createItem(
                Material.EMERALD_BLOCK,
                Translation.MENU_EDIT_TEMPLATE_CONFIRM_TITLE.toString(),
                Translation.MENU_EDIT_TEMPLATE_CONFIRM_LORE.toList()
        );
        setButton(CREATE_SLOT, createItem, event -> {
            templateController.updateTemplate(template.id, template.name, template.type, template.reason, template.duration);
            event.getWhoClicked().sendMessage(Translation.MENU_EDIT_TEMPLATE_SUCCESS.addPlaceholder("name", template.name).toString());
            new TemplateSelectorMenu(plugin, uuid);
        });
    }
}
