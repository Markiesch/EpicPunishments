package com.markiesch.menusystem.menus;

import com.markiesch.Permission;
import com.markiesch.chat.PlayerChat;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.PaginatedModelMenu;
import com.markiesch.modules.category.CategoryManager;
import com.markiesch.modules.category.CategoryModel;
import com.markiesch.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class CategoriesMenu extends PaginatedModelMenu<CategoryModel> {
    private static final byte SLOTS = 54;
    private static final byte[] ITEM_SLOTS = { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34 };
    private static final byte BACK_SLOT = 49;
    private static final byte CREATE_SLOT = 52;

    public CategoriesMenu(Plugin plugin, UUID uuid) {
        super(plugin, uuid, SLOTS, ITEM_SLOTS);
        open();
    }

    @Override
    public String getMenuName() {
        return Translation.MENU_CATEGORIES_TITLE.toString();
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }

    @Override
    protected @NotNull List<CategoryModel> getModels() {
        return CategoryManager.getInstance().getCategories();
    }

    @Override
    protected ItemStack modelToItemStack(CategoryModel model) {
        return ItemUtils.createItem(
            Material.BOOKSHELF,
            Translation.MENU_CATEGORIES_MODEL_TITLE.addPlaceholder("name", model.getName()).toString(),
            Translation.MENU_CATEGORIES_MODEL_LORE.toList()
        );
    }

    @Override
    protected void handleModelClick(InventoryClickEvent event, CategoryModel category) {
        if (event.getClick() == ClickType.DROP) {
            CategoryManager.getInstance().delete(category.getId());
            open();
            return;
        }

        new CategoryMenu(plugin, uuid, category);
    }

    @Override
    public void setMenuItems() {
        super.setMenuItems();

        setButton(
            BACK_SLOT,
            ItemUtils.createItem(
                Material.OAK_SIGN,
                Translation.MENU_BACK_BUTTON_TITLE.toString(),
                Translation.MENU_BACK_BUTTON_LORE.toList()
            ),
            (event) -> new PlayerSelectorMenu(plugin, uuid)
        );

        setButton(
            CREATE_SLOT,
            ItemUtils.createItem(
                Material.EMERALD_BLOCK,
                Translation.MENU_CATEGORIES_CREATE_TITLE.toString(),
                Translation.MENU_CATEGORIES_CREATE_LORE.toList()
            ),
            (event) -> {
                new PlayerChat(plugin, getOwner(), Translation.MENU_CATEGORY_INSERT_NAME_TITLE.toString(), Translation.MENU_CATEGORY_INSERT_NAME_SUBTITLE.toString(), (message) -> {
                    CategoryManager.getInstance().create(message);
                    open();
                });
            }
        );
    }
}
