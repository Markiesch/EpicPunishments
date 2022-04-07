package com.markiesch.commands;

import com.markiesch.controllers.TemplateController;
import com.markiesch.EpicPunishments;
import com.markiesch.utils.InputTypes;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.menusystem.menus.CreateTemplateMenu;
import com.markiesch.menusystem.menus.EditTemplateMenu;
import com.markiesch.menusystem.menus.TemplatesMenu;
import com.markiesch.utils.InputUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class TemplatesCommand extends CommandBase {
    EpicPunishments plugin = EpicPunishments.getInstance();
    private final TemplateController templateController;

    public TemplatesCommand() {
        super("templates", 0, -1, true);
        templateController = EpicPunishments.getTemplateController();
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            new TemplatesMenu(EpicPunishments.getPlayerMenuUtility(player), 0);
            return true;
        }

        PlayerMenuUtility playerMenuUtility = EpicPunishments.getPlayerMenuUtility(player);

        if ("create".equalsIgnoreCase(args[0])) {
            if (args.length >= 2) {
                playerMenuUtility.setTemplateName(args[1]);
                new CreateTemplateMenu(playerMenuUtility);
                return true;
            }

            plugin.getEditor().put(player.getUniqueId(), new InputUtils(InputTypes.CREATE_TEMPLATE_NAME, player, "§bNew Template", "§7Type in a template name"));
            return true;
        }

        if ("edit".equalsIgnoreCase(args[0])) {
            UUID uuid = templateController.getUUIDFromName(args[1]);
            if (uuid == null) {
                player.sendMessage("§cThe given template could not be found...");
                return true;
            }

            playerMenuUtility.reset();
            playerMenuUtility.setUUID(uuid);
            new EditTemplateMenu(playerMenuUtility);
            return true;
        }

        if ("delete".equalsIgnoreCase(args[0])) {
            UUID uuid = templateController.getUUIDFromName(args[1]);
            if (uuid == null) {
                player.sendMessage("§cThe given template could not be found...");
                return true;
            }

            if (!templateController.removeTemplate(uuid)) {
                player.sendMessage("§cFailed to delete " + args[1]);
                return true;
            }

            player.sendMessage("§7Successfully deleted §a" + args[1]);
            return true;
        }

        return true;
    }

    @Override public String getUsage() { return ""; }
    @Override public String getPermission() { return "epicpunishments.templates"; }
    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return TemplateTabCompleter.onTabComplete(args);
    }
}
