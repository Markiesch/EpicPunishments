package com.markiesch.modules.infraction;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class InfractionList extends ArrayList<InfractionModel> {
    public InfractionList getActiveByType(InfractionType infractionType) {
        return stream()
                .filter(InfractionModel::isActive)
                .filter(infractionModel -> infractionModel.type == infractionType)
                .collect(Collectors.toCollection(InfractionList::new));
    }

    public boolean isMuted() {
        return (getActiveByType(InfractionType.MUTE).size() > 0);
    }

    public boolean isBanned() {
        return (getActiveByType(InfractionType.BAN).size() > 0);
    }
}
