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

public class TemplateCommand implements CommandExecutor {
    private static final EpicPunishments plugin = EpicPunishments.getPlugin(EpicPunishments.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            new TemplatesMenu(EpicPunishments.getPlayerMenuUtility(player), 0).open();
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length >= 2) {
                new EditTemplateMenu(EpicPunishments.getPlayerMenuUtility(player), args[1]).open();
                return true;
            }

            plugin.getEditor().put(player.getUniqueId(), new InputUtils(InputTypes.TEMPLATE_NAME, player, "§bNew Template", "§7Type in a template name"));
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("§7Usage: §e/template <delete|create|edit> <name>");
            return true;
        }

        if (args[0].equalsIgnoreCase("delete")) {
            plugin.getTemplateStorage().removeTemplate(args[1]);
            player.sendMessage("§7Successfully deleted §a" + args[1]);
            return true;
        }

        return false;
    }
}
