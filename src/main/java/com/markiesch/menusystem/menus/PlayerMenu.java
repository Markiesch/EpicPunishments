package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerSelectorSearchType;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.profile.ProfileController;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class PlayerMenu extends Menu implements Listener {
    private final byte TELEPORT_BUTTON_SLOT = 29;
    private final byte PUNISH_BUTTON_SLOT = 31;
    private final byte INFRACTIONS_BUTTON_SLOT = 33;
    private final byte BACK_BUTTON_SLOT = 49;

    public OfflinePlayer target;

    public PlayerMenu(EpicPunishments plugin, UUID uuid, UUID player) {
        super(plugin, uuid, 54);

        target = Bukkit.getOfflinePlayer(player);
        open();
    }

    @Override
    public String getMenuName() {
        return "Player > " + target.getName();
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
            getOwner().sendMessage("§cCouldn't find player. Closing menu...");
            getOwner().closeInventory();
            return;
        }

        ItemStack teleportButton = ItemUtils.createItem(Material.ENDER_EYE, "§b§lTeleport", "§7Click to teleport to §e" + target.getName());
        getInventory().setItem(TELEPORT_BUTTON_SLOT, teleportButton);

        ItemStack punishButton = ItemUtils.createItem(Material.ANVIL, "§c§lPunish", "§7Click to punish §e" + target.getName());
        getInventory().setItem(PUNISH_BUTTON_SLOT, punishButton);

        ItemStack infractions = ItemUtils.createItem(Material.FLOWER_BANNER_PATTERN, "§c§lInfractions", "§7Click to view infractions");
        getInventory().setItem(INFRACTIONS_BUTTON_SLOT, infractions);

        ItemStack back = ItemUtils.createItem(Material.OAK_SIGN, "§b§lBack", "§7Click to go back");
        getInventory().setItem(BACK_BUTTON_SLOT, back);

        getInventory().setItem(13, ItemUtils.createPlayerInfoHead(target.getUniqueId()));
    }

    @Override
    public String getRequiredPermission() {
        return "epicpunishments.templates";
    }
}