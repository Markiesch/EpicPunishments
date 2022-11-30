package com.markiesch.commands;

import com.markiesch.locale.Translation;
import com.markiesch.modules.infraction.InfractionList;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.modules.infraction.InfractionType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class UnmuteCommand extends CommandBase {
    public UnmuteCommand() {
        super("unmute",
                "epicpunishments.unmute",
                "§7Usage: §e/unmute <target>",
                1,
                -1,
                false);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        OfflinePlayer victim = Bukkit.getOfflinePlayer(args[0]);

        InfractionManager infractionManager = InfractionManager.getInstance();
        InfractionList infractionList = infractionManager.getPlayer(victim.getUniqueId());

        if (!infractionList.isMuted()) {
            sender.sendMessage(Translation.COMMAND_UNMUTE_NOT_MUTED.addPlaceholder("victim_name", victim.getName()).toString());
            return true;
        }

        infractionManager.expirePunishments(victim.getUniqueId(), InfractionType.MUTE);
        sender.sendMessage(Translation.COMMAND_UNMUTE_SUCCESS.addPlaceholder("victim_name", victim.getName()).toString());

        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return new ArrayList<>();
    }
}
