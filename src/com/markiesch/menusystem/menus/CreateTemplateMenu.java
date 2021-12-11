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

import java.util.UUID;

public class CreateTemplateMenu extends Menu implements Listener {
    private final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);
    private final String name;
    private final String reason;
    private String type = "KICK";

    public String getMenuName() { return "Templates > Create Template"; }
    public int getSlots() { return 54; }

    public CreateTemplateMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        this.name = playerMenuUtility.getTemplateName();
        this.reason = playerMenuUtility.getReason();

        ConfigurationSection section = TemplateStorage.getConfig().getConfigurationSection(name);
        if (section != null) {
            playerMenuUtility.getOwner().sendMessage("§cA template with this name already exist");
            playerMenuUtility.getOwner().closeInventory();
        }
    }

    public void handleMenu(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        Material clickedItem = event.getCurrentItem().getType();

        if (clickedItem.equals(Material.WRITABLE_BOOK)) {
            plugin.getEditor().put(uuid, new InputUtils(InputTypes.CREATE_TEMPLATE_REASON, player, "§bTemplate Reason", "§7Type in a template reason"));
            return;
        }

        if (clickedItem.equals(Material.EMERALD_BLOCK)) {
            PlayerMenuUtility playerMenuUtility = EpicPunishments.getPlayerMenuUtility(player);
            String name = playerMenuUtility.getTemplateName();
            String reason = playerMenuUtility.getReason();
            TemplateStorage.addTemplate(name, reason, type);
            player.closeInventory();
            player.sendMessage("§7Successfully §acreated §7 the template with the name of §e" + name);
            return;
        }

        if (event.getSlot() == 19) {
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
        inventory.setItem(13, template);

        ItemStack typeItem = ItemUtils.createItem(getType(), "§c§l" + type, 1, "§7Click to toggle type");
        inventory.setItem(19, typeItem);

        ItemStack reasonItem = ItemUtils.createItem(Material.WRITABLE_BOOK, "§c§lReason", 1, "§7Click to insert reason", "", "§7Reason set: §c" + reason);
        inventory.setItem(25, reasonItem);

        ItemStack createItem = ItemUtils.createItem(Material.EMERALD_BLOCK, "§c§lCreate Template", 1, "§7Click to confirm settings", "", "§7Reason set: §c" + reason);
        inventory.setItem(40, createItem);
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
