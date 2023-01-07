package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.Permission;
import com.markiesch.menusystem.menus.PlayerSelectorMenu;
import com.markiesch.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PunishCommand extends CommandBase {
    private final EpicPunishments plugin;

    public PunishCommand(EpicPunishments plugin) {
        super(
                "punish",
                Permission.MANAGE_PLAYERS,
                "§7Usage: §e/punish <help | reload>",
                0,
                -1,
                false
        );
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length > 0) {
            if ("help".equalsIgnoreCase(args[0])) {
                showHelpPage(sender);
                return true;
            }
            if ("reload".equalsIgnoreCase(args[0])) {
                reloadConfig(sender);
                return true;
            }

            return false;
        }

        if (!(sender instanceof Player)) {
            showHelpPage(sender);
            return true;
        }

        Player player = (Player) sender;
        new PlayerSelectorMenu(plugin, player.getUniqueId());

        return true;
    }

    private void showHelpPage(CommandSender sender) {
        String[] helpPage = {
                " ",
                "&c&lEpicPunishments &7v" + plugin.getDescription().getVersion(),
                " ",
                "&9/punish (help | reload) &7opens the main menu",
                "&9/ban &e<player> <duration> (reason) &7bans the mentioned player",
                "&9/unban &e<player> &7will unban the mentioned player",
                "&9/mute &e<player> <duration> (reason) &7mutes the mentioned player",
                "&9/unmute &e<player> <duration> (reason) &7will unmute the mentioned player",
                "&9/kick &e<player> (reason) &7kicks the mentioned player",
                "&9/warn &e<player> <duration> (reason) &7warns the mentioned player",
                "&9/templates &7opens the template management menu",
                " "
        };

        sender.sendMessage(ChatUtils.changeColor(String.join("\n", helpPage)));
    }

    private void reloadConfig(CommandSender sender) {
        long startTime = System.currentTimeMillis();
        plugin.reloadConfig();
        EpicPunishments.getLangConfig().reloadConfig();
        sender.sendMessage("§aConfig files reloaded! Took " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        List<String> list = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission(Permission.ADMIN_RELOAD.getNode())) list.add("reload");
            if (sender.hasPermission(Permission.ADMIN_HELP.getNode())) list.add("help");
        }

        return list;
    }
}
