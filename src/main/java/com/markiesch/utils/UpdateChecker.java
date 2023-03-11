package com.markiesch.utils;

import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class UpdateChecker implements Listener {
    private final String SPIGOT_UPDATE_URL = "https://api.spigotmc.org/legacy/update.php?resource=";
    private final JavaPlugin plugin;
    private final int resourceId;
    private @Nullable String latestVersion = null;

    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void notifyOnJoin(PlayerJoinEvent event) {
        if (latestVersion == null || !event.getPlayer().hasPermission(Permission.ADMIN_NOTIFY_NEW_VERSION.getNode()))
            return;

        notify(event.getPlayer());
    }

    public void start() {
        // Minutes - seconds - ticks
        final long CHECK_DELAY_TIME = 30L * 60L * 20L;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::checkVersion, 0L, CHECK_DELAY_TIME);
    }

    private void checkVersion() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (InputStream inputStream = new URL(SPIGOT_UPDATE_URL + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (!scanner.hasNext()) return;

                String latestVersion = scanner.next();
                if (latestVersion.equals(plugin.getDescription().getVersion())) return;

                this.latestVersion = latestVersion;
                notify(plugin.getServer().getConsoleSender());
            } catch (IOException exception) {
                plugin.getLogger().info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }


    private void notify(CommandSender commandSender) {
        final String SPIGOT_RESOURCE_URL = "https://www.spigotmc.org/resources/epic-punishments.98340/";

        List<String> message = Translation.NEW_VERSION
            .addPlaceholder("url", SPIGOT_RESOURCE_URL)
            .addPlaceholder("latest_version", latestVersion)
            .addPlaceholder("current_version", plugin.getDescription().getVersion())
            .toList();

        for (String line : message) {
            commandSender.sendMessage(line);
        }
    }
}