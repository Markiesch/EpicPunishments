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

public class UnbanCommand extends CommandBase {

    public UnbanCommand() {
        super("unban",
                Permission.REVOKE_BAN,
                "§7Usage: §e/unban <target>",
                1,
                -1,
                false
        );
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        ProfileModel victim = ProfileManager.getInstance().getPlayer(args[0]);

        if (victim == null) {
            sender.sendMessage(Translation.COMMAND_PLAYER_NOT_FOUND.addPlaceholder("name", args[0]).toString());
            return true;
        }

        InfractionManager infractionManager = InfractionManager.getInstance();
        InfractionList infractionList = infractionManager.getPlayer(victim.uuid);

        if (!infractionList.isBanned()) {
            sender.sendMessage(Translation.COMMAND_UNBAN_NOT_BANNED.addPlaceholder("victim_name", victim.getName()).toString());
            return true;
        }

        infractionManager.expirePunishments(victim.uuid, InfractionType.BAN);
        sender.sendMessage(Translation.COMMAND_UNBAN_SUCCESS.addPlaceholder("victim_name", victim.getName()).toString());

        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1) return CommandUtils.getAllOfflinePlayerNames(args[0]);
        return new ArrayList<>();
    }
}
