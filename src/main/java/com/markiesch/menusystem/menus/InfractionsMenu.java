package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.PaginatedModelMenu;
import com.markiesch.modules.infraction.InfractionList;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class InfractionsMenu extends PaginatedModelMenu<InfractionModel> {
    private final OfflinePlayer target;
    private final static int BACK_BUTTON_SLOT = 49;

    private final static int[] SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
    private final InfractionList models;

    public InfractionsMenu(EpicPunishments plugin, UUID uuid, UUID target) {
        super(plugin, uuid, 54, SLOTS);

        this.target = Bukkit.getOfflinePlayer(target);
        models = InfractionManager.getInstance().getPlayer(this.target.getUniqueId());
        open();
    }

    @Override
    public String getMenuName() {
        return Translation.MENU_INFRACTIONS_TITLE.addPlaceholder("name", target.getName()).toString();
    }

    @Override
    public String getRequiredPermission() {
        return null;
    }

    @Override
    protected ItemStack modelToItemStack(InfractionModel model) {
        return ItemUtils.createItem(
                Material.PAPER,
                Translation.MENU_INFRACTIONS_BUTTON_TITLE
                        .addPlaceholder("type", model.type)
                        .toString(),
                Translation.MENU_INFRACTIONS_BUTTON_LORE
                        .addPlaceholder("reason", model.reason)
                        .addPlaceholder("duration", TimeUtils.makeReadable(model.duration))
                        .addPlaceholder("issuer", model.getIssuer())
                        .toList()
        );
    }

    @Override
    protected List<InfractionModel> getModels() {
        return models;
    }

    @Override
    protected void handleModelClick(InventoryClickEvent event, InfractionModel model) {
        if (event.getAction() != InventoryAction.DROP_ONE_SLOT) return;

        InfractionManager.getInstance().deletePunishment(model);
        open();
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        super.handleMenu(event);

        if (event.getSlot() == BACK_BUTTON_SLOT) {
            new PlayerMenu(plugin, uuid, target.getUniqueId());
        }
    }

    @Override
    public void setMenuItems() {
        super.setMenuItems();

        ItemStack backButton = ItemUtils.createItem(Material.OAK_SIGN, Translation.MENU_BACK_BUTTON_TITLE.toString(), Translation.MENU_BACK_BUTTON_LORE.toList());
        getInventory().setItem(BACK_BUTTON_SLOT, backButton);
    }
}
