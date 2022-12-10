package com.markiesch.commands;

import com.markiesch.locale.Translation;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandBase extends BukkitCommand implements CommandExecutor {
    private static final String COMMAND_FALLBACK_PREFIX = "epicpunishments";
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

        registerCommand();
    }

    private void registerCommand() {
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);

                CommandMap commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
                commandMap.register(COMMAND_FALLBACK_PREFIX, this);
            }
        } catch (NoSuchFieldException | IllegalAccessException error) {
            error.printStackTrace();
        }
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(getUsage());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, String[] arguments) {
        if (permission != null && !sender.hasPermission(permission)) {
            this.sendPermissionMessage(sender);
            return true;
        }

        if (arguments.length < minArgs || (arguments.length > maxArgs && maxArgs != -1)) {
            sendUsage(sender);
            return true;
        }

        if (playerOnly && !(sender instanceof Player)) {
            sender.sendMessage(Translation.COMMAND_PLAYER_ONLY.toString());
            return true;
        }

        if (!onCommand(sender, arguments)) sendUsage(sender);
        return true;
    }

    private void sendPermissionMessage(CommandSender sender) {
        sender.sendMessage(Translation.COMMAND_NO_PERMISSION.toString());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return this.onCommand(sender, args);
    }

    @Override
    public @NotNull List<String> tabComplete(CommandSender sender, @NotNull String alias, String[] args) {
        if (!sender.hasPermission(permission)) return new ArrayList<>();
        return onTabComplete(sender, alias, args);
    }
}
