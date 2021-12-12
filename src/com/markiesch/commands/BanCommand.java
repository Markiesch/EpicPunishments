package com.markiesch.commands;

import com.markiesch.utils.PlayerStorage;
import com.markiesch.utils.PunishTypes;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BanCommand {
    public BanCommand() {
        new CommandBase("ban", 2, -1, true) {
            @Override
            public boolean onCommand(CommandSender sender, String[] args) {
                Player player = (Player) sender;

                OfflinePlayer target = Bukkit.getPlayer(args[0]);
                if (target == null) target = Bukkit.getOfflinePlayer(args[0]);

                long duration = TimeUtils.parseTime(args[1]);
                List<String> arguments = Arrays.asList(args);
                String reason = String.join(" ", arguments.subList(2, arguments.size()));
                PlayerStorage.createPunishment(target.getUniqueId(), player.getUniqueId(), PunishTypes.BAN, reason, duration);
                return true;
            }
            @Override
            public String getUsage() { return "§7Usage: §e/ban <target> <duration | permanent> (reason)"; }
            public String getPermission() { return "epicpunishments.ban"; }
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                return sender.hasPermission(getPermission()) ? InfractionTabCompleter.onTabComplete(args, true) : new ArrayList<>();
            }
        };
    }
}
