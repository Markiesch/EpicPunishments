package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.InputTypes;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.utils.InputUtils;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TemplateStorage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Locale;
import java.util.UUID;

public class EditTemplateMenu extends Menu implements Listener {
    EpicPunishments plugin = EpicPunishments.getInstance();
    private String name = null;
    private String reason = null;
    private String type = "KICK";
    private final UUID uuid;

    private final int NAME_SLOT = 13;
    private final int TYPE_SLOT = 19;
    private final int REASON_SLOT = 25;
    private final int UPDATE_SLOT = 40;

    public String getMenuName() { return "Templates > Edit Template"; }
    public int getSlots() { return 54; }

    public EditTemplateMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        uuid = playerMenuUtility.getUUID();

        ConfigurationSection section = TemplateStorage.getConfig().getConfigurationSection(uuid.toString());
        if (section == null) {
            playerMenuUtility.getOwner().sendMessage("§cThe given template couldn't be found");
            return;
        }

        String name = section.getString("name");
        String reason = section.getString("reason");
        String type = section.getString("type");
        this.name = playerMenuUtility.getTemplateName() == null ? name : playerMenuUtility.getTemplateName();
        this.reason = playerMenuUtility.getReason() == null ? reason : playerMenuUtility.getReason();
        this.type = type == null ? "KICK" : type.toUpperCase(Locale.US);
    }

    public void handleMenu(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();
        Material clickedItem = event.getCurrentItem().getType();

        if (event.getSlot() == NAME_SLOT) {
            plugin.getEditor().put(player.getUniqueId(), new InputUtils(InputTypes.EDIT_TEMPLATE_NAME, player, "§bTemplate Name", "§7Type in a new name"));
            return;
        }

        if (event.getSlot() == REASON_SLOT) {
            plugin.getEditor().put(player.getUniqueId(), new InputUtils(InputTypes.EDIT_TEMPLATE_REASON, player, "§bTemplate Reason", "§7Type in a template reason"));
            return;
        }

        if (event.getSlot() == UPDATE_SLOT) {
            player.closeInventory();
            playerMenuUtility.setTemplateName(null);
            playerMenuUtility.setReason(null);

            if (!TemplateStorage.editTemplate(uuid, name, reason, type)) {
                player.sendMessage("§cAn error accrued whilst trying to save your changes");
                return;
            }

            player.sendMessage("§7Successfully §aedited §7 the template with the name of §e" + name);
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
            setMenuItems();
        }
    }

    public void setMenuItems() {
        String reason = this.reason;
        if (reason == null) reason = "None";

        ItemStack template = ItemUtils.createItem(Material.PAPER, "§c§l" + name, 1, "", "§cType: §7" + type, "§cReason: §7" + reason);
        inventory.setItem(NAME_SLOT, template);

        ItemStack typeItem = ItemUtils.createItem(getType(), "§c§l" + type, 1, "§7Click to toggle type");
        inventory.setItem(TYPE_SLOT, typeItem);

        ItemStack reasonItem = ItemUtils.createItem(Material.WRITABLE_BOOK, "§c§lReason", 1, "§7Click to insert reason", "", "§7Reason set: §c" + reason);
        inventory.setItem(REASON_SLOT, reasonItem);

        ItemStack createItem = ItemUtils.createItem(Material.EMERALD_BLOCK, "§c§lEdit Template", 1, "§7Click to confirm settings", "", "§7Reason set: §c" + reason);
        inventory.setItem(UPDATE_SLOT, createItem);
    }

    public Material getType() {
        Material type = Material.PAPER;
        if ("BAN".equalsIgnoreCase(this.type)) type = Material.OAK_DOOR;
        if ("KICK".equalsIgnoreCase(this.type)) type = Material.ENDER_EYE;
        if ("WARN".equalsIgnoreCase(this.type)) type = Material.PAPER;
        if ("MUTE".equalsIgnoreCase(this.type)) type = Material.STRING;
        return type;
    }
}
