package com.markiesch.commands;

import com.markiesch.Permission;
import com.markiesch.modules.infraction.InfractionType;
import com.markiesch.modules.infraction.InfractionUtils;
import com.markiesch.utils.CommandUtils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class MuteCommand extends CommandBase {
    public MuteCommand() {
        super(
                "mute",
                Permission.EXECUTE_MUTE,
                "§7Usage: §e/mute <target> <duration | permanent> (reason)",
                2,
                -1,
                false
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        InfractionUtils.commandToInfraction(sender, args, InfractionType.MUTE);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1) return CommandUtils.getAllOfflinePlayerNames(args[0]);
        if (args.length == 2) return CommandUtils.getTimeOptions(args[1]);
        else return List.of("reason");
    }
}
