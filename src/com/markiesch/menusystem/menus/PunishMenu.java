package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerMenuUtility;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import java.util.ArrayList;

import static com.markiesch.utils.BanMenuUtils.generatePlayerMeta;

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
            new InfractionsMenu(EpicPunishments.getPlayerMenuUtility(p), target).open();
            return;
        }

        if (e.getCurrentItem().getType().equals(Material.PAPER)) {
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            if (meta == null) return;
            String name = ChatColor.stripColor(meta.getDisplayName());

            String type = plugin.getConfig().getString("templates." + name + ".type");
            if (type == null) type = "WARN";
            PunishTypes punishType = PunishTypes.valueOf(type.toUpperCase());
            String reason = plugin.getConfig().getString("templates." + name + ".reason");
            String configDuration = plugin.getConfig().getString("templates." + name + ".duration");
            long duration = 0L;
            if (configDuration != null) duration = TimeUtils.parseTime(configDuration);
            Player issuer = playerMenuUtility.getOwner();
            plugin.getPlayerStorage().createPunishment(target.getUniqueId(), issuer.getUniqueId(), punishType, reason, duration);
        }

        if (e.getCurrentItem().getType().equals(Material.OAK_SIGN)) {
            new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(p), 0).open();
        }
    }

    @Override
    public void setMenuItems() {
        if (target == null) return;
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta player_meta = (SkullMeta) playerHead.getItemMeta();
        generatePlayerMeta(player_meta, target, playerHead);
        inventory.setItem(13, playerHead);

        ItemStack infractions = ItemUtils.createItem(Material.FLOWER_BANNER_PATTERN, "§c§lInfractions", 1, "§7Click to view infractions");
        inventory.setItem(52, infractions);

        ItemStack back = ItemUtils.createItem(Material.OAK_SIGN, "§b§lBack", 1, "§7Click to go back");
        inventory.setItem(49, back);

        generateTemplates();
    }

    public void generateTemplates() {
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
                String type = plugin.getConfig().getString("templates." + templates.get(i) + ".type");
                if (type != null) type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
                String reason = plugin.getConfig().getString("templates." + templates.get(i) + ".reason");
                ItemStack template = ItemUtils.createItem(Material.PAPER, "§9§l" + templates.get(i), 1, "§7Click to punish " + target.getName(), "", "§7Type: §a" + (type == null ? "none" : type), "§7Reason: §a" + (reason == null ? "none" : reason));
                inventory.setItem(slots[i], template);
            }
        }
    }
}
