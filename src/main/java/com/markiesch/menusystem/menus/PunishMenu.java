package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.chat.PlayerChat;
import com.markiesch.menusystem.Menu;
import com.markiesch.modules.infraction.InfractionType;
import com.markiesch.modules.infraction.PreparedInfraction;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PunishMenu extends Menu {
    private static final byte REASON_BUTTON_SLOT = 28;
    private static final byte DURATION_BUTTON_SLOT = 29;
    private static final byte TYPE_BUTTON_SLOT = 30;
    private static final byte TEMPLATE_BUTTON_SLOT = 32;
    private static final byte CONFIRM_BUTTON_SLOT = 34;
    private static final byte BACK_BUTTON_SLOT = 49;

    private final OfflinePlayer target;

    private String reason = "none";
    private long duration = 0L;
    private InfractionType type = InfractionType.KICK;

    public PunishMenu(EpicPunishments plugin, UUID uuid, UUID target) {
        super(plugin, uuid, 54);

        this.target = Bukkit.getOfflinePlayer(target);
        open();
    }

    @Override
    public String getMenuName() {
        return "Punish > " + target.getName();
    }

    @Override
    public String getRequiredPermission() {
        return "epicpunishments.punish";
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        switch (event.getSlot()) {
            case REASON_BUTTON_SLOT -> {
                new PlayerChat(plugin, getOwner(), "§b§lReason", "§7Insert a new reason", (String message) -> {
                    reason = message;
                   open();
                });
            }
            case DURATION_BUTTON_SLOT -> {
                new PlayerChat(plugin, getOwner(), "§b§lDuration", "§7Insert a new duration", (String message) -> {
                    duration = TimeUtils.parseTime(message);
                    open();
                });
            }
            case CONFIRM_BUTTON_SLOT -> {
                new PreparedInfraction(type, reason, duration).execute(getOwner(), target);
                getOwner().closeInventory();
            }
            case BACK_BUTTON_SLOT -> {
                new PlayerMenu(plugin, uuid, target.getUniqueId());
            }
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack reasonButton = ItemUtils.createItem(Material.WRITABLE_BOOK, "§b§lReason", "§7Click to insert a reason");
        getInventory().setItem(REASON_BUTTON_SLOT, reasonButton);

        ItemStack durationButton = ItemUtils.createItem(Material.CLOCK, "§b§lDuration", "§7Click to insert a duration");
        getInventory().setItem(DURATION_BUTTON_SLOT, durationButton);

        ItemStack typeButton = ItemUtils.createItem(Material.OAK_DOOR, "§b§lType", "§7Click to cycle type");
        getInventory().setItem(TYPE_BUTTON_SLOT, typeButton);

        ItemStack templateButton = ItemUtils.createItem(Material.ANVIL, "§b§lUse template", "§7Click to use a template");
        getInventory().setItem(TEMPLATE_BUTTON_SLOT, templateButton);

        ItemStack confirmButton = ItemUtils.createItem(Material.EMERALD_BLOCK,
                "§c§lConfirm",
                "§7Click to punish" + target.getName(),
                "",
                "Reason: " + reason,
                "Duration: " + TimeUtils.makeReadable(duration),
                "Type: " + type);

        getInventory().setItem(CONFIRM_BUTTON_SLOT, confirmButton);

        ItemStack back = ItemUtils.createItem(Material.OAK_SIGN, "§b§lBack", "§7Click to go back");
        getInventory().setItem(BACK_BUTTON_SLOT, back);
    }
}
