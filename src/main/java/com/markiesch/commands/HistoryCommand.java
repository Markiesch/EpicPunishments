package com.markiesch.commands;

import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import com.markiesch.menusystem.menus.HistoryMenu;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class HistoryCommand extends CommandBase {
    private final Plugin plugin;

    public HistoryCommand(Plugin plugin) {
        super("history", Permission.HISTORY_MENU, "§7Usage: §e/history <player>", 1, -1, true);

        this.plugin = plugin;
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        final ProfileModel profileModel = ProfileManager.getInstance().getPlayer(args[0]);
        if (profileModel == null) {
            sender.sendMessage(Translation.COMMAND_PLAYER_NOT_FOUND.addPlaceholder("name", args[0]).toString());
            return true;
        }

        new HistoryMenu(plugin, player.getUniqueId(), profileModel.uuid);
        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1) return CommandUtils.getAllOfflinePlayerNames(args[0]);
        return new ArrayList<>();
    }
}
