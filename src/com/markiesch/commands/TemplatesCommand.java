package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.InputTypes;
import com.markiesch.menusystem.PlayerMenuUtility;
import com.markiesch.menusystem.menus.EditTemplateMenu;
import com.markiesch.menusystem.menus.TemplatesMenu;
import com.markiesch.utils.InputUtils;
import com.markiesch.utils.TemplateStorage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TemplatesCommand {
    EpicPunishments plugin = EpicPunishments.getInstance();

    public TemplatesCommand() {
        new CommandBase("templates", 0, -1, true) {

            @Override
            public boolean onCommand(CommandSender sender, String[] args) {
                Player player = (Player) sender;

                if (args.length == 0) {
                    new TemplatesMenu(EpicPunishments.getPlayerMenuUtility(player), 0).open();
                    return true;
                }

                PlayerMenuUtility playerMenuUtility = EpicPunishments.getPlayerMenuUtility(player);

                if ("create".equalsIgnoreCase(args[0])) {
                    if (args.length >= 2) {
                        playerMenuUtility.setTemplateName(args[1]);
                        new EditTemplateMenu(playerMenuUtility).open();
                        return true;
                    }

                    plugin.getEditor().put(player.getUniqueId(), new InputUtils(InputTypes.CREATE_TEMPLATE_NAME, player, "§bNew Template", "§7Type in a template name"));
                    return true;
                }

                if ("edit".equalsIgnoreCase(args[0])) {
                    UUID uuid = TemplateStorage.getUUIDFromName(args[1]);
                    if (uuid == null) {
                        player.sendMessage("§cThe given template could not be found...");
                        return true;
                    }

                    playerMenuUtility.reset();
                    playerMenuUtility.setUUID(uuid);
                    new EditTemplateMenu(playerMenuUtility).open();
                    return true;
                }

                if ("delete".equalsIgnoreCase(args[0])) {
                    UUID uuid = TemplateStorage.getUUIDFromName(args[1]);
                    if (uuid == null) {
                        player.sendMessage("§cThe given template could not be found...");
                        return true;
                    }

                    if (!TemplateStorage.removeTemplate(uuid)) {
                        player.sendMessage("§cFailed to delete " + args[1]);
                        return true;
                    }

                    player.sendMessage("§7Successfully deleted §a" + args[1]);
                    return true;
                }

                return true;
            }

            @Override
            public String getUsage() { return ""; }
            public String getPermission() { return "epicpunishments.templates"; }
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                List<String> result = new ArrayList<>();
                if (!sender.hasPermission(getPermission())) return result;
                return TemplateTabCompleter.onTabComplete(args);
            }
        };
    }
}
