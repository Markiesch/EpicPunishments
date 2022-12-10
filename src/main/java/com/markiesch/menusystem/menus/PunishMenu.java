package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.chat.PlayerChat;
import com.markiesch.locale.Translation;
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
    private static final byte REASON_BUTTON_SLOT = 19;
    private static final byte DURATION_BUTTON_SLOT = 20;
    private static final byte TYPE_BUTTON_SLOT = 21;
    private static final byte TEMPLATE_BUTTON_SLOT = 23;
    private static final byte CONFIRM_BUTTON_SLOT = 25;
    private static final byte BACK_BUTTON_SLOT = 40;

    private final OfflinePlayer target;

    private String reason = "none";
    private long duration = 0L;
    private InfractionType type = InfractionType.KICK;

    public PunishMenu(EpicPunishments plugin, UUID uuid, UUID target) {
        super(plugin, uuid, 45);

        this.target = Bukkit.getOfflinePlayer(target);
        open();
    }

    @Override
    public String getMenuName() {
        return Translation.MENU_PUNISH_TITLE.addPlaceholder("name", target.getName()).toString();
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
            case TYPE_BUTTON_SLOT -> {
                type = type.getNextType();
                setMenuItems();
            }
            case CONFIRM_BUTTON_SLOT -> {
                new PreparedInfraction(type, getOwner(), target, reason, duration).execute();
                getOwner().closeInventory();
            }
            case BACK_BUTTON_SLOT -> {
                new PlayerMenu(plugin, uuid, target.getUniqueId());
            }
            case TEMPLATE_BUTTON_SLOT -> {
                new SelectTemplateMenu(plugin, uuid, (template) -> {
                    if (template != null) {
                        this.reason = template.reason;
                        this.duration = template.duration;
                        this.type = template.type;
                    }
                    open();
                });
            }
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack reasonButton = ItemUtils.createItem(
                Material.WRITABLE_BOOK,
                Translation.MENU_PUNISH_BUTTON_REASON_TITLE.toString(),
                Translation.MENU_PUNISH_BUTTON_REASON_LORE
                        .addPlaceholder("reason", reason)
                        .toList()
        );
        getInventory().setItem(REASON_BUTTON_SLOT, reasonButton);

        ItemStack typeButton = ItemUtils.createItem(
                type.getMaterial(),
                Translation.MENU_PUNISH_BUTTON_TYPE_TITLE.toString(),
                Translation.MENU_PUNISH_BUTTON_TYPE_LORE.addPlaceholder("type", type).toList()
        );
        getInventory().setItem(TYPE_BUTTON_SLOT, typeButton);

        ItemStack durationButton = ItemUtils.createItem(
                Material.CLOCK,
                Translation.MENU_PUNISH_BUTTON_DURATION_TITLE.toString(),
                Translation.MENU_PUNISH_BUTTON_DURATION_LORE
                        .addPlaceholder("duration", TimeUtils.makeReadable(duration))
                        .toList()
        );
        getInventory().setItem(DURATION_BUTTON_SLOT, durationButton);

        ItemStack templateButton = ItemUtils.createItem(
                Material.ANVIL,
                Translation.MENU_PUNISH_BUTTON_TEMPLATE_TITLE.toString(),
                Translation.MENU_PUNISH_BUTTON_TEMPLATE_LORE.toList()
        );
        getInventory().setItem(TEMPLATE_BUTTON_SLOT, templateButton);

        ItemStack confirmButton = ItemUtils.createItem(
                Material.EMERALD_BLOCK,
                Translation.MENU_PUNISH_BUTTON_CONFIRM_TITLE.toString(),
                Translation.MENU_PUNISH_BUTTON_CONFIRM_LORE
                        .addPlaceholder("name", target.getName())
                        .addPlaceholder("reason", reason)
                        .addPlaceholder("duration", TimeUtils.makeReadable(duration))
                        .addPlaceholder("type", type)
                        .toList()
        );
        getInventory().setItem(CONFIRM_BUTTON_SLOT, confirmButton);

        ItemStack back = ItemUtils.createItem(
                Material.OAK_SIGN,
                Translation.MENU_BACK_BUTTON_TITLE.toString(),
                Translation.MENU_BACK_BUTTON_LORE.toList()
        );
        getInventory().setItem(BACK_BUTTON_SLOT, back);
    }
}
