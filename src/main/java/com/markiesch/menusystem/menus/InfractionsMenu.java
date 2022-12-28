package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.PaginatedModelMenu;
import com.markiesch.modules.infraction.InfractionList;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class InfractionsMenu extends PaginatedModelMenu<InfractionModel> {
    private final static byte[] SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
    private final static byte BACK_BUTTON_SLOT = 49;

    private final ProfileModel target;
    private InfractionList models;

    public InfractionsMenu(EpicPunishments plugin, UUID uuid, UUID targetUUID) {
        super(plugin, uuid, 54, SLOTS);

        target = ProfileManager.getInstance().getPlayer(targetUUID);
        if (target == null) return;

        models = InfractionManager.getInstance().getPlayer(target.uuid);
        open();
    }

    @Override
    public String getMenuName() {
        return Translation.MENU_INFRACTIONS_TITLE.addPlaceholder("name", target.getName()).toString();
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }

    @Override
    protected ItemStack modelToItemStack(InfractionModel model) {
        return ItemUtils.createItem(
                Material.PAPER,
                Translation.MENU_INFRACTIONS_BUTTON_TITLE
                        .addPlaceholder("type", model.type)
                        .addPlaceholder("state",
                                (model.revoked ? Translation.WORD_REVOKED : model.isActive() ? Translation.WORD_ACTIVE : Translation.WORD_EXPIRED)
                                        .toString()
                                        .toUpperCase(Locale.ROOT))
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
    public void setMenuItems() {
        super.setMenuItems();

        ItemStack backButton = ItemUtils.createItem(Material.OAK_SIGN, Translation.MENU_BACK_BUTTON_TITLE.toString(), Translation.MENU_BACK_BUTTON_LORE.toList());
        setButton(BACK_BUTTON_SLOT, backButton, event -> new PlayerMenu(plugin, uuid, target.uuid));
    }
}
