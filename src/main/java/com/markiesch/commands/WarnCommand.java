package com.markiesch.commands;

import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import com.markiesch.modules.category.CategoryManager;
import com.markiesch.modules.category.CategoryModel;
import com.markiesch.modules.profile.ProfileManager;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.modules.warning.WarningUtils;
import com.markiesch.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarnCommand extends CommandBase {

    public WarnCommand() {
        super("warn", Permission.EXECUTE_WARN, "§7Usage: §e/warn <target> <category>", 2, -1, false);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        final ProfileModel profileModel = ProfileManager.getInstance().getPlayer(args[0]);
        if (profileModel == null) {
            sender.sendMessage(Translation.COMMAND_PLAYER_NOT_FOUND.addPlaceholder("name", args[0]).toString());
            return true;
        }

        CategoryModel category = CategoryManager.getInstance().getCategoryByName(args[1]);
        if (category == null) {
            return false;
        }

        UUID issuer = sender instanceof Player ? ((Player) sender).getUniqueId() : null;

        boolean success = WarningUtils.createWarning(sender, category.getId(), profileModel, issuer);

        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1) return CommandUtils.getAllOfflinePlayerNames(args[0]);
        if (args.length == 2) return CategoryManager.getInstance().getCategoryNames(args[1]);
        return new ArrayList<>();
    }
}
