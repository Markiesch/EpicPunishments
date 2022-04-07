package com.markiesch.commands;

import com.markiesch.utils.PlayerStorage;
import com.markiesch.utils.PunishTypes;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KickCommand extends CommandBase {
    public KickCommand() {
        super("kick", 1, -1, true);
    }

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
        return true;
    }

    @Override public String getUsage() { return "§7Usage: §e/kick <target> (reason)"; }
    @Override public String getPermission() { return "epicpunishments.kick"; }
    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return InfractionTabCompleter.onTabComplete(args, false);
    }
}
