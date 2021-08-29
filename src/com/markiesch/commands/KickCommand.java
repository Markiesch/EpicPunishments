package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.PunishTypes;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class KickCommand implements CommandExecutor {
    EpicPunishments plugin = EpicPunishments.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("epicpunishments.kick")) {
            player.sendMessage("§7You do not have &cpermissions §7to execute this command!");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§7Usage: §e/kick <target> (reason)");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(args[0] + " is not online");
            return true;
        }

        List<String> arguments = Arrays.asList(args);

        String reason = "none";
        if (!(args.length < 2)) {
            player.sendMessage("Test");
            reason = String.join(" ", arguments.subList(1, arguments.size()));
        }
        plugin.getPlayerStorage().createPunishment(target.getUniqueId(), player.getUniqueId(), PunishTypes.KICK, reason, 0L);
        sender.sendMessage("§7Successfully kicked §a" + target.getName() + " §7Reason: §e" + (reason.equals("") ? "none" : reason));
        return true;
    }
}
