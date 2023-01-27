package com.markiesch.modules.warning;

import com.markiesch.modules.categoryRule.CategoryRuleManager;
import com.markiesch.modules.infraction.PreparedInfraction;
import com.markiesch.modules.profile.ProfileModel;
import com.markiesch.modules.template.TemplateManager;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class WarningUtils {
    public static boolean createWarning(CommandSender commandSender, int categoryId, ProfileModel victim, UUID issuer) {
        boolean success = WarningManager.getInstance().createWarning(categoryId, victim.uuid, issuer);

        if (!success) return false;

        List<WarningModel> warnings = WarningManager.getInstance()
                .getPlayer(victim.uuid)
                .stream()
                .filter(warning -> warning.getCategoryId() == categoryId)
                .collect(Collectors.toList());


        CategoryRuleManager.getInstance()
                .getCategoryRules(categoryId)
                .stream()
                .filter(categoryRuleModel -> categoryRuleModel.getCount() == warnings.size())
                .map(categoryRuleModel -> TemplateManager.getInstance().getTemplate(categoryRuleModel.getTemplateId()))
                .forEach(templateModel -> {
                    if (templateModel == null) return;
                    new PreparedInfraction(
                            templateModel.type,
                            commandSender,
                            victim,
                            templateModel.reason,
                            templateModel.duration
                    ).execute();
                });

        return true;
    }
}
