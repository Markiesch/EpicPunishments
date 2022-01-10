package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.menusystem.SearchTypes;
import com.markiesch.utils.*;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.*;

public class PunishMenu extends Menu implements Listener {
    EpicPunishments plugin = EpicPunishments.getInstance();
    public OfflinePlayer target;
    int maxTemplatesPerPage = 14;
    int page = 0;

    public PunishMenu(PlayerMenuUtility playerMenuUtility, OfflinePlayer player) {
        super(playerMenuUtility);
        target = player;
        open();
    }

    public String getMenuName() {
        return "Punish > " + target.getName();
    }

    public int getSlots() {
        return 54;
    }

    public void handleMenu(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem().getType().equals(Material.FLOWER_BANNER_PATTERN)) {
            new InfractionsMenu(EpicPunishments.getPlayerMenuUtility(player), target, 0);
            return;
        }

        if (event.getCurrentItem().getType().equals(Material.PAPER)) {
            ItemMeta meta = event.getCurrentItem().getItemMeta();
            if (meta == null) return;

            String uuid = meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "templateUUID"), PersistentDataType.STRING);
            if (uuid == null) return;

            if (!PlayerStorage.hasTemplatePermission(player.getUniqueId(), UUID.fromString(uuid))) player.sendMessage("§7You do not have§c permissions§7 to use this template");
            else if (!PlayerStorage.punishPlayerFromTemplate(target.getUniqueId(), player.getUniqueId(), UUID.fromString(uuid))) player.sendMessage("§cFailed to punish player");
            player.closeInventory();
            return;
        }

        if (event.getCurrentItem().getType().equals(Material.OAK_SIGN)) {
            new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(player), 0, SearchTypes.ALL);
        }
    }

    public void setMenuItems() {
        if (target == null) {
            playerMenuUtility.getOwner().sendMessage("§cCouldn't find player. Closing menu...");
            playerMenuUtility.getOwner().closeInventory();
            return;
        }

        ItemStack infractions = ItemUtils.createItem(Material.FLOWER_BANNER_PATTERN, "§c§lInfractions", "§7Click to view infractions");
        inventory.setItem(52, infractions);

        ItemStack back = ItemUtils.createItem(Material.OAK_SIGN, "§b§lBack", "§7Click to go back");
        inventory.setItem(49, back);

        generateTemplates();
        generatePlayerHead();
    }

    private void generateTemplates() {
        ConfigurationSection configurationSection = TemplateStorage.getConfig().getConfigurationSection("");
        if (configurationSection == null) {
            playerMenuUtility.getOwner().sendMessage("There was an error whilst opening the Templates Menu");
            return;
        }

        ArrayList<String> templates = new ArrayList<>(configurationSection.getKeys(false));
        if (templates.isEmpty()) return;

        int[] slots = {28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

        for (int i = 0; i < maxTemplatesPerPage; i++) {
            int index = maxTemplatesPerPage * page + i;
            if (index >= templates.size()) break;
            if (templates.get(index) == null) continue;

            String name = TemplateStorage.getConfig().getString(templates.get(index) + ".name");
            String type = TemplateStorage.getConfig().getString(templates.get(index) + ".type");
            String reason = TemplateStorage.getConfig().getString(templates.get(index) + ".reason");
            if (type != null) type = type.substring(0, 1).toUpperCase(Locale.US) + type.substring(1).toLowerCase(Locale.US);
            ItemStack template = ItemUtils.createItem(Material.PAPER, "§9§l" + name, "§7Click to punish " + target.getName(), "", "§7Type: §a" + (type == null ? "none" : type), "§7Reason: §a" + (reason == null ? "none" : reason));

            ItemMeta meta = template.getItemMeta();
            String uuid = templates.get(index);
            if (meta != null && uuid != null) {
                meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "templateUUID"), PersistentDataType.STRING, uuid);
                template.setItemMeta(meta);
            }
            inventory.setItem(slots[i], template);
        }
    }

    private void generatePlayerHead() {
        Date date = new Date(target.getFirstPlayed());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String formattedDate = sdf.format(date);
        List<String> infractionsList = PlayerStorage.getPunishments(target.getUniqueId());

        ItemStack playerHead = ItemUtils.createItem(
                Material.PLAYER_HEAD, "§b§l" + target.getName(), "",
                (infractionsList.isEmpty() ? "§a✔ §7didn't received any punishments yet" : "§6✔ §7had received " + infractionsList.size() + " punishments"),
                (PlayerStorage.isPlayerBanned(target.getUniqueId()) ? "§6✔ §7" + target.getName() + " is §abanned §7on §e" + plugin.getServer().getName() : "§a✔ §a" + target.getName() + " §7is not §ebanned"),
                "", "§7Joined at: " + formattedDate);

        SkullMeta playerMeta = (SkullMeta) playerHead.getItemMeta();
        if (playerMeta != null) playerMeta.setOwningPlayer(target);
        playerHead.setItemMeta(playerMeta);
        inventory.setItem(13, playerHead);
    }

    public boolean hasPermission() {
        Player player = playerMenuUtility.getOwner();
        if (player.hasPermission("epicpunishments.templates")) return true;
        player.sendMessage("§7You do not have§c permissions§7 to view templates");
        player.closeInventory();
        return false;
    }
}
