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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class WarnCommand extends CommandBase {

    public WarnCommand() {
        super("warn", Permission.EXECUTE_WARN, "§7Usage: §e/warn <target> <category | reason>", 2, -1, false);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        final ProfileModel profileModel = ProfileManager.getInstance().getPlayer(args[0]);
        if (profileModel == null) {
            sender.sendMessage(Translation.COMMAND_PLAYER_NOT_FOUND.addPlaceholder("name", args[0]).toString());
            return true;
        }

        List<String> arguments = Arrays.asList(args);
        String categoryNameInput = String.join(" ", arguments.subList(1, arguments.size()));
        CategoryModel category = CategoryManager.getInstance().getCategoryByName(categoryNameInput);


        UUID issuer = sender instanceof Player ? ((Player) sender).getUniqueId() : null;

        boolean success;

        if (category == null) {
            success = WarningUtils.createWarning(sender, null, categoryNameInput, profileModel, issuer);
        } else {
            success = WarningUtils.createWarning(sender, category.getId(), null, profileModel, issuer);
        }

        if (success) {
            sender.sendMessage(Translation.EVENT_WARN_SUCCESS
                .addPlaceholder("victim_name", args[0])
                .addPlaceholder("reason", categoryNameInput)
                .toString()
            );
        }

        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1) return CommandUtils.getAllOfflinePlayerNames(args[0]);
        if (args.length == 2) return CategoryManager.getInstance().getCategoryNames(args[1]);
        return new ArrayList<>();
    }
}
