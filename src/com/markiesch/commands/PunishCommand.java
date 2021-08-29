package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.menus.PunishMenu;
import com.markiesch.menusystem.menus.PlayerSelectorMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PunishCommand implements CommandExecutor {
    private final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (args.length == 0) {
            new PlayerSelectorMenu(EpicPunishments.getPlayerMenuUtility(player), 0).open();
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("epicPunishments.reload")) {
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
}
