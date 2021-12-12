package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.menus.PunishMenu;
import com.markiesch.menusystem.menus.PlayerSelectorMenu;
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

                if (args.length == 0) {
                    new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(player), 0).open();
                    return true;
                }

                if ("reload".equalsIgnoreCase(args[0])) {
                    if (!player.hasPermission("epicpunishments.reload")) {
                        player.sendMessage("§cYou do not have permissions to run this command!");
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
                    player.sendMessage("§7You do not have §cpermissions §7to use this command!");
                    return true;
                }
                // Open menu of defined player
                OfflinePlayer target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    target = Bukkit.getOfflinePlayer(args[0]);
                }

                if (plugin.getPlayerStorage().getConfig().contains(target.getUniqueId().toString())) {
                    new PunishMenu(EpicPunishments.getPlayerMenuUtility(player), target).open();
                } else {
                    player.sendMessage("§cCouldn't link that username to any UUID!");
                }
                return true;
            }

            @Override
            public String getUsage() { return ""; }
            public String getPermission() { return "epicpunishments.punish"; }
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                List<String> result = new ArrayList<>();
                if (!sender.hasPermission(getPermission())) return result;
                return PunishTabCompleter.onTabComplete(args);
            }
        };
    }


}
