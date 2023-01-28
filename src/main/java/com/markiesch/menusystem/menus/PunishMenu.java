package com.markiesch.menusystem.menus;

import com.markiesch.Permission;
import com.markiesch.chat.PlayerChat;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.Menu;
import com.markiesch.modules.infraction.InfractionType;
import com.markiesch.modules.infraction.PreparedInfraction;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class PunishMenu extends Menu {
    private static final byte REASON_BUTTON_SLOT = 19;
    private static final byte DURATION_BUTTON_SLOT = 20;
    private static final byte TYPE_BUTTON_SLOT = 21;
    private static final byte TEMPLATE_BUTTON_SLOT = 23;
    private static final byte CONFIRM_BUTTON_SLOT = 25;
    private static final byte BACK_BUTTON_SLOT = 40;

    private final ProfileModel target;

    private String reason = "none";
    private long duration = 0L;
    private InfractionType type = InfractionType.KICK;

    public PunishMenu(Plugin plugin, UUID uuid, UUID target) {
        super(plugin, uuid, 45);

        this.target = ProfileManager.getInstance().getPlayer(target);
        open();
    }

    @Override
    public String getMenuName() {
        return Translation.MENU_PUNISH_TITLE.addPlaceholder("name", target.getName()).toString();
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.MANAGE_PLAYERS;
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
        setButton(REASON_BUTTON_SLOT, reasonButton, event -> {
            new PlayerChat(plugin, getOwner(), Translation.MENU_PUNISH_INSERT_REASON_TITLE.toString(), Translation.MENU_PUNISH_INSERT_REASON_SUBTITLE.toString(), (message) -> {
                reason = message;
                open();
            });
        });

        ItemStack typeButton = ItemUtils.createItem(
                type.getMaterial(),
                Translation.MENU_PUNISH_BUTTON_TYPE_TITLE.toString(),
                Translation.MENU_PUNISH_BUTTON_TYPE_LORE.addPlaceholder("type", type).toList()
        );
        setButton(TYPE_BUTTON_SLOT, typeButton, event -> {
            type = type.getNextType();
            setMenuItems();
        });

        ItemStack durationButton = ItemUtils.createItem(
                Material.CLOCK,
                Translation.MENU_PUNISH_BUTTON_DURATION_TITLE.toString(),
                Translation.MENU_PUNISH_BUTTON_DURATION_LORE
                        .addPlaceholder("duration", TimeUtils.makeReadable(duration))
                        .toList()
        );
        setButton(DURATION_BUTTON_SLOT, durationButton, event -> {
            new PlayerChat(plugin, getOwner(), Translation.MENU_PUNISH_INSERT_DURATION_TITLE.toString(), Translation.MENU_PUNISH_INSERT_DURATION_SUBTITLE.toString(), (message) -> {
                duration = TimeUtils.parseTime(message);
                open();
            });
        });

        ItemStack templateButton = ItemUtils.createItem(
                Material.ANVIL,
                Translation.MENU_PUNISH_BUTTON_TEMPLATE_TITLE.toString(),
                Translation.MENU_PUNISH_BUTTON_TEMPLATE_LORE.toList()
        );
        setButton(TEMPLATE_BUTTON_SLOT, templateButton, event -> {
            new SelectTemplateMenu(plugin, uuid, (template) -> {
                if (template != null) {
                    this.reason = template.getReason();
                    this.duration = template.getDuration();
                    this.type = template.getType();
                }
                open();
            });
        });

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
        setButton(CONFIRM_BUTTON_SLOT, confirmButton, event -> {
            new PreparedInfraction(type, getOwner(), target, reason, duration).execute();
            close();
        });

        ItemStack back = ItemUtils.createItem(
                Material.OAK_SIGN,
                Translation.MENU_BACK_BUTTON_TITLE.toString(),
                Translation.MENU_BACK_BUTTON_LORE.toList()
        );
        setButton(BACK_BUTTON_SLOT, back, event -> new PlayerMenu(plugin, uuid, target.uuid));
    }
}
