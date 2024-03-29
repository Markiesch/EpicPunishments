package com.markiesch.menusystem.menus;

import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.PaginatedModelMenu;
import com.markiesch.modules.template.TemplateManager;
import com.markiesch.modules.template.TemplateModel;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class SelectTemplateMenu extends PaginatedModelMenu<TemplateModel> {
    private final static byte SLOTS = 54;
    private final static byte[] ITEM_SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
    private final static byte BACK_SLOT = 49;

    private final Consumer<TemplateModel> callback;

    public SelectTemplateMenu(Plugin plugin, UUID uuid, Consumer<@Nullable TemplateModel> callback) {
        super(plugin, uuid, SLOTS, ITEM_SLOTS);

        this.callback = callback;

        open();
    }

    @Override
    public String getMenuName() {
        return Translation.MENU_SELECT_TEMPLATE_TITLE.toString();
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }

    @Override
    protected @NotNull List<TemplateModel> getModels() {
        return TemplateManager.getInstance().getTemplates();
    }

    @Override
    protected void handleModelClick(InventoryClickEvent event, TemplateModel template) {
        callback.accept(template);
    }

    @Override
    public void setMenuItems() {
        super.setMenuItems();

        ItemStack backButton = ItemUtils.createItem(
                Material.OAK_SIGN,
                Translation.MENU_BACK_BUTTON_TITLE.toString(),
                Translation.MENU_BACK_BUTTON_LORE.toList()
        );

        setButton(BACK_SLOT, backButton, (event) -> callback.accept(null));
    }

    @Override
    protected ItemStack modelToItemStack(TemplateModel template) {
        return ItemUtils.createItem(Material.PAPER,
                Translation.MENU_SELECT_TEMPLATE_BUTTON_TITLE
                        .addPlaceholder("name", template.getName())
                        .toString(),
                Translation.MENU_SELECT_TEMPLATE_BUTTON_LORE
                        .addPlaceholder("reason", template.getReason())
                        .addPlaceholder("duration", TimeUtils.makeReadable(template.getDuration()))
                        .addPlaceholder("type", template.getType())
                        .toList()
        );
    }
}