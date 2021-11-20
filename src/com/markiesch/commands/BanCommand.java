package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.PunishTypes;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class BanCommand implements CommandExecutor {
    EpicPunishments plugin = EpicPunishments.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("epicpunishments.ban")) {
            player.sendMessage("§7You do not have §cpermissions §7to execute this command!");
            return true;
        }

        int minArgs = 2;
        if (args.length < minArgs) {
            sender.sendMessage("§7Usage: §e/ban <target> <duration | permanent> (reason)");
            return true;
        }

        OfflinePlayer target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            target = Bukkit.getOfflinePlayer(args[0]);
        }

        long duration = TimeUtils.parseTime(args[1]);

        player.sendMessage(args[1]);
        player.sendMessage(duration + "");

        List<String> arguments = Arrays.asList(args);
        String reason = String.join(" ", arguments.subList(2, arguments.size()));
        plugin.getPlayerStorage().createPunishment(target.getUniqueId(), player.getUniqueId(), PunishTypes.BAN, reason, duration);
        sender.sendMessage("§7Successfully banned §a" + target.getName() + " §7Reason: §e" + reason);
        return true;
    }
}
