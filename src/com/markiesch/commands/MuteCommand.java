package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.PunishTypes;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MuteCommand implements CommandExecutor {
    EpicPunishments plugin = EpicPunishments.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("epicpunishments.mute")) {
            player.sendMessage("§7You do not have §cpermissions §7to execute this command!");
            return true;
        }

        int minArgs = 1;
        if (args.length < minArgs) {
            sender.sendMessage("§7Usage: §e/mute <target> (reason)");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        UUID tUUID = target.getUniqueId();
        if (!target.hasPlayedBefore() && !plugin.getPlayerStorage().playerRegistered(tUUID)) {
            player.sendMessage("§cCould not find " + args[0]);
            return true;
        }

        List<String> arguments = Arrays.asList(args);
        String reason = "none";
        if (args.length >= 2) {
            player.sendMessage("Test");
            reason = String.join(" ", arguments.subList(1, arguments.size()));
        }

        plugin.getPlayerStorage().createPunishment(target.getUniqueId(), player.getUniqueId(), PunishTypes.MUTE, reason, 0L);
        return true;
    }
}
