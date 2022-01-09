package com.markiesch.commands;

import com.markiesch.EpicPunishments;
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
import java.util.UUID;

public class MuteCommand {
    public MuteCommand() {
        new CommandBase("mute", 1, -1, true) {
            @Override
            public boolean onCommand(CommandSender sender, String[] args) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                UUID tUUID = target.getUniqueId();
                if (target.getPlayer() == null && !PlayerStorage.playerRegistered(tUUID)) {
                    sender.sendMessage("§cCould not find " + args[0]);
                    return true;
                }

                long duration = args.length >= 2 ? TimeUtils.parseTime(args[1]) : 0L;
                String reason = "none";
                if (args.length >= 2) {
                    List<String> arguments = Arrays.asList(args);
                    reason = String.join(" ", arguments.subList(2, arguments.size()));
                }

                Player player = (Player) sender;
                PlayerStorage.createPunishment(target.getUniqueId(), player.getUniqueId(), PunishTypes.MUTE, reason, duration);
                return true;
            }

            public String getUsage() { return "§7Usage: §e/mute <target> (duration) (reason)"; }
            public String getPermission() { return "epicpunishments.mute"; }
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                return sender.hasPermission(getPermission()) ? InfractionTabCompleter.onTabComplete(args, true) : new ArrayList<>();
            }
        };
    }
}
