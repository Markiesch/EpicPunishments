package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.utils.BanMenuUtils;
import com.markiesch.utils.ItemUtils;
import com.markiesch.utils.PunishTypes;
import com.markiesch.utils.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PunishMenu extends Menu implements Listener {
    private static final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);
    public OfflinePlayer target;
    int maxTemplatesPerPage = 14;
    int page = 0;
    public PunishMenu(PlayerMenuUtility playerMenuUtility, OfflinePlayer player) {
        super(playerMenuUtility);
        target = player;
    }

    @Override
    public String getMenuName() {
        return "Punish > " + target.getName();
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
            new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(p), 0).open();
            return;
        }

        if (e.getCurrentItem().getType().equals(Material.FLOWER_BANNER_PATTERN)) {
            new InfractionsMenu(EpicPunishments.getPlayerMenuUtility(p), target, 0).open();
            return;
        }

        if (e.getCurrentItem().getType().equals(Material.PAPER)) {
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            if (meta == null) return;
            String name = ChatColor.stripColor(meta.getDisplayName());

            String type = plugin.getTemplateStorage().getConfig().getString(name + ".type");
            if (type == null) type = "WARN";
            PunishTypes punishType = PunishTypes.valueOf(type.toUpperCase(Locale.US));
            String reason = plugin.getTemplateStorage().getConfig().getString(name + ".reason");
            if (reason == null || reason.isEmpty()) reason = "none";
            String configDuration = plugin.getTemplateStorage().getConfig().getString(name + ".duration");
            long duration = 0L;
            if (configDuration != null) duration = TimeUtils.parseTime(configDuration);
            Player issuer = playerMenuUtility.getOwner();
            issuer.closeInventory();
            plugin.getPlayerStorage().createPunishment(target.getUniqueId(), issuer.getUniqueId(), punishType, reason, duration);
        }

        if (e.getCurrentItem().getType().equals(Material.OAK_SIGN)) {
            new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(p), 0).open();
        }
    }

    @Override
    public void setMenuItems() {
        if (target == null) {
            playerMenuUtility.getOwner().sendMessage("§cCouldn't find player. Closing menu...");
            playerMenuUtility.getOwner().closeInventory();
            return;
        }

        ItemStack infractions = ItemUtils.createItem(Material.FLOWER_BANNER_PATTERN, "§c§lInfractions", 1, "§7Click to view infractions");
        inventory.setItem(52, infractions);

        ItemStack back = ItemUtils.createItem(Material.OAK_SIGN, "§b§lBack", 1, "§7Click to go back");
        inventory.setItem(49, back);

        generateTemplates();
        generatePlayerHead();
    }

    private void generateTemplates() {
        int[] slots = {28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
        ConfigurationSection configurationSection = plugin.getTemplateStorage().getConfig().getConfigurationSection("");
        if (configurationSection == null) {
            playerMenuUtility.getOwner().sendMessage("There was an error whilst opening the Templates Menu");
            return;
        }
        ArrayList<String> templates = new ArrayList<>(configurationSection.getKeys(false));
        if (templates.isEmpty()) return;
        for (int i = 0; i < maxTemplatesPerPage; i++) {
            int index = maxTemplatesPerPage * page + i;
            if (index >= templates.size()) break;
            if (templates.get(index) != null) {
                String type = plugin.getTemplateStorage().getConfig().getString(templates.get(i) + ".type");
                if (type != null) type = type.substring(0, 1).toUpperCase(Locale.US) + type.substring(1).toLowerCase(Locale.US);
                String reason = plugin.getTemplateStorage().getConfig().getString(templates.get(i) + ".reason");
                ItemStack template = ItemUtils.createItem(Material.PAPER, "§9§l" + templates.get(i), 1, "§7Click to punish " + target.getName(), "", "§7Type: §a" + (type == null ? "none" : type), "§7Reason: §a" + (reason == null ? "none" : reason));
                inventory.setItem(slots[i], template);
            }
        }
    }

    private void generatePlayerHead() {
        Date date = new Date(target.getFirstPlayed());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);
        List<String> infractionsList = plugin.getPlayerStorage().getPunishments(target.getUniqueId());

        ItemStack playerHead = ItemUtils.createItem(
                Material.PLAYER_HEAD,"§b§l" + target.getName(),1,"",
                (infractionsList.size() < 1 ? "§a✔ §7didn't received any punishments yet" : "§6✔ §7had received " + infractionsList.size() + " punishments"),
                (plugin.getPlayerStorage().isPlayerBanned(target.getUniqueId()) ? "§6✔ §7" + target.getName() + " is §abanned §7on §e" + plugin.getServer().getName() : "§a✔ §a" + target.getName() + " §7is not §ebanned"),
                "", "§7Joined at: " + formattedDate);

        SkullMeta playerMeta = (SkullMeta) playerHead.getItemMeta();
        if (playerMeta != null) playerMeta.setOwningPlayer(target);
        playerHead.setItemMeta(playerMeta);
        inventory.setItem(13, playerHead);
    }
}
