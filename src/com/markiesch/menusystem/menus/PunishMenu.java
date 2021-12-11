package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PunishMenu extends Menu implements Listener {
    EpicPunishments plugin = EpicPunishments.getInstance();
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
    public void handleMenu(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
            new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(player), 0).open();
            return;
        }

        if (event.getCurrentItem().getType().equals(Material.FLOWER_BANNER_PATTERN)) {
            new InfractionsMenu(EpicPunishments.getPlayerMenuUtility(player), target, 0).open();
            return;
        }

        if (event.getCurrentItem().getType().equals(Material.PAPER)) {
            ItemMeta meta = event.getCurrentItem().getItemMeta();
            if (meta == null) return;
            String name = ChatColor.stripColor(meta.getDisplayName());

            String type = TemplateStorage.getConfig().getString(name + ".type");
            if (type == null) type = "WARN";
            PunishTypes punishType = PunishTypes.valueOf(type.toUpperCase(Locale.US));
            String reason = TemplateStorage.getConfig().getString(name + ".reason");
            if (reason == null || reason.isEmpty()) reason = "none";
            String configDuration = TemplateStorage.getConfig().getString(name + ".duration");
            long duration = 0L;
            if (configDuration != null) duration = TimeUtils.parseTime(configDuration);
            Player issuer = playerMenuUtility.getOwner();
            issuer.closeInventory();
            plugin.getPlayerStorage().createPunishment(target.getUniqueId(), issuer.getUniqueId(), punishType, reason, duration);
        }

        if (event.getCurrentItem().getType().equals(Material.OAK_SIGN)) {
            new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(player), 0).open();
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
        ConfigurationSection configurationSection = TemplateStorage.getConfig().getConfigurationSection("");
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
                String name = TemplateStorage.getConfig().getString(templates.get(index) + ".name");
                String type = TemplateStorage.getConfig().getString(templates.get(index) + ".type");
                if (type != null) type = type.substring(0, 1).toUpperCase(Locale.US) + type.substring(1).toLowerCase(Locale.US);
                String reason = TemplateStorage.getConfig().getString(templates.get(index) + ".reason");
                ItemStack template = ItemUtils.createItem(Material.PAPER, "§9§l" + name, 1, "§7Click to punish " + target.getName(), "", "§7Type: §a" + (type == null ? "none" : type), "§7Reason: §a" + (reason == null ? "none" : reason));
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
