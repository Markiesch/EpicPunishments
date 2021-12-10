package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.PunishTypes;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class UnmuteCommand {
    EpicPunishments plugin = EpicPunishments.getInstance();

    public UnmuteCommand() {
        new CommandBase("unmute", 1, -1, true) {
            @Override
            public boolean onCommand(CommandSender sender, String[] args) {
                Player player = (Player) sender;

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                UUID tUUID = target.getUniqueId();
                if (target.getPlayer() == null && !plugin.getPlayerStorage().playerRegistered(tUUID)) {
                    sender.sendMessage("§cCould not find " + args[0]);
                    return true;
                }

//                if (target.getPlayer().equals(sender)) {
//                    sender.sendMessage("§cYou cannot unmute yourself");
//                }

                plugin.getPlayerStorage().unMute(target.getUniqueId());
                return true;
            }

            public String getUsage() { return "§7Usage: §e/unmute <target>"; }
            public String getPermission() { return "epicpunishments.unmute"; }
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                return sender.hasPermission(getPermission()) ? InfractionTabCompleter.onTabComplete(args, true) : new ArrayList<>();
            }
        };
    }
}
