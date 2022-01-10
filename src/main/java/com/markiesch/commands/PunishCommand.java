package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.SearchTypes;
import com.markiesch.menusystem.menus.PunishMenu;
import com.markiesch.menusystem.menus.PlayerSelectorMenu;
import com.markiesch.utils.PlayerStorage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PunishCommand {
    EpicPunishments plugin = EpicPunishments.getInstance();

    public PunishCommand() {
        new CommandBase("punish", 0, -1, true) {
            @Override
            public boolean onCommand(CommandSender sender, String[] args) {
                Player player = (Player) sender;

                if (args.length > 0 && "reload".equalsIgnoreCase(args[0])) {
                    if (!player.hasPermission("epicpunishments.reload")) {
                        this.sendPermissionMessage(sender);
                        return true;
                    }

                    try {
                        long startTime = System.currentTimeMillis();
                        plugin.reloadConfig();
                        long estimatedTime = System.currentTimeMillis() - startTime;
                        player.sendMessage("§aConfig files reloaded! Took [ms]ms".replace("[ms]", Long.toString(estimatedTime)));
                    } catch (Exception e) {
                        player.sendMessage("§cFailed to load config file! Check spelling!");
                    }
                    return true;
                }

                if (!player.hasPermission("epicpunishments.gui")) {
                    this.sendPermissionMessage(sender);
                    return true;
                }

                if (args.length == 0) {
                    new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(player), 0, SearchTypes.ALL);
                    return true;
                }

                // Open menu of defined player
                OfflinePlayer target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    target = Bukkit.getOfflinePlayer(args[0]);
                }

                if (PlayerStorage.getConfig().contains(target.getUniqueId().toString())) {
                    new PunishMenu(EpicPunishments.getPlayerMenuUtility(player), target);
                } else {
                    player.sendMessage("§cCouldn't link that username to any UUID!");
                }
                return true;
            }

            @Override
            public String getUsage() { return ""; }
            public String getPermission() { return "epicpunishments.punish"; }
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                if (!sender.hasPermission(getPermission())) return new ArrayList<>();
                return PunishTabCompleter.onTabComplete(args);
            }
        };
    }
}
