package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.menusystem.menus.TemplateSelectorMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TemplatesCommand extends CommandBase {
    private final EpicPunishments plugin;

    public TemplatesCommand(EpicPunishments plugin) {
        super(
                "templates",
                "epicpunishments.templates",
                "§7Usage: §e/templates",
                0,
                -1,
                true
        );
        this.plugin = plugin;
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        Player player = (Player)sender;

        new TemplateSelectorMenu(plugin, player.getUniqueId());
        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return null;
    }
}
