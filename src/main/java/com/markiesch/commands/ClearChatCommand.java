package com.markiesch.commands;

import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClearChatCommand extends CommandBase {
    public ClearChatCommand() {
        super("ClearChat", Permission.COMMAND_CLEARCHAT_EXECUTE, "§7Usage: §e/clearchat", -1, -1, false);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        String[] content = new String[150];
        Arrays.fill(content, "");

        Bukkit.getOnlinePlayers()
                .forEach(player -> {
                    if (!player.hasPermission(Permission.COMMAND_CLEARCHAT_BYPASS.getNode())) {
                        player.sendMessage(content);
                    }

                    if (!(sender instanceof Player) || player.getUniqueId() != ((Player) sender).getUniqueId()) {
                        player.sendMessage(Translation.COMMAND_CLEAR_CHAT_BROADCAST
                                .addPlaceholder("issuer", sender instanceof Player ? sender.getName() : "Console")
                                .toString()
                        );
                    }
                });

        sender.sendMessage(Translation.COMMAND_CLEAR_CHAT_SUCCESS.toString());

        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return new ArrayList<>();
    }
}
