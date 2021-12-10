package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.InputTypes;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.menusystem.menus.EditTemplateMenu;
import com.markiesch.utils.InputUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class PlayerInput implements Listener {
    private final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);

    @EventHandler
    public void onInput(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!plugin.getEditor().containsKey(uuid)) return;
        InputUtils edit = plugin.getEditor().get(event.getPlayer().getUniqueId());
        String message = event.getMessage();
        event.setCancelled(true);
        edit.cancel();
        Bukkit.getScheduler().scheduleSyncDelayedTask(EpicPunishments.getInstance(), () -> {
            PlayerMenuUtility playerMenuUtility = EpicPunishments.getPlayerMenuUtility(player);
            if (edit.getChat().equals(InputTypes.TEMPLATE_NAME)) {
                playerMenuUtility.setReason("none");
                new EditTemplateMenu(EpicPunishments.getPlayerMenuUtility(player), message.replace(" ", "_")).open();
            } else if (edit.getChat().equals(InputTypes.TEMPLATE_REASON)) {
                String templateName = playerMenuUtility.getTemplateName();
                playerMenuUtility.setReason(message);
                new EditTemplateMenu(EpicPunishments.getPlayerMenuUtility(player), templateName).open();
            }
        }, 5L);
    }
}
