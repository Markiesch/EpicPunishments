package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.InputTypes;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.MenuUtils;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.utils.InputUtils;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TemplateStorage;
import com.markiesch.utils.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CreateTemplateMenu extends Menu implements Listener {
    EpicPunishments plugin = EpicPunishments.getInstance();
    private final String name;
    private final String reason;
    private final Long duration;
    private String type;

    private final int NAME_SLOT = 13;
    private final int TYPE_SLOT = 19;
    private final int DURATION_SLOT = 22;
    private final int REASON_SLOT = 25;
    private final int CREATE_SLOT = 40;

    public String getMenuName() { return "Templates > Create Template"; }
    public int getSlots() { return 54; }

    public CreateTemplateMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);

        playerMenuUtility.fillEmptyFields();

        name = playerMenuUtility.getTemplateName();
        reason = playerMenuUtility.getReason();
        duration = playerMenuUtility.getDuration();
        type = playerMenuUtility.getType();
    }

    public void handleMenu(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() == NAME_SLOT) {
            plugin.getEditor().put(player.getUniqueId(), new InputUtils(InputTypes.CREATE_TEMPLATE_NAME, player, "§bTemplate Name", "§7Type in a new name"));
            return;
        }

        if (event.getSlot() == TYPE_SLOT) {
            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();
            if (meta == null) return;
            String name = ChatColor.stripColor(meta.getDisplayName());
            if ("BAN".equalsIgnoreCase(name)) type = "KICK";
            else if ("KICK".equalsIgnoreCase(name)) type = "WARN";
            else if ("WARN".equalsIgnoreCase(name)) type = "MUTE";
            else if ("MUTE".equalsIgnoreCase(name)) type = "BAN";
            playerMenuUtility.setType(type);
            setMenuItems();
            return;
        }

        if (event.getSlot() == DURATION_SLOT) {
            player.sendMessage("§7Please type in a valid time\n§ay §7- §eYear\n§ad §7- §eDay\n§am §7- §eMinute\n§as §7- §eSecond");
            plugin.getEditor().put(player.getUniqueId(), new InputUtils(InputTypes.CREATE_TEMPLATE_DURATION, player, "§bTemplate Duration", "§7Type in a template duration"));
            return;
        }

        if (event.getSlot() == REASON_SLOT) {
            plugin.getEditor().put(player.getUniqueId(), new InputUtils(InputTypes.CREATE_TEMPLATE_REASON, player, "§bTemplate Reason", "§7Type in a template reason"));
            return;
        }

        if (event.getSlot() == CREATE_SLOT) {
            PlayerMenuUtility playerMenuUtility = EpicPunishments.getPlayerMenuUtility(player);
            TemplateStorage.addTemplate(name, reason, type, duration);
            playerMenuUtility.reset();
            player.sendMessage("§7Successfully§a created§7 the template with the name of §e" + name);
            new TemplatesMenu(EpicPunishments.getPlayerMenuUtility(player), 0).open();
        }
    }

    public void setMenuItems() {
        String reason = this.reason;
        if (reason == null) reason = "None";

        ItemStack template = ItemUtils.createItem(Material.PAPER, "§c§l" + name, "§7Click to insert a new name", "", "§cType: §7" + type, "§cReason: §7" + reason, "§cDuration: §7" + TimeUtils.makeReadable(duration));
        inventory.setItem(NAME_SLOT, template);

        ItemStack typeItem = ItemUtils.createItem(MenuUtils.getMaterialType(this.type), "§c§l" + type, "§7Click to toggle type");
        inventory.setItem(TYPE_SLOT, typeItem);

        ItemStack timeItem = ItemUtils.createItem(Material.CLOCK, "§c§lDuration", "§7Click to insert duration", "", "§7Duration set: §c" + TimeUtils.makeReadable(duration));
        inventory.setItem(DURATION_SLOT, timeItem);

        ItemStack reasonItem = ItemUtils.createItem(Material.WRITABLE_BOOK, "§c§lReason", "§7Click to insert reason", "", "§7Reason set: §c" + reason);
        inventory.setItem(REASON_SLOT, reasonItem);

        ItemStack createItem = ItemUtils.createItem(Material.EMERALD_BLOCK, "§c§lCreate Template", "§7Click to confirm settings", "", "§7Reason set: §c" + reason);
        inventory.setItem(CREATE_SLOT, createItem);
    }
}
