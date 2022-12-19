package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.Menu;
import com.markiesch.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerMenu extends Menu implements Listener {
    private static final byte TELEPORT_BUTTON_SLOT = 20;
    private static final byte PUNISH_BUTTON_SLOT = 22;
    private static final byte INFRACTIONS_BUTTON_SLOT = 24;
    private static final byte BACK_BUTTON_SLOT = 40;

    public OfflinePlayer target;

    public PlayerMenu(EpicPunishments plugin, UUID uuid, UUID player) {
        super(plugin, uuid, 45);

        target = Bukkit.getOfflinePlayer(player);
        open();
    }

    @Override
    public String getMenuName() {
        return Translation.MENU_PLAYER_TITLE.addPlaceholder("name", target.getName()).toString();
    }

    @Override
    public String getRequiredPermission() {
        return "epicpunishments.templates";
    }

    public void setMenuItems() {
        if (target == null) {
            getOwner().sendMessage(Translation.MENU_PLAYER_NOT_FOUND.toString());
            getOwner().closeInventory();
            return;
        }

        ItemStack teleportButton = ItemUtils.createItem(
                Material.ENDER_EYE,
                Translation.MENU_PLAYER_TELEPORT_TITLE.toString(),
                Translation.MENU_PLAYER_TELEPORT_LORE.addPlaceholder("name", target.getName()).toList()
        );
        setButton(TELEPORT_BUTTON_SLOT, teleportButton, event -> {
            // TODO teleport to player
        });

        ItemStack punishButton = ItemUtils.createItem(
                Material.ANVIL,
                Translation.MENU_PLAYER_PUNISH_TITLE.toString(),
                Translation.MENU_PLAYER_PUNISH_LORE.addPlaceholder("name", target.getName()).toList()
        );
        setButton(PUNISH_BUTTON_SLOT, punishButton, event -> new PunishMenu(plugin, uuid, target.getUniqueId()));

        ItemStack infractions = ItemUtils.createItem(
                Material.FLOWER_BANNER_PATTERN,
                Translation.MENU_PLAYER_INFRACTIONS_TITLE.toString(),
                Translation.MENU_PLAYER_INFRACTIONS_LORE.addPlaceholder("name", target.getName()).toList()
        );
        setButton(INFRACTIONS_BUTTON_SLOT, infractions, event -> new InfractionsMenu(plugin, uuid, target.getUniqueId()));

        ItemStack back = ItemUtils.createItem(
                Material.OAK_SIGN,
                Translation.MENU_BACK_BUTTON_TITLE.toString(),
                Translation.MENU_BACK_BUTTON_LORE.toList()
        );
        setButton(BACK_BUTTON_SLOT, back, event -> new PlayerSelectorMenu(plugin, uuid));
    }
}