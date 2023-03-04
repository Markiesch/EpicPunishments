package com.markiesch.commands;

import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import com.markiesch.modules.infraction.InfractionList;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.infraction.InfractionType;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.utils.CommandUtils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class InfoCommand extends CommandBase {
    public InfoCommand() {
        super("punishinfo", Permission.EXECUTE_INFO, "§7Usage: §e/punishinfo <player>", 1, -1, false);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        ProfileModel profileModel = ProfileManager.getInstance().getPlayer(args[0]);

        if (profileModel == null) {
            sender.sendMessage(
                    Translation.COMMAND_PLAYER_NOT_FOUND
                            .addPlaceholder("name", args[0])
                            .toString()
            );
            return true;
        }

        InfractionList infractionList = InfractionManager.getInstance().getPlayer(profileModel.uuid);

        InfractionList banList = infractionList.getByType(InfractionType.BAN);
        InfractionList muteList = infractionList.getByType(InfractionType.MUTE);

        List<String> content = Translation.COMMAND_INFO_CONTENT
                .addPlaceholder("name", profileModel.getName())
                .addPlaceholder("is_banned", infractionList.isBanned())
                .addPlaceholder("is_muted", infractionList.isMuted())
                .addPlaceholder("ban_format",
                        (banList.isEmpty() ?
                                Translation.MENU_INFRACTION_PLAYER_EMPTY :
                                Translation.MENU_INFRACTION_PLAYER_FILLED.addPlaceholder("size", banList.size())
                        ).addPlaceholder("punish_type", Translation.WORD_BANNED.toString()).toString()
                )
                .addPlaceholder("mute_format",
                        (muteList.isEmpty() ?
                                Translation.MENU_INFRACTION_PLAYER_EMPTY :
                                Translation.MENU_INFRACTION_PLAYER_FILLED.addPlaceholder("size", muteList.size())
                        ).addPlaceholder("punish_type", Translation.WORD_MUTED.toString()).toString()
                )
                .toList();

        for (String line : content) {
            sender.sendMessage(line);
        }

        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1) return CommandUtils.getAllOfflinePlayerNames(args[0]);
        return new ArrayList<>();
    }
}
