package com.markiesch.commands;

import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RandomPlayerCommand extends CommandBase {
    public RandomPlayerCommand() {
        super("randomPlayer", Permission.RANDOM_PLAYER, "§7Usage: §e/randomplayer", -1, -1, true);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        List<Player> onlinePlayers = Bukkit.getOnlinePlayers()
                .stream()
                .filter(onlinePlayer -> onlinePlayer.getUniqueId() != player.getUniqueId() && !onlinePlayer.hasPermission(Permission.RANDOM_PLAYER_EXEMPT.getNode()))
                .collect(Collectors.toList());


        if (onlinePlayers.size() == 0) {
            player.sendMessage(Translation.COMMAND_RANDOM_PLAYER_NO_PLAYERS.toString());
            return true;
        }

        Collections.shuffle(onlinePlayers);
        Player target = onlinePlayers.get(0);

        player.teleport(target);
        player.sendMessage(Translation.COMMAND_RANDOM_PLAYER_SUCCESS.addPlaceholder("target", target.getName()).toString());

        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return new ArrayList<>();
    }
}
