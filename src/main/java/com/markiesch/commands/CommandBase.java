package com.markiesch.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandBase extends BukkitCommand implements CommandExecutor {
    private final int minArgs;
    private final int maxArgs;
    private final boolean playerOnly;
    private final String permission;

    protected abstract boolean onCommand(CommandSender sender, String[] args);
    protected abstract List<String> onTabComplete(CommandSender sender, String alias, String[] args);

    public CommandBase(
            String command,
            String permission,
            String usage,
            int minArgs,
            int maxArgs,
            boolean playerOnly
    ) {
        super(command);
        this.permission = permission;
        this.setUsage(usage);
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        this.playerOnly = playerOnly;

        CommandMap commandMap = getCommandMap();
        if (commandMap != null) {
            commandMap.register(command, this);
        }
    }

    public CommandMap getCommandMap() {
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);

                return (CommandMap) field.get(Bukkit.getPluginManager());
            }
        } catch (NoSuchFieldException | IllegalAccessException error) {
            error.printStackTrace();
        }

        return null;
    }

    public void sendUsage(CommandSender sender) {
        sender.sendMessage(getUsage());
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] arguments) {
        if (permission != null && !sender.hasPermission(permission)) {
            this.sendPermissionMessage(sender);
            return true;
        }

        if (arguments.length < minArgs || (arguments.length > maxArgs && maxArgs != -1)) {
            sendUsage(sender);
            return true;
        }

        if (playerOnly && !(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        if (!onCommand(sender, arguments)) sendUsage(sender);
        return true;
    }

    public void sendPermissionMessage(CommandSender sender) {
        sender.sendMessage("§7You do not have§c permissions §7to use this command!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        return this.onCommand(sender, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if (!sender.hasPermission(permission)) return new ArrayList<>();
        return onTabComplete(sender, alias, args);
    }
}
