package com.markiesch.modules.warning;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class WarningManager {
    private final List<WarningModel> warnings;

    private WarningManager() {
        warnings = new ArrayList<>();
    }

    public void initialize() {
        warnings.addAll(new WarningController().readAll());

        for (WarningModel warning : warnings) {
            System.out.println("issuer" + warning.getIssuer());
            System.out.println("victim" + warning.getVictim());
            System.out.println("id" + warning.getId());
        }
    }

    private static class WarningManagerHolder {
        public static final WarningManager INSTANCE = new WarningManager();
    }

    public static WarningManager getInstance() {
        return WarningManagerHolder.INSTANCE;
    }

    public List<WarningModel> getPlayer(UUID uuid) {
        return warnings.stream()
                .filter(warning -> warning.getVictim().equals(uuid))
                .collect(Collectors.toList());
    }

    public boolean createWarning(int categoryId, UUID victim, UUID issuer) {
        WarningModel warningModel = new WarningController().create(categoryId, victim, issuer);

        if (warningModel == null) return false;

        warnings.add(warningModel);
        return true;
    }

    public boolean delete(int id) {
        int affectedRows = new WarningController().delete(id);
        if (affectedRows == 0) return false;

        warnings.removeIf(warning -> warning.getId() == id);

        return true;
    }
}
