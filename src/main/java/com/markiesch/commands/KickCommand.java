package com.markiesch.commands;

import com.markiesch.Permission;
import com.markiesch.modules.infraction.InfractionType;
import com.markiesch.modules.infraction.InfractionUtils;
import com.markiesch.utils.CommandUtils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class KickCommand extends CommandBase {
    public KickCommand() {
        super(
                "kick",
                Permission.EXECUTE_KICK,
                "§7Usage: §e/kick <target> (reason)",
                1,
                -1,
                false
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        InfractionUtils.commandToInfraction(sender, args, InfractionType.KICK);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1) return CommandUtils.getAllOnlinePlayerNames(args[0]);
        else return List.of("reason");
    }
}
