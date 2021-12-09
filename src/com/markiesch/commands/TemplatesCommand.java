package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.InputTypes;
import com.markiesch.menusystem.menus.EditTemplateMenu;
import com.markiesch.menusystem.menus.TemplatesMenu;
import com.markiesch.utils.InputUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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

                if ("create".equalsIgnoreCase(args[0])) {
                    if (args.length >= 2) {
                        new EditTemplateMenu(EpicPunishments.getPlayerMenuUtility(player), args[1]).open();
                        return true;
                    }

                    plugin.getEditor().put(player.getUniqueId(), new InputUtils(InputTypes.TEMPLATE_NAME, player, "§bNew Template", "§7Type in a template name"));
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage("§7Usage: §e/templates <delete|create|edit> <name>");
                    return true;
                }

                if ("delete".equalsIgnoreCase(args[0])) {
                    plugin.getTemplateStorage().removeTemplate(args[1]);
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
                return TemplateTabCompleter.onTabComplete(sender, args);
            }
        };
    }
}
