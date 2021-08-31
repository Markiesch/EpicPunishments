package com.markiesch.listeners;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.InputTypes;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.menusystem.menus.EditTemplateMenu;
import com.markiesch.utils.InputUtils;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class PlayerInput implements Listener {
    private final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);

    @EventHandler
    public void onInput(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if (!plugin.getEditor().containsKey(uuid)) return;
        InputUtils edit = plugin.getEditor().get(e.getPlayer().getUniqueId());
        String message = e.getMessage();
        e.setCancelled(true);
        edit.cancel();
        Bukkit.getScheduler().scheduleSyncDelayedTask(EpicPunishments.getInstance(), () -> {
            PlayerMenuUtility playerMenuUtility = EpicPunishments.getPlayerMenuUtility(p);
            if (edit.getChat().equals(InputTypes.TEMPLATE_NAME)) {
                playerMenuUtility.setReason("none");
                new EditTemplateMenu(EpicPunishments.getPlayerMenuUtility(p), message.replace(" ", "_")).open();
            } else if (edit.getChat().equals(InputTypes.TEMPLATE_REASON)) {
                String templateName = playerMenuUtility.getTemplateName();
                playerMenuUtility.setReason(message);
                new EditTemplateMenu(EpicPunishments.getPlayerMenuUtility(p), templateName).open();
            }
        }, 5L);
    }
}
