package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.PlayerSelectorSearchType;
import com.markiesch.menusystem.menus.PlayerSelectorMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PunishCommand extends CommandBase {
    private final EpicPunishments plugin;

    public PunishCommand(EpicPunishments plugin) {
        super(
            "punish",
            "epicpunishments.punish",
            "§7Usage: §e/punish (target)",
            0,
            -1,
            true
        );
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        new PlayerSelectorMenu(plugin, player.getUniqueId(), 0, PlayerSelectorSearchType.ALL);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return new ArrayList<>();
    }
}
