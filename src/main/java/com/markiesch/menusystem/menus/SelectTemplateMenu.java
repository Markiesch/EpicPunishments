package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.PaginatedModelMenu;
import com.markiesch.modules.template.TemplateController;
import com.markiesch.modules.template.TemplateModel;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class SelectTemplateMenu extends PaginatedModelMenu<TemplateModel> {
    private final static int SLOTS = 54;
    private final static int[] ITEM_SLOTS = new int[] { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34 };
    private final Consumer<TemplateModel> callback;
    private final List<TemplateModel> models;

    public SelectTemplateMenu(EpicPunishments plugin, UUID uuid, Consumer<TemplateModel> callback) {
        super(plugin, uuid, SLOTS, ITEM_SLOTS);

        models = new TemplateController().readAll();

        this.callback = callback;
        open();
    }

    @Override
    public String getMenuName() {
        return "Punish > Select template";
    }

    @Override
    public String getRequiredPermission() {
        return null;
    }

    @Override
    protected ItemStack modelToItemStack(TemplateModel template) {
        return ItemUtils.createItem(Material.PAPER,
                "§b§l" + template.name,
                "§7Reason: §e" + template.reason,
                "§7Duration: §e" + TimeUtils.makeReadable(template.duration),
                "§7Type: §e" + template.type,
                "",
                "§7Click to select template");
    }

    @Override
    protected List<TemplateModel> getModels() {
        return models;
    }

    @Override
    protected void handleModelClick(InventoryClickEvent event, TemplateModel template) {
        callback.accept(template);
    }
}
