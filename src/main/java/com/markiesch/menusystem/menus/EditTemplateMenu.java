package com.markiesch.menusystem.menus;

import com.markiesch.Permission;
import com.markiesch.chat.PlayerChat;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.Menu;
import com.markiesch.modules.template.TemplateManager;
import com.markiesch.modules.template.TemplateModel;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class EditTemplateMenu extends Menu {
    private static final byte SLOTS = 45;

    private static final byte NAME_SLOT = 20;
    private static final byte REASON_SLOT = 21;
    private static final byte TYPE_SLOT = 23;
    private static final byte DURATION_SLOT = 24;
    private static final byte UPDATE_SLOT = 40;

    private final TemplateModel template;

    public EditTemplateMenu(Plugin plugin, UUID uuid, int templateId) {
        super(plugin, uuid, SLOTS);

        template = TemplateManager.getInstance().getTemplate(templateId);

        open();
    }

    @Override
    public String getMenuName() {
        return Translation.MENU_EDIT_TEMPLATE_TITLE.toString();
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }

    @Override
    public void setMenuItems() {
        ItemStack nameItem = ItemUtils.createItem(
                Material.PAPER,
                Translation.MENU_EDIT_TEMPLATE_NAME_BUTTON_TITLE.toString(),
                Translation.MENU_EDIT_TEMPLATE_NAME_BUTTON_LORE.addPlaceholder("name", template.getName()).toList()
        );
        setButton(NAME_SLOT, nameItem, (event) -> {
            new PlayerChat(
                    plugin,
                    getOwner(),
                    Translation.MENU_EDIT_TEMPLATE_INSERT_NAME_TITLE.toString(),
                    Translation.MENU_EDIT_TEMPLATE_INSERT_NAME_SUBTITLE.toString(),
                    (String message) -> {
                        template.setName(message);
                        open();
                    });
        });

        ItemStack typeItem = ItemUtils.createItem(
                template.getType().getMaterial(),
                Translation.MENU_EDIT_TEMPLATE_TYPE_BUTTON_TITLE.toString(),
                Translation.MENU_EDIT_TEMPLATE_TYPE_BUTTON_LORE.addPlaceholder("type", template.getType()).toList()
        );
        setButton(TYPE_SLOT, typeItem, event -> {
            template.setType(template.getType().getNextType());
            setMenuItems();
        });

        ItemStack timeItem = ItemUtils.createItem(
                Material.CLOCK,
                Translation.MENU_EDIT_TEMPLATE_TIME_BUTTON_TITLE.toString(),
                Translation.MENU_EDIT_TEMPLATE_TIME_BUTTON_LORE.addPlaceholder("duration", TimeUtils.makeReadable(template.getDuration())).toList()
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
                        template.setDuration(TimeUtils.parseTime(message));
                        open();
                    });
        });

        ItemStack reasonItem = ItemUtils.createItem(
                Material.WRITABLE_BOOK,
                Translation.MENU_EDIT_TEMPLATE_REASON_BUTTON_TITLE.toString(),
                Translation.MENU_EDIT_TEMPLATE_REASON_BUTTON_LORE.addPlaceholder("reason", template.getReason()).toList()
        );
        setButton(REASON_SLOT, reasonItem, event -> {
            new PlayerChat(
                    plugin,
                    getOwner(),
                    Translation.MENU_EDIT_TEMPLATE_INSERT_REASON_TITLE.toString(),
                    Translation.MENU_EDIT_TEMPLATE_INSERT_REASON_SUBTITLE.toString(),
                    (String message) -> {
                        template.setReason(message);
                        open();
                    });
        });

        ItemStack updateItem = ItemUtils.createItem(
                Material.EMERALD_BLOCK,
                Translation.MENU_EDIT_TEMPLATE_CONFIRM_TITLE.toString(),
                Translation.MENU_EDIT_TEMPLATE_CONFIRM_LORE.toList()
        );
        setButton(UPDATE_SLOT, updateItem, event -> {
            TemplateManager.getInstance().update(template.getId(), template.getName(), template.getType(), template.getReason(), template.getDuration());
            event.getWhoClicked().sendMessage(Translation.MENU_EDIT_TEMPLATE_SUCCESS.addPlaceholder("name", template.getName()).toString());
            new TemplateSelectorMenu(plugin, uuid);
        });
    }
}
