package com.markiesch.modules.infraction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InfractionManager {
    private final Map<UUID, InfractionList> infractionsMap;

    private InfractionManager() {
        infractionsMap = new HashMap<>();
    }

    private static class InfractionManagerHolder {
        public static final InfractionManager INSTANCE = new InfractionManager();
    }

    public static InfractionManager getInstance() {
        return InfractionManagerHolder.INSTANCE;
    }

    public boolean createInfraction(PreparedInfraction preparedInfraction) {
        InfractionModel infractionModel = new InfractionController().create(preparedInfraction);

        if (infractionModel == null) return false;

        this.getPlayer(preparedInfraction.victim.getUniqueId()).add(infractionModel);
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

    public void removePlayer(UUID uuid) {
        infractionsMap.remove(uuid);
    }

    public InfractionList getPlayer(UUID uuid) {
        return infractionsMap.computeIfAbsent(uuid, (T) -> new InfractionController().readAll(uuid));
    }
}
