package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnbanCommand {
    EpicPunishments plugin = EpicPunishments.getInstance();

    public UnbanCommand() {
        new CommandBase("unban", 1, 1, true) {
            public boolean onCommand(CommandSender sender, String[] args) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                UUID tUUID = target.getUniqueId();
                if (target.getPlayer() == null && !plugin.getPlayerStorage().playerRegistered(tUUID)) {
                    sender.sendMessage("§cCould not find " + args[0]);
                    return true;
                }

                plugin.getPlayerStorage().unBan(target.getUniqueId());
                sender.sendMessage("§7Successfully unbanned §a" + target.getName());
                return true;
            }

            public String getUsage() { return "§7Usage: §e/unban <target>"; }
            public String getPermission() { return "epicpunishments.unban"; }
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                return sender.hasPermission(getPermission()) ? InfractionTabCompleter.onTabComplete(args, true) : new ArrayList<>();
            }
        };
    }
}
