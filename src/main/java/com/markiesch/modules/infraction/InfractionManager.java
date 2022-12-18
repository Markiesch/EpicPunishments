package com.markiesch.modules.infraction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InfractionManager {
    private final Map<UUID, InfractionList> infractionsMap;
    private final InfractionController infractionController;

    private InfractionManager() {
        infractionsMap = new HashMap<>();
        infractionController = new InfractionController();
    }

    public void initialize() {
        InfractionList infractionList = infractionController.readAll();

        for (InfractionModel infractionModel : infractionList) {
            infractionsMap.computeIfAbsent(infractionModel.victim, k -> new InfractionList()).add(infractionModel);
        }
    }

    private static class InfractionManagerHolder {
        public static final InfractionManager INSTANCE = new InfractionManager();
    }

    public static InfractionManager getInstance() {
        return InfractionManagerHolder.INSTANCE;
    }


    public boolean createInfraction(PreparedInfraction preparedInfraction) {
        InfractionModel infractionModel = infractionController.create(preparedInfraction);

        if (infractionModel == null) return false;

        this.getPlayer(preparedInfraction.victimProfile.uuid).add(infractionModel);
        return true;
    }

    public boolean expirePunishments(UUID victim, InfractionType infractionType) {
        InfractionList activeList = getPlayer(victim).getActiveByType(infractionType);

        if (activeList.isEmpty()) return false;

        new InfractionController().expire(activeList);

        for (InfractionModel infractionModel : activeList) {
            infractionModel.setRevoked(true);
        }

        return true;
    }

    public boolean deletePunishment(InfractionModel infractionModel) {
        boolean success = infractionController.delete(infractionModel.id);

        if (success) {
            this.getPlayer(infractionModel.victim)
                    .removeIf(item -> item.id == infractionModel.id);
        }

        return success;
    }

    public InfractionList getPlayer(UUID uuid) {
        return infractionsMap.computeIfAbsent(uuid, (T) -> new InfractionController().readAll(uuid));
    }
}
