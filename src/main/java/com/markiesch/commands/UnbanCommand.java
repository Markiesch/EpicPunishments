package com.markiesch.commands;

import com.markiesch.locale.Translation;
import com.markiesch.modules.infraction.InfractionController;
import com.markiesch.modules.infraction.InfractionList;
import com.markiesch.modules.infraction.InfractionManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class UnbanCommand extends CommandBase {

    public UnbanCommand() {
        super("unban",
                "epicpunishments.unban",
                "§7Usage: §e/unban <target>",
                1,
                -1,
                false);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        OfflinePlayer victim = Bukkit.getOfflinePlayer(args[0]);

        InfractionManager infractionManager = InfractionManager.getInstance();
        InfractionList infractionList = infractionManager.getPlayer(victim.getUniqueId());

        if (!infractionList.isBanned()) {
            sender.sendMessage(Translation.COMMAND_UNBAN_NOT_BANNED.addPlaceholder("victim_name", victim.getName()).toString());
            return true;
        }

        return false;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return new ArrayList<>();
    }
}
