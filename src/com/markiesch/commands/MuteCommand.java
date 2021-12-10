package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.PunishTypes;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MuteCommand {
    EpicPunishments plugin = EpicPunishments.getInstance();

    public MuteCommand() {
        new CommandBase("mute", 1, -1, true) {
            @Override
            public boolean onCommand(CommandSender sender, String[] args) {
                Player player = (Player) sender;

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                UUID tUUID = target.getUniqueId();
                if (target.getPlayer() == null && !plugin.getPlayerStorage().playerRegistered(tUUID)) {
                    sender.sendMessage("§cCould not find " + args[0]);
                    return true;
                }

                if (target.getPlayer().equals(sender)) {
                    sender.sendMessage("§cYou cannot mute yourself");
                    return true;
                }

                if (target.getPlayer().hasPermission("epicpunishments.mute.bypass")) {
                    sender.sendMessage("§cYou cannot punish this player!");
                    return true;
                }

                List<String> arguments = Arrays.asList(args);
                String reason = "none";
                if (args.length >= 2) {
                    sender.sendMessage("Test");
                    reason = String.join(" ", arguments.subList(1, arguments.size()));
                }

                plugin.getPlayerStorage().createPunishment(target.getUniqueId(), player.getUniqueId(), PunishTypes.MUTE, reason, 0L);
                return true;
            }

            public String getUsage() { return "§7Usage: §e/mute <target> (reason)"; }
            public String getPermission() { return "epicpunishments.mute"; }
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                return sender.hasPermission(getPermission()) ? InfractionTabCompleter.onTabComplete(args, true) : new ArrayList<>();
            }
        };
    }
}
