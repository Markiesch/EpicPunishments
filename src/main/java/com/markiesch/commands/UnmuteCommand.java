package com.markiesch.commands;

import com.markiesch.utils.PlayerStorage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnMuteCommand extends CommandBase {
    public UnMuteCommand() {
        super("unmute", 1, 1, false);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        UUID tUUID = target.getUniqueId();
        if (target.getPlayer() == null && !PlayerStorage.playerRegistered(tUUID)) {
            sender.sendMessage("§cCould not find " + args[0]);
            return true;
        }

        if (!PlayerStorage.isMuted(target.getUniqueId())) {
            sender.sendMessage("§e" + target.getName() + " §7is not muted!");
            return true;
        }

        PlayerStorage.unMute(target.getUniqueId());
        sender.sendMessage("§7Successfully unmuted §e" + target.getName());
        return true;
    }

    @Override public String getUsage() { return "§7Usage: §e/unmute <target>"; }
    @Override public String getPermission() { return "epicpunishments.unmute"; }
    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return InfractionTabCompleter.onTabComplete(args, true);
    }
}
