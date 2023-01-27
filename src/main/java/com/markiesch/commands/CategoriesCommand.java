package com.markiesch.commands;

import com.markiesch.Permission;
import com.markiesch.menusystem.menus.CategoriesMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CategoriesCommand extends CommandBase {
    private final Plugin plugin;

    public CategoriesCommand(Plugin plugin) {
        super("categories", Permission.CATEGORIES_EXECUTE, "§7Usage: §e/categories", -1, 0, true);

        this.plugin = plugin;
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        new CategoriesMenu(plugin, ((Player)sender).getUniqueId());
        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return new ArrayList<>();
    }
}
