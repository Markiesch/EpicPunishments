package com.markiesch.commands;

import com.markiesch.Permission;
import com.markiesch.locale.Translation;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandBase extends BukkitCommand {
    private static final String COMMAND_FALLBACK_PREFIX = "epicpunishments";
    private final int minArgs;
    private final int maxArgs;
    private final boolean playerOnly;

    public CommandBase(
            String command,
            Permission permission,
            String usage,
            int minArgs,
            int maxArgs,
            boolean playerOnly
    ) {
        super(command);
        this.setPermission(permission.getNode());
        this.setUsage(usage);
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        this.playerOnly = playerOnly;

        registerCommand();
    }

    protected abstract boolean onCommand(CommandSender sender, String[] args);
    protected abstract List<String> onTabComplete(CommandSender sender, String alias, String[] args);

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
        if (getPermission() != null && !sender.hasPermission(getPermission())) {
            sender.sendMessage(Translation.COMMAND_NO_PERMISSION.toString());
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

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        if (getPermission() != null && !sender.hasPermission(getPermission())) return new ArrayList<>();
        return onTabComplete(sender, alias, args);
    }
}
