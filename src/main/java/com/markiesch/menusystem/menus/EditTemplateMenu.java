package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.modules.template.TemplateController;
import com.markiesch.utils.InputTypes;
import com.markiesch.utils.InputUtils;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EditTemplateMenu extends Menu {
    private static final byte SLOTS = 54;

    private static final byte INFO_SLOT = 4;
    private static final byte NAME_SLOT = 20;
    private static final byte REASON_SLOT = 21;
    private static final byte TYPE_SLOT = 23;
    private static final byte DURATION_SLOT = 24;
    private static final byte CREATE_SLOT = 40;

    private final TemplateController templateController;

    private final String name;
    private final String reason;
    private final long duration;
    private String type;

    public EditTemplateMenu(EpicPunishments plugin, PlayerMenuUtility playerMenuUtility) {
        super(plugin, playerMenuUtility, SLOTS);

        templateController = new TemplateController();

        playerMenuUtility.fillEmptyFields();
        name = playerMenuUtility.getTemplateName();
        reason = playerMenuUtility.getReason();
        duration = playerMenuUtility.getTemplateDuration();
        type = playerMenuUtility.getType();

        open();
    }

    @Override
    public String getMenuName() {
        return "Templates > Edit > " + name;
    }

    @Override
    public String getRequiredPermission() {
        return null;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() == NAME_SLOT) {
            plugin.getEditor().put(player.getUniqueId(), new InputUtils(plugin, InputTypes.CREATE_TEMPLATE_NAME, player, "§bTemplate Name", "§7Type in a new name"));
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
            plugin.getEditor().put(player.getUniqueId(), new InputUtils(plugin, InputTypes.CREATE_TEMPLATE_DURATION, player, "§bTemplate Duration", "§7Type in a template duration"));
            return;
        }

        if (event.getSlot() == REASON_SLOT) {
            plugin.getEditor().put(player.getUniqueId(), new InputUtils(plugin, InputTypes.CREATE_TEMPLATE_REASON, player, "§bTemplate Reason", "§7Type in a template reason"));
            return;
        }

        if (event.getSlot() == CREATE_SLOT) {
            templateController.addTemplate(name, reason, type, duration);
            playerMenuUtility.reset();
            player.sendMessage("§7Successfully§a created§7 the template with the name of §e" + name);
            new TemplateSelectorMenu(plugin, playerMenuUtility);
        }
    }

    @Override
    public void setMenuItems() {
        String reason = this.reason;
        if (reason == null) reason = "None";

        ItemStack infoItem = ItemUtils.createItem(
                Material.NETHER_STAR,
                "§b§lNew template",
                "",
                "§eName: §7" + name,
                "§eType: §7" + type,
                "§eReason: §7" + reason,
                "§eDuration: §7" + TimeUtils.makeReadable(duration)
        );
        getInventory().setItem(INFO_SLOT, infoItem);

        ItemStack nameItem = ItemUtils.createItem(
                Material.PAPER,
                "§b§lName",
                "§7Click to insert a new name",
                "",
                "§eCurrent name: §7" + name
        );
        getInventory().setItem(NAME_SLOT, nameItem);

        ItemStack typeItem = ItemUtils.createItem(
                Material.OAK_DOOR,
                "§b§lType",
                "§7Click to toggle type",
                "",
                "§eCurrent type: §7" + type
        );
        getInventory().setItem(TYPE_SLOT, typeItem);

        ItemStack timeItem = ItemUtils.createItem(Material.CLOCK, "§b§lDuration", "§7Click to insert duration", "", "§7Duration set: §e" + TimeUtils.makeReadable(duration));
        getInventory().setItem(DURATION_SLOT, timeItem);

        ItemStack reasonItem = ItemUtils.createItem(Material.WRITABLE_BOOK, "§b§lReason", "§7Click to insert reason", "", "§7Reason set: §e" + reason);
        getInventory().setItem(REASON_SLOT, reasonItem);

        ItemStack createItem = ItemUtils.createItem(Material.EMERALD_BLOCK, "§b§lCreate Template", "§7Click to create template");
        getInventory().setItem(CREATE_SLOT, createItem);
    }
}