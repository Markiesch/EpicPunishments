package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.controllers.TemplateController;
import com.markiesch.utils.InputTypes;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.MenuUtils;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.utils.InputUtils;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class EditTemplateMenu extends Menu implements Listener {
    private final EpicPunishments plugin;
    private final TemplateController templateController;
    private String name;
    private String reason;
    private Long duration;
    private String type;
    private UUID uuid;

    private final int NAME_SLOT = 13;
    private final int TYPE_SLOT = 19;
    private final int DURATION_SLOT = 22;
    private final int REASON_SLOT = 25;
    private final int UPDATE_SLOT = 40;

    public EditTemplateMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility, "Templates > Edit Template", 54);
        plugin = EpicPunishments.getInstance();
        templateController = EpicPunishments.getTemplateController();

        if (!hasPermission()) return;

        uuid = playerMenuUtility.getUUID();

        ConfigurationSection section = templateController.getConfigurationSection(uuid.toString());
        if (section == null) {
            playerMenuUtility.getOwner().sendMessage("§cThe given template couldn't be found");
            playerMenuUtility.getOwner().closeInventory();
            return;
        }

        String name = section.getString("name");
        String reason = section.getString("reason");
        long duration = section.getLong("duration");
        String type = section.getString("type");

        this.name = playerMenuUtility.getTemplateName() == null ? name : playerMenuUtility.getTemplateName();
        this.reason = playerMenuUtility.getReason() == null ? reason : playerMenuUtility.getReason();
        this.duration = playerMenuUtility.getDuration() == null ? duration : playerMenuUtility.getDuration();
        this.type = playerMenuUtility.getType() == null ? type == null ? "KICK" : type : playerMenuUtility.getType();
        open();
    }

    public void handleMenu(InventoryClickEvent event) {
        if (event.getCurrentItem() == null || !hasPermission()) return;
        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() == NAME_SLOT) {
            plugin.getEditor().put(player.getUniqueId(), new InputUtils(InputTypes.EDIT_TEMPLATE_NAME, player, "§bTemplate Name", "§7Type in a new name"));
            return;
        }

        if (event.getSlot() == REASON_SLOT) {
            plugin.getEditor().put(player.getUniqueId(), new InputUtils(InputTypes.EDIT_TEMPLATE_REASON, player, "§bTemplate Reason", "§7Type in a template reason"));
            return;
        }

        if (event.getSlot() == DURATION_SLOT) {
            player.sendMessage("§7Please type in a valid time\n§ay §7- §eYear\n§ad §7- §eDay\n§am §7- §eMinute\n§as §7- §eSecond");
            plugin.getEditor().put(player.getUniqueId(), new InputUtils(InputTypes.EDIT_TEMPLATE_DURATION, player, "§bTemplate Duration", "§7Type in a template duration"));
            return;
        }

        if (event.getSlot() == UPDATE_SLOT) {
            player.closeInventory();
            playerMenuUtility.reset();

            if (!templateController.editTemplate(uuid, name, reason, type, duration)) {
                player.sendMessage("§cAn error accrued whilst trying to save your changes");
                return;
            }

            new TemplatesMenu(EpicPunishments.getPlayerMenuUtility(player), 0);
            player.sendMessage("§7Successfully§a edited §7the template with the name of §e" + name);
            return;
        }

        if (event.getSlot() == TYPE_SLOT) {
            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();
            if (meta == null) return;
            String name = ChatColor.stripColor(meta.getDisplayName());
            if ("BAN".equalsIgnoreCase(name)) type = "KICK";
            if ("KICK".equalsIgnoreCase(name)) type = "WARN";
            if ("WARN".equalsIgnoreCase(name)) type = "MUTE";
            if ("MUTE".equalsIgnoreCase(name)) type = "BAN";
            playerMenuUtility.setType(type);
            setMenuItems();
        }
    }

    public void setMenuItems() {
        String reason = this.reason;
        if (reason == null) reason = "None";

        ItemStack template = ItemUtils.createItem(Material.PAPER, "§c§l" + name, "", "§cType: §7" + type, "§cReason: §7" + reason);
        getInventory().setItem(NAME_SLOT, template);

        ItemStack typeItem = ItemUtils.createItem(MenuUtils.getMaterialType(this.type), "§c§l" + type, "§7Click to toggle type");
        getInventory().setItem(TYPE_SLOT, typeItem);

        ItemStack timeItem = ItemUtils.createItem(Material.CLOCK, "§c§lDuration", "§7Click to insert duration", "", "§7Duration set: §c" + TimeUtils.makeReadable(duration));
        getInventory().setItem(DURATION_SLOT, timeItem);

        ItemStack reasonItem = ItemUtils.createItem(Material.WRITABLE_BOOK, "§c§lReason", "§7Click to insert reason", "", "§7Reason set: §c" + reason);
        getInventory().setItem(REASON_SLOT, reasonItem);

        ItemStack createItem = ItemUtils.createItem(Material.EMERALD_BLOCK, "§c§lEdit Template", "§7Click to confirm settings", "", "§7Reason set: §c" + reason);
        getInventory().setItem(UPDATE_SLOT, createItem);
    }

    public boolean hasPermission() {
        Player player = playerMenuUtility.getOwner();
        if (player.hasPermission("epicpunishments.templates.manage")) return true;
        player.sendMessage("§7You do not have§c permissions§7 to edit templates");
        player.closeInventory();
        return false;
    }
}
