package com.markiesch.menusystem.menus;

import com.markiesch.Permission;
import com.markiesch.chat.IntegerPlayerChat;
import com.markiesch.chat.PlayerChat;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.PaginatedModelMenu;
import com.markiesch.modules.category.CategoryManager;
import com.markiesch.modules.category.CategoryModel;
import com.markiesch.modules.categoryRule.CategoryRuleManager;
import com.markiesch.modules.categoryRule.CategoryRuleModel;
import com.markiesch.utils.ItemUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
        return Permission.MANAGE_CATEGORIES;
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
            CategoryRuleManager.getInstance().delete(model);
            open();
            return;
        }

        if (event.getClick() == ClickType.LEFT) {
            new SelectTemplateMenu(plugin, uuid, (template) -> {
                if (template == null) return;

                CategoryRuleManager.getInstance().update(model.getCategoryId(), model.getId(), template.getId(), model.getCount());
                open();
            });
            return;
        }

        if (event.getClick() == ClickType.RIGHT) {
            new IntegerPlayerChat(plugin, getOwner(), Translation.MENU_CATEGORY_INSERT_RULE_COUNT_TITLE.toString(), Translation.MENU_CATEGORY_INSERT_RULE_COUNT_SUBTITLE.toString(), (count) -> {
                CategoryRuleManager.getInstance().update(model.getCategoryId(), model.getId(), model.getTemplateId(), count);
                open();
            });
        }
    }

    @Override
    public void setMenuItems() {
        super.setMenuItems();

        if (getModels().isEmpty()) {
            ItemStack emptyButtonItem = ItemUtils.createItem(
                    Material.PAPER,
                    Translation.MENU_CATEGORY_EMPTY_TITLE.toString(),
                    Translation.MENU_CATEGORY_EMPTY_LORE.toList()
            );

            setButton(EMPTY_RULES_BUTTON, emptyButtonItem);
        }

        final ItemStack categoryButtonItem = ItemUtils.createItem(
                Material.BOOKSHELF,
                Translation.MENU_CATEGORY_INFO_TITLE.addPlaceholder("name", categoryModel.getName()).toString(),
                Translation.MENU_CATEGORY_INFO_LORE
                        .addPlaceholder("message", categoryModel.getMessage())
                        .toList()
        );
        setButton(MODEL_BUTTON, categoryButtonItem, this::handleCategoryButtonClick);

        final ItemStack backButtonItem = ItemUtils.createItem(
                Material.OAK_SIGN,
                Translation.MENU_BACK_BUTTON_TITLE.toString(),
                Translation.MENU_BACK_BUTTON_LORE.toList()
        );
        setButton(BACK_BUTTON, backButtonItem, (event) -> new CategoriesMenu(plugin, uuid));

        final ItemStack addRuleButtonItem = ItemUtils.createItem(
                Material.EMERALD_BLOCK,
                Translation.MENU_CATEGORY_CREATE_RULE_TITLE.toString(),
                Translation.MENU_CATEGORY_CREATE_RULE_LORE.toList()
        );
        setButton(ADD_RULE_BUTTON, addRuleButtonItem, this::handleAddRuleButtonClick);
    }

    private void handleCategoryButtonClick(InventoryClickEvent event) {
        if (event.getClick() == ClickType.LEFT) {
            new PlayerChat(plugin, getOwner(), Translation.MENU_CATEGORY_INSERT_NAME_TITLE.toString(), Translation.MENU_CATEGORY_INSERT_NAME_SUBTITLE.toString(), (message) -> {
                CategoryManager.getInstance().update(categoryModel.getId(), message, categoryModel.getMessage());
                open();
            });
        } else if (event.getClick() == ClickType.RIGHT) {
            TextComponent textComponent = new TextComponent(Translation.MENU_CATEGORY_INSERT_MESSAGE_COPY.toString());
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, categoryModel.getMessage()));
            getOwner().spigot().sendMessage(textComponent);

            new PlayerChat(plugin, getOwner(), Translation.MENU_CATEGORY_INSERT_MESSAGE_TITLE.toString(), Translation.MENU_CATEGORY_INSERT_MESSAGE_SUBTITLE.toString(), (message) -> {
                CategoryManager.getInstance().update(categoryModel.getId(), categoryModel.getName(), message);
                open();
            });
        }
    }

    private void handleAddRuleButtonClick(InventoryClickEvent event) {
        new SelectTemplateMenu(plugin, uuid, (template) -> {
            if (template == null) {
                open();
                return;
            }

            new IntegerPlayerChat(
                    plugin,
                    getOwner(),
                    Translation.MENU_CATEGORY_INSERT_RULE_COUNT_TITLE.toString(),
                    Translation.MENU_CATEGORY_INSERT_RULE_COUNT_SUBTITLE.toString(),
                    (count) -> {
                        CategoryRuleManager.getInstance().create(categoryModel.getId(), template.getId(), count);
                        open();
                    });
        });
    }
}
