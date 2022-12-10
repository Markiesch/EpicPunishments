package com.markiesch.commands;

import com.markiesch.locale.Translation;
import com.markiesch.modules.infraction.InfractionType;
import com.markiesch.modules.infraction.PreparedInfraction;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.utils.CommandUtils;
import com.markiesch.utils.TimeUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class BanCommand extends CommandBase {
    public BanCommand() {
        super(
                "ban",
                "epicpunishments.ban",
                "§7Usage: §e/ban <target> <duration | permanent> (reason)",
                2,
                -1,
                false
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        final ProfileModel profileModel = ProfileManager.getInstance().getPlayer(args[0]);

        if (profileModel == null) {
            sender.sendMessage(Translation.COMMAND_PLAYER_NOT_FOUND.addPlaceholder("name", args[0]).toString());
            return true;
        }

        long duration = TimeUtils.parseTime(args[1]);
        List<String> arguments = Arrays.asList(args);
        String reason = String.join(" ", arguments.subList(2, arguments.size()));

        new PreparedInfraction(
            InfractionType.BAN,
            sender,
            profileModel.uuid,
            reason,
            duration
        ).execute();

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1) return CommandUtils.getAllOfflinePlayerNames();
        if (args.length == 2) return CommandUtils.getTimeOptions(args[1]);
        else return List.of("reason");
    }
}
