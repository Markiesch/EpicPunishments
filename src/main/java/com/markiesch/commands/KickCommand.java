package com.markiesch.commands;

import com.markiesch.modules.infraction.InfractionType;
import com.markiesch.modules.infraction.PreparedInfraction;
import com.markiesch.utils.CommandUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class KickCommand extends CommandBase {
    public KickCommand() {
        super(
                "kick",
                "epicpunishments.kick",
                "§7Usage: §e/kick <target> (reason)",
                1,
                -1,
                true
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        Player issuer = (Player) sender;

        OfflinePlayer target = Bukkit.getPlayer(args[0]);
        if (target == null) target = Bukkit.getOfflinePlayer(args[0]);

        List<String> arguments = Arrays.asList(args);
        String reason = String.join(" ", arguments.subList(1, arguments.size()));

        new PreparedInfraction(
                InfractionType.KICK,
                reason
        ).execute(issuer, target);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1) return CommandUtils.getAllOnlinePlayerNames();
        else return List.of("reason");
    }
}
