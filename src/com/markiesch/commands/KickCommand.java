package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.PlayerStorage;
import com.markiesch.utils.PunishTypes;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KickCommand {
    public KickCommand() {
        new CommandBase("kick", 1, -1, true) {
            @Override
            public boolean onCommand(CommandSender sender, String[] args) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage("§c" + args[0] + " is not online!");
                    return true;
                }

                List<String> arguments = Arrays.asList(args);
                Player player = (Player) sender;

                String reason = "none";
                if (args.length >= 2) reason = String.join(" ", arguments.subList(1, arguments.size()));
                PlayerStorage.createPunishment(target.getUniqueId(), player.getUniqueId(), PunishTypes.KICK, reason, 0L);
                sender.sendMessage("§7Successfully kicked §a" + target.getName() + " §7Reason: §e" + reason);

                return true;
            }

            @Override
            public String getUsage() { return "§7Usage: §e/kick <target> (reason)"; }
            public String getPermission() { return "epicpunishments.kick"; }
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                return sender.hasPermission(getPermission()) ? InfractionTabCompleter.onTabComplete(args, false) : new ArrayList<>();
            }
        };
    }
}
