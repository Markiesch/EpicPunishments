package com.markiesch.menusystem.menus;

import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.PaginatedModelMenu;
import com.markiesch.modules.infraction.InfractionList;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.infraction.InfractionType;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class HistoryMenu extends PaginatedModelMenu<InfractionModel> {
    private final static byte[] SLOTS = {19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
    private final static byte PLAYER_BUTTON_SLOT = 4;
    private final static byte BACK_BUTTON_SLOT = 49;

    private final ProfileModel target;
    private InfractionList models;

    public HistoryMenu(Plugin plugin, UUID uuid, UUID targetUUID) {
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
        return Permission.HISTORY_MENU;
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

        InfractionList banList = models.getByType(InfractionType.BAN);
        InfractionList muteList = models.getByType(InfractionType.MUTE);

        ItemStack headButton = ItemUtils.createItem(Material.PLAYER_HEAD,
                Translation.MENU_INFRACTION_PLAYER_TITLE.addPlaceholder("name", target.getName()).toString(),
                Translation.MENU_INFRACTION_PLAYER_LORE
                        .addPlaceholder("ban_format",
                                (banList.isEmpty() ?
                                        Translation.MENU_INFRACTION_PLAYER_EMPTY :
                                        Translation.MENU_INFRACTION_PLAYER_FILLED.addPlaceholder("size", banList.size())
                                ).addPlaceholder("punish_type", Translation.WORD_BANNED.toString()).toString()
                        )
                        .addPlaceholder("mute_format",
                                (muteList.isEmpty() ?
                                        Translation.MENU_INFRACTION_PLAYER_EMPTY :
                                        Translation.MENU_INFRACTION_PLAYER_FILLED.addPlaceholder("size", muteList.size())
                                ).addPlaceholder("punish_type", Translation.WORD_MUTED.toString()).toString()
                        )
                        .toList()
        );
        ItemUtils.setSkullOwner(headButton, target.getPlayer());
        setButton(PLAYER_BUTTON_SLOT, headButton);

        ItemStack backButton = ItemUtils.createItem(Material.OAK_SIGN, Translation.MENU_BACK_BUTTON_TITLE.toString(), Translation.MENU_BACK_BUTTON_LORE.toList());
        setButton(BACK_BUTTON_SLOT, backButton, event -> new PlayerMenu(plugin, uuid, target.uuid));
    }
}
