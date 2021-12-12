package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.PlayerStorage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnbanCommand {
    public UnbanCommand() {
        new CommandBase("unban", 1, 1, true) {
            public boolean onCommand(CommandSender sender, String[] args) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                UUID tUUID = target.getUniqueId();
                if (target.getPlayer() == null && !PlayerStorage.playerRegistered(tUUID)) {
                    sender.sendMessage("§cCould not find " + args[0]);
                    return true;
                }

                if (!PlayerStorage.isPlayerBanned(target.getUniqueId())) {
                    sender.sendMessage("§e" + target.getName() + " §7is not banned!");
                    return true;
                }

                PlayerStorage.unBan(target.getUniqueId());
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
