package com.markiesch.commands;

import com.markiesch.EpicPunishments;
import com.markiesch.controllers.InfractionController;
import com.markiesch.models.ProfileModel;
import com.markiesch.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BanCommand extends CommandBase {
    private final EpicPunishments plugin;

    public BanCommand(EpicPunishments plugin) {
        super(
            "ban",
            "epicpunishments.ban",
            "§7Usage: §e/ban <target> <duration | permanent> (reason)",
            2,
            -1,
            true
        );
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        Player issuer = (Player) sender;

        OfflinePlayer target = Bukkit.getPlayer(args[0]);
        if (target == null) target = Bukkit.getOfflinePlayer(args[0]);

        long duration = TimeUtils.parseTime(args[1]);
        List<String> arguments = Arrays.asList(args);
        String reason = String.join(" ", arguments.subList(2, arguments.size()));

        ProfileModel profile = plugin.getProfileController().getProfile(target.getUniqueId());
//        PlayerStorage.createPunishment(target.getUniqueId(), issuer.getUniqueId(), PunishTypes.BAN, reason, duration);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return new ArrayList<>();
    }
}
