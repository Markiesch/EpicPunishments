package com.markiesch.commands;

import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import com.markiesch.modules.infraction.InfractionManager;
import com.markiesch.utils.ChatUtils;
import com.markiesch.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ImMutedCommand extends CommandBase {
    private final Plugin plugin;

    public ImMutedCommand(Plugin plugin) {
        super("immuted", Permission.IM_MUTED_EXECUTE, "§7Usage: §e/immuted <player>", 1, 1, true);

        this.plugin = plugin;
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        boolean isMuted = InfractionManager.getInstance().getPlayer(player.getUniqueId()).isMuted();
        if (!isMuted) {
            player.sendMessage(Translation.COMMAND_IM_MUTED_NOT_MUTED.toString());
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(Translation.COMMAND_PLAYER_NOT_FOUND.addPlaceholder("name", args[0]).toString());
            return true;
        }

        boolean executeCommand = plugin.getConfig().getBoolean("im_muted.execute_command");

        if (executeCommand) {
            String command = plugin.getConfig().getString("im_muted.command");
            if (command != null) {
                player.performCommand(command
                        .replace("[target]", target.getName())
                        .replace("[issuer]", player.getName())
                        .replace("[message]", Translation.COMMAND_IM_MUTED_MESSAGE.toString())
                );
            }
        } else {
            String message = plugin.getConfig().getString("im_muted.message");
            if (message != null) {
                target.sendMessage(
                        ChatUtils.changeColor(
                                message
                                        .replace("[target]", target.getName())
                                        .replace("[issuer]", player.getName())
                                        .replace("[message]", Translation.COMMAND_IM_MUTED_MESSAGE.toString())
                        )
                );
            }
        }

        player.sendMessage(Translation.COMMAND_IM_MUTED_SUCCESS.toString());

        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return CommandUtils.getAllOnlinePlayerNames(args[0]);
    }
}
