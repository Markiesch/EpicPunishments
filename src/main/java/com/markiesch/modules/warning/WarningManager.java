package com.markiesch.modules.warning;

import org.jetbrains.annotations.Nullable;

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

    public @Nullable WarningModel createWarning(@Nullable Integer categoryId, @Nullable String reason, UUID victim, UUID issuer, boolean isOnline) {
        WarningModel warningModel = new WarningController().create(categoryId, reason, victim, issuer, isOnline);

        if (warningModel == null) return null;

        warnings.add(warningModel);
        return warningModel;
    }

    public void updateWarning(int warningId, boolean seen) {
        int affectedRows = new WarningController().update(warningId, seen);

        if (affectedRows == 0) return;

        warnings.stream()
                .filter(warningModel -> warningModel.getId() == warningId)
                .findFirst()
                .ifPresent(warningModel -> {
                    warningModel.setSeen(seen);
                });
    }

    public boolean delete(int id) {
        int affectedRows = new WarningController().delete(id);
        if (affectedRows == 0) return false;

        warnings.removeIf(warning -> warning.getId() == id);

        return true;
    }
}
