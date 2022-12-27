package com.markiesch.modules.infraction;

import com.markiesch.locale.Translation;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.utils.TimeUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class InfractionUtils {
    public static void commandToInfraction(CommandSender commandSender, String[] args, InfractionType infractionType) {
        final ProfileModel profileModel = ProfileManager.getInstance().getPlayer(args[0]);

        if (profileModel == null) {
            commandSender.sendMessage(Translation.COMMAND_PLAYER_NOT_FOUND.addPlaceholder("name", args[0]).toString());
            return;
        }

        long duration = infractionType == InfractionType.KICK ? 0L : TimeUtils.parseTime(args[1]);
        List<String> arguments = Arrays.asList(args);
        int splitIndex = infractionType == InfractionType.KICK ? 1 : 2;
        String reason = String.join(" ", arguments.subList(splitIndex, arguments.size()));

        new PreparedInfraction(
                infractionType,
                commandSender,
                profileModel,
                reason,
                duration
        ).execute();
    }
}
