package com.markiesch.commands;

import com.markiesch.locale.Translation;
import com.markiesch.modules.infraction.InfractionType;
import com.markiesch.modules.infraction.PreparedInfraction;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.utils.CommandUtils;
import org.bukkit.command.CommandSender;

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

        List<String> arguments = Arrays.asList(args);
        String reason = String.join(" ", arguments.subList(1, arguments.size()));

        new PreparedInfraction(
                InfractionType.KICK,
                sender,
                profileModel.uuid,
                reason,
                0L
        ).execute();

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1) return CommandUtils.getAllOnlinePlayerNames();
        else return List.of("reason");
    }
}
