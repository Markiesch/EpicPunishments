package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.chat.PlayerChat;
import com.markiesch.menusystem.Menu;
import com.markiesch.modules.template.TemplateController;
import com.markiesch.modules.template.TemplateModel;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class EditTemplateMenu extends Menu {
    private static final byte SLOTS = 54;

    private static final byte INFO_SLOT = 4;
    private static final byte NAME_SLOT = 20;
    private static final byte REASON_SLOT = 21;
    private static final byte TYPE_SLOT = 23;
    private static final byte DURATION_SLOT = 24;
    private static final byte SAVE_SLOT = 40;

    private final TemplateController templateController;
    private final TemplateModel template;

    private String name;
    private String reason;
    private long duration;
    private String type;

    public EditTemplateMenu(EpicPunishments plugin, UUID uuid, int id) {
        super(plugin, uuid, SLOTS);

        templateController = new TemplateController();
        template = templateController.readSingle(id);

        System.out.println(template.type);

        this.name = template.name;
        this.reason = template.reason;
        this.duration = template.duration;
        this.type = template.type;
        open();
    }

    @Override
    public String getMenuName() {
        return "Templates > Edit Template";
    }

    @Override
    public String getRequiredPermission() {
        return null;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case NAME_SLOT -> {
                new PlayerChat(plugin, getOwner(), "§bTemplate Name", "§7Type in a new name", (String message) -> {
                    this.name = message;
                    System.out.println("test " + message);
                    open();
                });
            }
            case TYPE_SLOT -> {
                System.out.println(type);
                if ("BAN".equalsIgnoreCase(type)) type = "KICK";
                else if ("KICK".equalsIgnoreCase(type)) type = "WARN";
                else if ("WARN".equalsIgnoreCase(type)) type = "MUTE";
                else if ("MUTE".equalsIgnoreCase(type)) type = "BAN";
                setMenuItems();
            }
            case DURATION_SLOT -> {
                player.sendMessage("§7Please type in a valid time\n§ay §7- §eYear\n§ad §7- §eDay\n§am §7- §eMinute\n§as §7- §eSecond");

                new PlayerChat(plugin, getOwner(), "§bTemplate Duration", "§7Type in a template duration", (String message) -> {
                    this.duration = TimeUtils.parseTime(message);
                    open();
                });
            }
            case REASON_SLOT -> {
                new PlayerChat(plugin, getOwner(), "§bTemplate Reason", "§7Type in a template reason", (String message) -> {
                    this.reason = message;
                    open();
                });
            }
            case SAVE_SLOT -> {
                templateController.updateTemplate(template.id, name, type, reason, duration);
                player.sendMessage("§7Successfully§a saved§7 template §e" + name);
                new TemplateSelectorMenu(plugin, uuid);
            }
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack infoItem = ItemUtils.createItem(
                Material.NETHER_STAR,
                "§b§lEdit template",
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

        ItemStack saveItem = ItemUtils.createItem(Material.EMERALD_BLOCK, "§b§lSave Template", "§7Click to save changes");
        getInventory().setItem(SAVE_SLOT, saveItem);
    }
}