package com.markiesch.menusystem.menus;

import com.markiesch.Permission;
import com.markiesch.chat.PlayerChat;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.PaginatedModelMenu;
import com.markiesch.modules.category.CategoryManager;
import com.markiesch.modules.category.CategoryModel;
import com.markiesch.modules.categoryRule.CategoryRuleManager;
import com.markiesch.modules.categoryRule.CategoryRuleModel;
import com.markiesch.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class CategoryMenu extends PaginatedModelMenu<CategoryRuleModel> {
    private final static byte ADD_RULE_BUTTON = 52;
    private final static byte MODEL_BUTTON = 4;
    private final static byte BACK_BUTTON = 49;
    private final static byte EMPTY_RULES_BUTTON = 22;

    private final CategoryModel categoryModel;

    public CategoryMenu(Plugin plugin, UUID uuid, CategoryModel categoryModel) {
        super(plugin, uuid, 54, new byte[]{19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34});
        this.categoryModel = categoryModel;

        open();
    }

    @Override
    public String getMenuName() {
        return Translation.MENU_CATEGORY_TITLE.addPlaceholder("name", categoryModel.getName()).toString();
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }

    @Override
    protected ItemStack modelToItemStack(CategoryRuleModel categoryRule) {
        return ItemUtils.createItem(
                Material.BOOK,
                Translation.MENU_CATEGORY_RULE_TITLE.toString(),
                Translation.MENU_CATEGORY_RULE_LORE
                        .addPlaceholder("template", categoryRule.getTemplateName())
                        .addPlaceholder("count", categoryRule.getCount())
                        .toList()
        );
    }

    @Override
    protected @NotNull List<CategoryRuleModel> getModels() {
        return CategoryRuleManager.getInstance().getCategoryRules(categoryModel.getId());
    }

    @Override
    protected void handleModelClick(InventoryClickEvent event, CategoryRuleModel model) {
        if (event.getClick() == ClickType.DROP) {
            CategoryRuleManager.getInstance().delete(model.getId());
            open();
            return;
        }

        if (event.getClick() == ClickType.LEFT) {
            new SelectTemplateMenu(plugin, uuid, (template) -> {
                if (template == null) return;

                CategoryRuleManager.getInstance().update(model.getId(), template.id, model.getCount());
                open();
            });
            return;
        }

        if (event.getClick() == ClickType.RIGHT) {
            // TODO promt to change amount/count
        }
    }

    @Override
    public void setMenuItems() {
        super.setMenuItems();

        if (getModels().isEmpty()) {
            setButton(
                    EMPTY_RULES_BUTTON,
                    ItemUtils.createItem(
                            Material.PAPER,
                            Translation.MENU_CATEGORY_EMPTY_TITLE.toString(),
                            Translation.MENU_CATEGORY_EMPTY_LORE.toList()
                    )
            );
        }

        setButton(
                MODEL_BUTTON,
                ItemUtils.createItem(
                        Material.BOOKSHELF,
                        Translation.MENU_CATEGORY_INFO_TITLE.addPlaceholder("name", categoryModel.getName()).toString(),
                        Translation.MENU_CATEGORY_INFO_LORE.toList()
                ),
                (event) -> {
                    new PlayerChat(plugin, getOwner(), "insert a new name", "", (message) -> {
                        CategoryManager.getInstance().update(categoryModel.getId(), message);
                        open();
                    });
                }
        );
        setButton(
                BACK_BUTTON,
                ItemUtils.createItem(
                        Material.OAK_SIGN,
                        Translation.MENU_BACK_BUTTON_TITLE.toString(),
                        Translation.MENU_BACK_BUTTON_LORE.toList()
                ),
                (event) -> new CategoriesMenu(plugin, uuid)
        );

        setButton(
                ADD_RULE_BUTTON,
                ItemUtils.createItem(
                        Material.EMERALD_BLOCK,
                        Translation.MENU_CATEGORY_CREATE_RULE_TITLE.toString(),
                        Translation.MENU_CATEGORY_CREATE_RULE_LORE.toList()
                ),
                (event) -> {
                    new SelectTemplateMenu(plugin, uuid, (template) -> {
                        if (template == null) return;

                        new PlayerChat(plugin, getOwner(), "Insert ", "", (message) -> {
                             CategoryRuleManager.getInstance().create(categoryModel.getId(), template.id, Integer.parseInt(message));
                            open();
                        });
                    });
                }
        );
    }
}
