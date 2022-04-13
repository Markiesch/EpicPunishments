package com.markiesch.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class UnBanCommand extends CommandBase {
    public UnBanCommand() {
        super(
            "unban",
            "epicpunishments.unban",
            "§7Usage: §e/unban <target>",
            1,
            1,
            false
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
//        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
//        UUID tUUID = target.getUniqueId();
//        if (target.getPlayer() == null && !PlayerStorage.playerRegistered(tUUID)) {
//            sender.sendMessage("§cCould not find " + args[0]);
//            return true;
//        }
//
//        if (!PlayerStorage.isPlayerBanned(target.getUniqueId())) {
//            sender.sendMessage("§e" + target.getName() + " §7is not banned!");
//            return true;
//        }
//
//        PlayerStorage.unBan(target.getUniqueId());
//        sender.sendMessage("§7Successfully unbanned §a" + target.getName());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return new ArrayList<>();
    }
}
