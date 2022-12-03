package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.Menu;
import com.markiesch.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerMenu extends Menu implements Listener {
    private final byte TELEPORT_BUTTON_SLOT = 20;
    private final byte PUNISH_BUTTON_SLOT = 22;
    private final byte INFRACTIONS_BUTTON_SLOT = 24;
    private final byte BACK_BUTTON_SLOT = 40;

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

    public void handleMenu(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;

        switch (event.getSlot()) {
            case TELEPORT_BUTTON_SLOT -> {
                // TODO teleport to player
            }
            case PUNISH_BUTTON_SLOT -> {
                new PunishMenu(plugin, uuid, target.getUniqueId());
            }
            case BACK_BUTTON_SLOT -> new PlayerSelectorMenu(plugin, uuid, 0);
            case INFRACTIONS_BUTTON_SLOT -> new InfractionsMenu(plugin, uuid, target.getUniqueId());
        }
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
        getInventory().setItem(TELEPORT_BUTTON_SLOT, teleportButton);

        ItemStack punishButton = ItemUtils.createItem(
                Material.ANVIL,
                Translation.MENU_PLAYER_PUNISH_TITLE.toString(),
                Translation.MENU_PLAYER_PUNISH_LORE.addPlaceholder("name", target.getName()).toList()
        );
        getInventory().setItem(PUNISH_BUTTON_SLOT, punishButton);

        ItemStack infractions = ItemUtils.createItem(
                Material.FLOWER_BANNER_PATTERN,
                Translation.MENU_PLAYER_INFRACTIONS_TITLE.toString(),
                Translation.MENU_PLAYER_INFRACTIONS_LORE.addPlaceholder("name", target.getName()).toList()
        );
        getInventory().setItem(INFRACTIONS_BUTTON_SLOT, infractions);

        ItemStack back = ItemUtils.createItem(
                Material.OAK_SIGN,
                Translation.MENU_BACK_BUTTON_TITLE.toString(),
                Translation.MENU_BACK_BUTTON_LORE.toList()
        );
        getInventory().setItem(BACK_BUTTON_SLOT, back);
    }
}