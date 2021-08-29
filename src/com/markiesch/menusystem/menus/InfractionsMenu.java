package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class InfractionsMenu extends Menu {
    private final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);
    public OfflinePlayer target;

    public InfractionsMenu(PlayerMenuUtility playerMenuUtility, OfflinePlayer player) {
        super(playerMenuUtility);
        target = player;
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

    }

    @Override
    public void setMenuItems() {
        int maxTemplatesPerPage = 14;
        int page = 0;
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        ArrayList<String> infractions = new ArrayList<>(plugin.getPlayerStorage().getConfig().getStringList(target.getUniqueId() + ".infractions"));
        if (infractions.isEmpty()) return;

        for (int i = 0; i < maxTemplatesPerPage; i++) {
            int index = maxTemplatesPerPage * page + i;
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

//        plugin.getPlayerStorage().getConfig().getStringList(target.getUniqueId() + ".infractions").forEach(punishment -> {
//        });
    }
}
