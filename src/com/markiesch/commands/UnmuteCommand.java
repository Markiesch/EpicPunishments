package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.PlayerStorage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnmuteCommand {
    EpicPunishments plugin = EpicPunishments.getInstance();

    public UnmuteCommand() {
        new CommandBase("unmute", 1, -1, true) {
            @Override
            public boolean onCommand(CommandSender sender, String[] args) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                UUID tUUID = target.getUniqueId();
                if (target.getPlayer() == null && !PlayerStorage.playerRegistered(tUUID)) {
                    sender.sendMessage("§cCould not find " + args[0]);
                    return true;
                }

                PlayerStorage.unMute(target.getUniqueId());
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
