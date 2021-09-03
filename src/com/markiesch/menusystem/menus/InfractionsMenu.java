package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class InfractionsMenu extends Menu {
    private final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);
    public OfflinePlayer target;
    int page = 0;

    public InfractionsMenu(PlayerMenuUtility playerMenuUtility, OfflinePlayer player, int page) {
        super(playerMenuUtility);
        target = player;
        this.page = page;
    }

    @Override
    public String getMenuName() {
        return target.getName() + " > Infractions";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        Player player = (Player) e.getWhoClicked();

        if (e.getCurrentItem().getType().equals(Material.OAK_SIGN)) {
            new PunishMenu(EpicPunishments.getPlayerMenuUtility(player), target).open();
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack back = ItemUtils.createItem(Material.OAK_SIGN, "§b§lBack", 1, "§7Click to go back");
        inventory.setItem(49, back);

        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        ArrayList<String> infractions = new ArrayList<>(plugin.getPlayerStorage().getConfig().getStringList(target.getUniqueId() + ".infractions"));
        if (infractions.isEmpty()) return;

        for (int i = 0; i < slots.length; i++) {
            int index = slots.length * page + i;
            if (index >= infractions.size()) break;
            if (infractions.get(index) != null) {
                String[] data = infractions.get(i).split(";");

                OfflinePlayer issuer = Bukkit.getOfflinePlayer(UUID.fromString(data[0]));
                String type = data[1];
                String reason = data[2];
                long duration = Long.parseLong(data[3]);

                ItemStack infraction = ItemUtils.createItem(Material.PAPER, "§6§l" + type, 1,
                        "",
                        "§7reason",
                        "§e" + reason,
                        "",
                        "§7Duration",
                        "§e" + TimeUtils.makeReadable(duration),
                        "",
                        "§7Issuer",
                        "§e" + issuer.getName());
                inventory.setItem(slots[i], infraction);
            }
        }
    }
}
