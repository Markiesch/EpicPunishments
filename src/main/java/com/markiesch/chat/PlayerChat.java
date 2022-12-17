package com.markiesch.chat;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Consumer;

public class PlayerChat implements Listener {
    private final EpicPunishments plugin;

    private int taskId;
    private final String title;
    private final String subtitle;
    private final Player player;
    private final Consumer<String> finishCallback;

    public PlayerChat(EpicPunishments plugin, Player player, String title, String subtitle, Consumer<String> finishCallback) {
        this.plugin = plugin;
        this.title = title;
        this.subtitle = subtitle;
        this.player = player;
        this.finishCallback = finishCallback;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        start();
    }

    private void start() {
        player.closeInventory();
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (player.isOnline() && !player.isDead()) {
                player.sendTitle(ChatUtils.changeColor(title), ChatUtils.changeColor(subtitle), 0, 51, 0);
            }
        }, 0L, 50L);
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(this.taskId);
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onInput(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        this.cancel();

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            this.finishCallback.accept(event.getMessage());
        }, 5L);
    }
}
