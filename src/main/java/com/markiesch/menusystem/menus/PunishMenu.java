package com.markiesch.menusystem.menus;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.Menu;
import com.markiesch.menusystem.PlayerSelectorSearchType;
import com.markiesch.modules.infraction.InfractionModel;
import com.markiesch.modules.profile.ProfileController;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class PunishMenu extends Menu implements Listener {
    private final byte BACK_BUTTON_SLOT = 49;
    private final byte SHOW_INFRACTIONS_BUTTON_SLOT = 52;

    public OfflinePlayer target;

    public PunishMenu(EpicPunishments plugin, UUID uuid, UUID player) {
        super(plugin, uuid, 54);

        target = Bukkit.getOfflinePlayer(player);
        open();
    }

    @Override
    public String getMenuName() {
        return "Punish > " + target.getName();
    }

    public void handleMenu(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;

        switch (event.getSlot()) {
            case BACK_BUTTON_SLOT -> new PlayerSelectorMenu(plugin, uuid, 0, PlayerSelectorSearchType.ALL);
            case SHOW_INFRACTIONS_BUTTON_SLOT -> new InfractionsMenu(plugin, uuid, target.getUniqueId());
        }
    }

    public void setMenuItems() {
        if (target == null) {
            getOwner().sendMessage("§cCouldn't find player. Closing menu...");
            getOwner().closeInventory();
            return;
        }

        ItemStack infractions = ItemUtils.createItem(Material.FLOWER_BANNER_PATTERN, "§c§lInfractions", "§7Click to view infractions");
        getInventory().setItem(SHOW_INFRACTIONS_BUTTON_SLOT, infractions);

        ItemStack back = ItemUtils.createItem(Material.OAK_SIGN, "§b§lBack", "§7Click to go back");
        getInventory().setItem(BACK_BUTTON_SLOT, back);

        generatePlayerHead();
    }

    private void generatePlayerHead() {
        Date date = new Date(target.getFirstPlayed());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String formattedDate = sdf.format(date);


        ProfileModel profile = new ProfileController().getProfile(target.getUniqueId());
        List<InfractionModel> infractions = profile.getInfractions();

        ItemStack playerHead = ItemUtils.createItem(
                Material.PLAYER_HEAD,
                "§b§l" + target.getName(),
                "",
                (infractions.isEmpty() ? "§a✔ §7didn't received any punishments yet" : "§6✔ §7had received " + infractions.size() + " punishments"),
                (profile.isBanned() ? "§6✔ §7" + target.getName() + " is§a banned §7on §e" + plugin.getServer().getName() : "§a✔ §a" + target.getName() + " §7is not§e banned"),
                "",
                "§7Joined at: " + formattedDate);

        SkullMeta playerMeta = (SkullMeta) playerHead.getItemMeta();
        if (playerMeta != null) playerMeta.setOwningPlayer(target);
        playerHead.setItemMeta(playerMeta);
        getInventory().setItem(13, playerHead);
    }

    @Override
    public String getRequiredPermission() {
        return "epicpunishments.templates";
    }
}