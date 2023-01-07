package com.markiesch.menusystem.menus;

import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.Menu;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class PlayerMenu extends Menu implements Listener {
    private static final byte PUNISH_BUTTON_SLOT = 21;
    private static final byte INFRACTIONS_BUTTON_SLOT = 23;
    private static final byte BACK_BUTTON_SLOT = 40;

    public ProfileModel target;

    public PlayerMenu(Plugin plugin, UUID uuid, UUID player) {
        super(plugin, uuid, 45);

        target = ProfileManager.getInstance().getPlayer(player);
        open();
    }

    @Override
    public String getMenuName() {
        return Translation.MENU_PLAYER_TITLE.addPlaceholder("name", target.getName()).toString();
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.MANAGE_PLAYERS;
    }

    public void setMenuItems() {
        if (target == null) {
            getOwner().sendMessage(Translation.MENU_PLAYER_NOT_FOUND.toString());
            getOwner().closeInventory();
            return;
        }

        ItemStack punishButton = ItemUtils.createItem(
                Material.ANVIL,
                Translation.MENU_PLAYER_PUNISH_TITLE.toString(),
                Translation.MENU_PLAYER_PUNISH_LORE.addPlaceholder("name", target.getName()).toList()
        );
        setButton(PUNISH_BUTTON_SLOT, punishButton, event -> new PunishMenu(plugin, uuid, target.uuid));

        ItemStack infractions = ItemUtils.createItem(
                Material.FLOWER_BANNER_PATTERN,
                Translation.MENU_PLAYER_INFRACTIONS_TITLE.toString(),
                Translation.MENU_PLAYER_INFRACTIONS_LORE.addPlaceholder("name", target.getName()).toList()
        );
        setButton(INFRACTIONS_BUTTON_SLOT, infractions, event -> new InfractionsMenu(plugin, uuid, target.uuid));

        ItemStack back = ItemUtils.createItem(
                Material.OAK_SIGN,
                Translation.MENU_BACK_BUTTON_TITLE.toString(),
                Translation.MENU_BACK_BUTTON_LORE.toList()
        );
        setButton(BACK_BUTTON_SLOT, back, event -> new PlayerSelectorMenu(plugin, uuid));
    }
}