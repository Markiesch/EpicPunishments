package com.markiesch.modules.warning;

import com.markiesch.modules.categoryRule.CategoryRuleManager;
import com.markiesch.modules.infraction.PreparedInfraction;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.modules.template.TemplateManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class WarningUtils {
    public static boolean createWarning(CommandSender commandSender, @Nullable Integer categoryId, @Nullable String reason, ProfileModel victim, UUID issuer) {
        boolean victimIsOnline = victim.getPlayer().isOnline();

        WarningModel warningModel = WarningManager.getInstance().createWarning(categoryId, reason, victim.uuid, issuer, victimIsOnline);
        if (warningModel == null) return false;

        if (victim.getPlayer().isOnline()) {
            Player player = (Player) victim.getPlayer();
            player.sendMessage(warningModel.getFormat());
        }

        if (categoryId != null) {
            List<WarningModel> warnings = WarningManager.getInstance()
                .getPlayer(victim.uuid)
                .stream()
                .filter(warning -> Objects.equals(warning.getCategoryId(), categoryId))
                .collect(Collectors.toList());

            CategoryRuleManager.getInstance()
                .getCategoryRules(categoryId)
                .stream()
                .filter(categoryRuleModel -> categoryRuleModel.getCount() == warnings.size())
                .map(categoryRuleModel -> TemplateManager.getInstance().getTemplate(categoryRuleModel.getTemplateId()))
                .forEach(templateModel -> {
                    if (templateModel == null) return;
                    new PreparedInfraction(
                        templateModel.getType(),
                        commandSender,
                        victim,
                        templateModel.getReason(),
                        templateModel.getDuration()
                    ).execute();
                });
        }

        return true;
    }
}
