package com.markiesch.menusystem.menus;

import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.PaginatedModelMenu;
import com.markiesch.modules.category.CategoryModel;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.modules.warning.WarningManager;
import com.markiesch.modules.warning.WarningModel;
import com.markiesch.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class WarningsMenu extends PaginatedModelMenu<WarningModel> {
    private static final byte SLOTS = 54;
    private static final byte[] ITEM_SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
    private final static byte BACK_BUTTON_SLOT = 49;

    private final ProfileModel target;

    public WarningsMenu(Plugin plugin, UUID uuid, UUID targetUUID) {
        super(plugin, uuid, SLOTS, ITEM_SLOTS);

        target = ProfileManager.getInstance().getPlayer(targetUUID);
        if (target == null) return;

        open();
    }

    @Override
    public String getMenuName() {
        return Translation.MENU_WARNINGS_TITLE.addPlaceholder("name", target.getName()).toString();
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }

    @Override
    protected ItemStack modelToItemStack(WarningModel warning) {
        CategoryModel category = warning.getCategory();

        return ItemUtils.createItem(
                Material.BOOK,
                Translation.MENU_WARNINGS_WARNING_TITLE
                        .addPlaceholder("category", category == null ? Translation.WORD_WARNED : category.getName())
                        .toString(),
                Translation.MENU_WARNINGS_WARNING_LORE
                        .addPlaceholder("issuer", warning.getIssuerName())
                        .toList()
        );
    }

    @Override
    protected @NotNull List<WarningModel> getModels() {
        return WarningManager.getInstance().getPlayer(target.uuid);
    }

    @Override
    protected void handleModelClick(InventoryClickEvent event, WarningModel model) {
        if (event.getClick() != ClickType.DROP) return;

        WarningManager.getInstance().delete(model.getId());
        open();
    }

    @Override
    public void setMenuItems() {
        super.setMenuItems();

        final ItemStack backButton = ItemUtils.createItem(
                Material.OAK_SIGN,
                Translation.MENU_BACK_BUTTON_TITLE.toString(),
                Translation.MENU_BACK_BUTTON_LORE.toList()
        );
        setButton(BACK_BUTTON_SLOT, backButton, event -> new PlayerMenu(plugin, uuid, target.uuid));
    }
}
