package com.markiesch.commands;

import com.markiesch.utils.PlayerStorage;
import com.markiesch.utils.PunishTypes;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class WarnCommand extends CommandBase {
    public WarnCommand() {
        super("warn", 1, -1, true);
    }

    public boolean onCommand(CommandSender sender, String[] args) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        UUID tUUID = target.getUniqueId();
        if (target.getPlayer() == null && !PlayerStorage.playerRegistered(tUUID)) {
            sender.sendMessage("§cCould not find " + args[0]);
            return true;
        }

        List<String> arguments = Arrays.asList(args);
        Player player = (Player) sender;

        String reason = "none";
        if (args.length >= 2) reason = String.join(" ", arguments.subList(1, arguments.size()));
        PlayerStorage.createPunishment(target.getUniqueId(), player.getUniqueId(), PunishTypes.WARN, reason, 0L);
        return true;
    }

    @Override public String getUsage() { return "§7Usage: §e/warn <target> (reason)"; }
    @Override public String getPermission() { return "epicpunishments.warn"; }
    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return InfractionTabCompleter.onTabComplete(args, false);
    }
}
